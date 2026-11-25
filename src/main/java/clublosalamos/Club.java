package clublosalamos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Club
 *
 * Responsabilidades:
 * 1. Guarda y manipula las colecciones de recursos, socios y reservas para su
 *   consulta y gestión, registrándolos y buscándolos por su identificador.
 * 2. Registra reservas, verificando que el socio esté registrado y habilitado
 *   y que el recurso esté registrado y disponible en la fecha y franja
 *   horaria solicitadas (sin superponerse con las reservas ya registradas),
 *   y generando el número único de reserva.
 * 3. Delega en las reservas su confirmación, cancelación y finalización.
 * 4. Consulta reservas por socio, por recurso y fecha, y por día.
 * 5. Verifica la situación administrativa de un socio, lo da de baja,
 *   suspende a un socio habilitado, pasa a moroso a un socio suspendido y
 *   habilita a un socio suspendido o moroso.
 * 6. Configura los porcentajes de descuento o recargo de cada categoría de
 *   socio.
 *
 * Colaboradores:
 * - Recurso: Club los registra y verifica su disponibilidad.
 * - Socio: Club los registra y verifica si pueden reservar.
 * - Reserva: Club las registra, confirma, cancela, finaliza y consulta.
 * - Pago: Club lo recibe y se lo pasa a la reserva.
 * - EstadoReserva: Club consulta si una reserva está cancelada.
 * - TipoSocio: Club configura el porcentaje de ajuste de cada categoría.
 * ============================================================================
 */
public class Club {
    private final Map<String, Recurso> recursos = new HashMap<>();
    private final Map<Integer, Socio> socios = new HashMap<>();
    private final Map<Integer, Reserva> reservas = new HashMap<>();
    private final List<Reserva> reservasPorCreacion = new ArrayList<>();
    private int proximoNumeroReserva = 1;

