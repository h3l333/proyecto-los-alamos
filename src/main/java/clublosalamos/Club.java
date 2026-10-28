package clublosalamos;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
 * 2. Registra reservas, verificando que el socio esté registrado y habilitado,
 *   que el recurso esté registrado y disponible en la fecha y franja horaria
 *   solicitadas (considerando las reservas ya registradas), y generando el
 *   número único de reserva.
 * 3. Confirma reservas con el pago del saldo pendiente y las cancela.
 * 4. Consulta reservas por socio y por recurso y fecha.
 *
 * Colaboradores:
 * - Recurso: Club los registra y verifica su disponibilidad.
 * - Socio: Club los registra y verifica si pueden reservar.
 * - Reserva: Club las registra, confirma, cancela y consulta.
 * - Pago: Club lo recibe para registrar los pagos de una reserva.
 * ============================================================================
 */
public class Club {
    private static final double PORCENTAJE_PAGO_INICIAL = 0.5;
    private static final double TOLERANCIA_IMPORTE = 0.01;
    private static final String ESTADO_PAGO_PARCIAL = "pagoparcial";
    private static final String ESTADO_CANCELADA = "cancelada";

    private final Map<String, Recurso> recursos = new HashMap<>();
    private final Map<Integer, Socio> socios = new HashMap<>();
    private final Map<String, Reserva> reservas = new HashMap<>();
    private int proximoNumeroReserva = 1;

    public void registrarRecurso(Recurso recurso) {
        recursos.put(recurso.getCodigo(), recurso);
    }

    public void registrarSocio(Socio socio) {
        if (socios.containsKey(socio.getNumeroSocio())) {
            throw new IllegalArgumentException("Ya existe un socio con el número " + socio.getNumeroSocio());
        }
        socios.put(socio.getNumeroSocio(), socio);
    }

    public Recurso buscarRecurso(String codigo) {
        return recursos.get(codigo);
    }

    public Socio buscarSocio(int numeroSocio) {
        return socios.get(numeroSocio);
    }

    public Reserva buscarReserva(String numeroReserva) {
        return reservas.get(numeroReserva);
    }

    // Registra una reserva a nombre de un socio. El pago inicial debe ser el 50% del importe total.
    public Reserva registrarReserva(int numeroSocio, String codigoRecurso, LocalDate fechaUso,
                                    LocalTime horarioInicio, int duracionHoras, Pago pagoInicial) {
        Socio socio = socios.get(numeroSocio);
        if (socio == null) {
            throw new IllegalArgumentException("El socio " + numeroSocio + " no está registrado");
        }
        if (!socio.puedeReservar()) {
            throw new IllegalStateException("La situación administrativa del socio no permite reservar");
        }
        Recurso recurso = recursos.get(codigoRecurso);
        if (recurso == null) {
            throw new IllegalArgumentException("El recurso " + codigoRecurso + " no está registrado");
        }
        if (!recurso.estaDisponible(fechaUso, horarioInicio, duracionHoras)
                || haySuperposicion(recurso, fechaUso, horarioInicio, duracionHoras)) {
            throw new IllegalStateException("El recurso no está disponible en la fecha y horario solicitados");
        }

        String numeroReserva = String.valueOf(proximoNumeroReserva);
        Reserva reserva = new Reserva(numeroReserva, socio, recurso, fechaUso, horarioInicio, duracionHoras,
                pagoInicial);
        double pagoEsperado = reserva.getImporteTotal() * PORCENTAJE_PAGO_INICIAL;
        if (Math.abs(pagoInicial.getMonto() - pagoEsperado) > TOLERANCIA_IMPORTE) {
            throw new IllegalArgumentException("El pago inicial debe ser el 50% del importe total: " + pagoEsperado);
        }

        proximoNumeroReserva++;
        reservas.put(numeroReserva, reserva);
        return reserva;
    }

    // Registra el pago del saldo restante y confirma la reserva.
    public void confirmarReserva(String numeroReserva, Pago pagoSaldo) {
        Reserva reserva = obtenerReserva(numeroReserva);
        if (!reserva.getEstado().equals(ESTADO_PAGO_PARCIAL)) {
            throw new IllegalStateException("Solo se puede confirmar una reserva en estado de pago parcial");
        }
        reserva.registrarPago(pagoSaldo);
        reserva.confirmar();
    }

    public void cancelarReserva(String numeroReserva) {
        obtenerReserva(numeroReserva).cancelar();
    }

    public List<Reserva> consultarReservasPorSocio(int numeroSocio) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reserva : reservas.values()) {
            if (reserva.getSocio().getNumeroSocio() == numeroSocio) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    public List<Reserva> consultarReservasPorRecursoYFecha(String codigoRecurso, LocalDate fecha) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reserva : reservas.values()) {
            if (reserva.getRecurso().getCodigo().equals(codigoRecurso) && reserva.getFechaUso().equals(fecha)) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    private Reserva obtenerReserva(String numeroReserva) {
        Reserva reserva = reservas.get(numeroReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva " + numeroReserva + " no existe");
        }
        return reserva;
    }

    // Las reservas canceladas liberan el recurso, por lo que no cuentan como superposición.
    private boolean haySuperposicion(Recurso recurso, LocalDate fecha, LocalTime inicio, int horas) {
        LocalDateTime nuevoInicio = fecha.atTime(inicio);
        LocalDateTime nuevoFin = nuevoInicio.plusHours(horas);
        for (Reserva reserva : reservas.values()) {
            if (!reserva.getRecurso().getCodigo().equals(recurso.getCodigo())
                    || reserva.getEstado().equals(ESTADO_CANCELADA)) {
                continue;
            }
            LocalDateTime inicioExistente = reserva.getFechaUso().atTime(reserva.getHorarioInicio());
            LocalDateTime finExistente = inicioExistente.plusHours(reserva.getDuracionHoras());
            if (nuevoInicio.isBefore(finExistente) && inicioExistente.isBefore(nuevoFin)) {
                return true;
            }
        }
        return false;
    }
}
