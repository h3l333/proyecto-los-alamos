package clublosalamos;

import java.math.BigDecimal;
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
 *   solicitadas (considerando las reservas ya registradas), validando el
 *   pago inicial y generando el número único de reserva.
 * 3. Notifica al socio los datos de la reserva registrada mediante un email
 *   simulado por consola.
 * 4. Confirma reservas con el pago del saldo pendiente, las cancela y las
 *   finaliza.
 * 5. Consulta reservas por socio y por recurso y fecha, y genera el informe
 *   de reservas de un día.
 * 6. Verifica la situación administrativa de un socio, lo da de baja,
 *   suspende a un socio habilitado y pasa a moroso a un socio suspendido.
 * 7. Configura los porcentajes de descuento o recargo de cada categoría de
 *   socio.
 *
 * Colaboradores:
 * - Recurso: Club los registra y verifica su disponibilidad.
 * - Socio: Club los registra y verifica si pueden reservar.
 * - Reserva: Club las registra, confirma, cancela, finaliza y consulta.
 * - Pago: Club lo recibe para validar su monto y registrar los pagos de una reserva.
 * - EstadoReserva: Club consulta el estado de las reservas.
 * - TipoSocio: Club configura el porcentaje de ajuste de cada categoría.
 * ============================================================================
 */