    public void registrarRecurso(Recurso recurso) {
        if (recursos.containsKey(recurso.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un recurso con el código " + recurso.getCodigo());
        }
        recursos.put(recurso.getCodigo(), recurso);
    }

    public void registrarSocio(Socio socio) {
        if (socios.containsKey(socio.getNumeroSocio())) {
            throw new IllegalArgumentException("Ya existe un socio con el número " + socio.getNumeroSocio());
        }
        socios.put(socio.getNumeroSocio(), socio);
    }

    // Describe si un socio puede reservar o no dependiendo de su situacion administrativa.
    public boolean verificarSituacionAdministrativa(int numeroSocio) {
        return obtenerSocio(numeroSocio).puedeReservar();
    }

    // Importe del pago inicial (50% del total) que debe abonar el socio al reservar.
    public BigDecimal calcularPagoInicial(int numeroSocio, String codigoRecurso, int duracionHoras) {
        return Reserva.calcularPagoInicial(obtenerSocio(numeroSocio), obtenerRecurso(codigoRecurso), duracionHoras);
    }

    // Registra una reserva a nombre de un socio. La reserva valida sus propios datos al crearse;
    // el número solo se consume si la reserva queda registrada.
    public Reserva registrarReserva(int numeroSocio, String codigoRecurso, LocalDate fechaUso,
                                    LocalTime horarioInicio, int duracionHoras, Pago pagoInicial) {
        Socio socio = obtenerSocio(numeroSocio);
        if (!socio.puedeReservar()) {
            throw new IllegalStateException("La situación administrativa del socio no permite reservar");
        }
        Recurso recurso = obtenerRecurso(codigoRecurso);
        Reserva reserva = new Reserva(proximoNumeroReserva, socio, recurso, fechaUso, horarioInicio, duracionHoras,
                pagoInicial);
        if (!recurso.estaDisponible(fechaUso, horarioInicio, duracionHoras) || haySuperposicion(reserva)) {
            throw new IllegalStateException("El recurso no está disponible en la fecha y horario solicitados");
        }
        proximoNumeroReserva++;
        reservas.put(reserva.getNumeroReserva(), reserva);
        reservasPorCreacion.add(reserva);
        return reserva;
    }

    // Registra el pago del saldo restante y confirma la reserva.
    public void confirmarReserva(int numeroReserva, Pago pagoSaldo) {
        obtenerReserva(numeroReserva).confirmar(pagoSaldo);
    }

    public void cancelarReserva(int numeroReserva) {
        obtenerReserva(numeroReserva).cancelar();
    }

    public void finalizarReserva(int numeroReserva) {
        obtenerReserva(numeroReserva).finalizar();
    }

    public List<Reserva> consultarReservasPorSocio(int numeroSocio) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reserva : reservasPorCreacion) {
            if (reserva.getSocio().getNumeroSocio() == numeroSocio) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    public List<Reserva> consultarReservasPorRecursoYFecha(String codigoRecurso, LocalDate fecha) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reserva : reservasPorCreacion) {
            if (reserva.getRecurso().getCodigo().equals(codigoRecurso) && reserva.getFechaUso().equals(fecha)) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    // Reservas con fecha de uso en el día indicado, en el orden en que fueron creadas.
    public List<Reserva> consultarReservasDelDia(LocalDate fecha) {
        List<Reserva> delDia = new ArrayList<>();
        for (Reserva reserva : reservasPorCreacion) {
            if (reserva.getFechaUso().equals(fecha)) {
                delDia.add(reserva);
            }
        }
        return delDia;
    }

    public void darSocioDeBaja(int numeroSocio) {
        obtenerSocio(numeroSocio).setSituacionAdmin(DadoDeBaja.getInstancia());
    }

    // Solo un socio habilitado puede ser suspendido.
    public void suspenderSocio(int numeroSocio) {
        obtenerSocio(numeroSocio).suspender();
    }

    // Solo un socio suspendido puede pasar a moroso.
    public void marcarSocioMoroso(int numeroSocio) {
        obtenerSocio(numeroSocio).marcarMoroso();
    }

    // Solo un socio suspendido o moroso puede volver a ser habilitado.
    public void habilitarSocio(int numeroSocio) {
        obtenerSocio(numeroSocio).habilitar();
    }

    // Los porcentajes se expresan en puntos porcentuales: positivo = recargo, negativo = descuento.
    public void configurarAjusteCategoriaSocioActivo(BigDecimal porcentaje) {
        Activo.getInstancia().setPorcentajeAjuste(porcentaje.movePointLeft(2));
    }

    public void configurarAjusteCategoriaSocioVitalicio(BigDecimal porcentaje) {
        SocioVitalicio.getInstancia().setPorcentajeAjuste(porcentaje.movePointLeft(2));
    }

    public void configurarAjusteCategoriaSocioAdherente(BigDecimal porcentaje) {
        Adherente.getInstancia().setPorcentajeAjuste(porcentaje.movePointLeft(2));
    }

    public Socio obtenerSocio(int numeroSocio) {
        Socio socio = socios.get(numeroSocio);
        if (socio == null) {
            throw new IllegalArgumentException("El socio " + numeroSocio + " no está registrado");
        }
        return socio;
    }

    private Recurso obtenerRecurso(String codigoRecurso) {
        Recurso recurso = recursos.get(codigoRecurso);
        if (recurso == null) {
            throw new IllegalArgumentException("El recurso " + codigoRecurso + " no está registrado");
        }
        return recurso;
    }

    public Reserva obtenerReserva(int numeroReserva) {
        Reserva reserva = reservas.get(numeroReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva " + numeroReserva + " no existe");
        }
        return reserva;
    }

    // Las reservas canceladas liberan el recurso, por lo que no cuentan como superposición.
    private boolean haySuperposicion(Reserva nueva) {
        for (Reserva reserva : reservasPorCreacion) {
            if (reserva.getEstado() != Cancelada.getInstancia() && reserva.seSuperponeCon(nueva)) {
                return true;
            }
        }
        return false;
    }
}