public class Club {
    private static final BigDecimal PORCENTAJE_PAGO_INICIAL = new BigDecimal("0.5");

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
        validarDuracion(duracionHoras);
        Socio socio = obtenerSocio(numeroSocio);
        Recurso recurso = obtenerRecurso(codigoRecurso);
        BigDecimal importeTotal = socio.getTipoSocio()
                .aplicarAjuste(recurso.getPrecioBaseHora().multiply(BigDecimal.valueOf(duracionHoras)));
        return calcularPagoInicialSobre(importeTotal);
    }

    // Si el 50% cae en medio centavo se redondea. El saldo pendiente absorbe la diferencia.
    private BigDecimal calcularPagoInicialSobre(BigDecimal importeTotal) {
        return importeTotal.multiply(PORCENTAJE_PAGO_INICIAL)
                .setScale(Reserva.ESCALA_IMPORTE, Reserva.REDONDEO_IMPORTE);
    }

    // Registra una reserva a nombre de un socio. El pago inicial debe ser el 50% del importe total.
    public Reserva registrarReserva(int numeroSocio, String codigoRecurso, LocalDate fechaUso,
                                    LocalTime horarioInicio, int duracionHoras, Pago pagoInicial) {
        Socio socio = obtenerSocio(numeroSocio);
        if (!socio.puedeReservar()) {
            throw new IllegalStateException("La situación administrativa del socio no permite reservar");
        }
        Recurso recurso = obtenerRecurso(codigoRecurso);
        validarDuracion(duracionHoras);
        if (fechaUso == null || horarioInicio == null) {
            throw new IllegalArgumentException("La fecha y el horario de uso son obligatorios");
        }
        if (fechaUso.atTime(horarioInicio).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y el horario de uso no pueden estar en el pasado");
        }
        if (!recurso.estaDisponible(fechaUso, horarioInicio, duracionHoras)
                || haySuperposicion(recurso, fechaUso, horarioInicio, duracionHoras)) {
            throw new IllegalStateException("El recurso no está disponible en la fecha y horario solicitados");
        }

        int numeroReserva = proximoNumeroReserva;
        Reserva reserva = new Reserva(numeroReserva, socio, recurso, fechaUso, horarioInicio, duracionHoras,
                pagoInicial);
        BigDecimal pagoEsperado = calcularPagoInicialSobre(reserva.getImporteTotal());
        if (pagoInicial.getMonto().compareTo(pagoEsperado) != 0) {
            throw new IllegalArgumentException("El pago inicial debe ser el 50% del importe total: " + pagoEsperado);
        }

        proximoNumeroReserva++;
        reservas.put(numeroReserva, reserva);
        reservasPorCreacion.add(reserva);
        enviarEmailConfirmacion(reserva);
        return reserva;
    }

    // Registra el pago del saldo restante y confirma la reserva.
    public void confirmarReserva(int numeroReserva, Pago pagoSaldo) {
        Reserva reserva = obtenerReserva(numeroReserva);
        if (reserva.getEstado() != PagoParcial.getInstancia()) {
            throw new IllegalStateException("Solo se puede confirmar una reserva en estado de pago parcial");
        }
        // Se valida que el monto del pago sea exactamente el saldo pendiente.
        if (pagoSaldo.getMonto().compareTo(reserva.getSaldoPendiente()) != 0) {
            throw new IllegalArgumentException("El pago debe ser igual al saldo pendiente: "
                    + reserva.getSaldoPendiente());
        }
        reserva.registrarPago(pagoSaldo);
        reserva.confirmar();
    }

    public void cancelarReserva(int numeroReserva) {
        obtenerReserva(numeroReserva).cancelar();
    }

    // Da por concluida la reserva una vez que llegó la fecha y el horario de uso.
    public void finalizarReserva(int numeroReserva) {
        Reserva reserva = obtenerReserva(numeroReserva);
        if (LocalDateTime.now().isBefore(reserva.getFechaUso().atTime(reserva.getHorarioInicio()))) {
            throw new IllegalStateException("Todavía no llegó la fecha y el horario de uso de la reserva");
        }
        reserva.finalizar();
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

    // Informe de todas las reservas con fecha de uso en el día indicado, en el orden en que fueron creadas.
    public String generarInformeReservasDelDia(LocalDate fecha) {
        List<Reserva> delDia = new ArrayList<>();
        for (Reserva reserva : reservasPorCreacion) {
            if (reserva.getFechaUso().equals(fecha)) {
                delDia.add(reserva);
            }
        }

        StringBuilder informe = new StringBuilder("INFORME DE RESERVAS DEL " + fecha + " (en orden de creación)\n");
        if (delDia.isEmpty()) {
            informe.append("No hay reservas para este día.");
        }
        for (Reserva reserva : delDia) {
            informe.append(reserva.resumen()).append("\n");
        }
        return informe.toString();
    }

    public void darSocioDeBaja(int numeroSocio) {
        obtenerSocio(numeroSocio).setSituacionAdmin(DadoDeBaja.getInstancia());
    }

    // Solo un socio habilitado puede ser suspendido.
    public void suspenderSocio(int numeroSocio) {
        Socio socio = obtenerSocio(numeroSocio);
        if (socio.getSituacionAdministrativa() != Habilitado.getInstancia()) {
            throw new IllegalStateException("Solo un socio habilitado puede ser suspendido");
        }
        socio.setSituacionAdmin(Suspendido.getInstancia());
    }

    // Solo un socio suspendido puede pasar a moroso.
    public void marcarSocioMoroso(int numeroSocio) {
        Socio socio = obtenerSocio(numeroSocio);
        if (socio.getSituacionAdministrativa() != Suspendido.getInstancia()) {
            throw new IllegalStateException("Solo un socio suspendido puede pasar a moroso");
        }
        socio.setSituacionAdmin(Moroso.getInstancia());
    }

    // Los porcentajes se expresan en puntos porcentuales: positivo = recargo, negativo = descuento.
    public void configurarAjusteCategoriaSocioActivo(BigDecimal porcentaje) {
        Activo.getInstancia().setPorcentajeAjuste(porcentaje.movePointLeft(2));
    }

    public void configurarAjusteCategoriaSocioVitalicio(BigDecimal porcentaje) {
        if (porcentaje.signum() > 0) {
            throw new IllegalArgumentException("La categoría Vitalicio solo admite descuento (porcentaje negativo o cero)");
        }
        SocioVitalicio.getInstancia().setPorcentajeAjuste(porcentaje.movePointLeft(2));
    }

    public void configurarAjusteCategoriaSocioAdherente(BigDecimal porcentaje) {
        if (porcentaje.signum() < 0) {
            throw new IllegalArgumentException("La categoría Adherente solo admite recargo (porcentaje positivo o cero)");
        }
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

    private void validarDuracion(int duracionHoras) {
        if (duracionHoras <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
    }

    // Las reservas canceladas liberan el recurso, por lo que no cuentan como superposición.
    private boolean haySuperposicion(Recurso recurso, LocalDate fecha, LocalTime inicio, int horas) {
        LocalDateTime nuevoInicio = fecha.atTime(inicio);
        LocalDateTime nuevoFin = nuevoInicio.plusHours(horas);
        for (Reserva reserva : reservasPorCreacion) {
            if (!reserva.getRecurso().getCodigo().equals(recurso.getCodigo())
                    || reserva.getEstado() == Cancelada.getInstancia()) {
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

    // Simulación: en lugar de enviar un email real, lo imprime por consola.
    private void enviarEmailConfirmacion(Reserva reserva) {
        System.out.println("\n--- EMAIL SIMULADO ---");
        System.out.println("Para: " + reserva.getSocio().getCorreoElectronico());
        System.out.println("Asunto: Reserva #" + reserva.getNumeroReserva() + " registrada");
        System.out.println(reserva.resumen());
        System.out.println("----------------------");
    }
}
