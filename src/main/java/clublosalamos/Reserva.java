package clublosalamos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Reserva
 *
 * Responsabilidades:
 * 1. Guarda los datos de una reserva de un recurso realizada por un socio.
 * 2. Genera el número único de reserva con una variable de clase.
 * 3. Calcula el importe total y registra los pagos, actualizando el
 *   importe abonado y el saldo pendiente.
 * 4. Gestiona el estado de la reserva: la confirma, cancela y finaliza,
 *   rechazando los cambios de estado inválidos.
 * 5. Provee un resumen de sus datos y los medios de pago utilizados.
 * 6. Valida sus propios datos al crearse: duración, fecha y hora de uso,
 *   disponibilidad del recurso y que el pago inicial sea el 50% del importe
 *   total.
 * 7. Indica si ocupa el recurso en una franja horaria dada.
 *
 * Colaboradores:
 * - Socio: Es el socio solicitante de la reserva.
 * - Recurso: Es el recurso reservado; provee su precio base por hora y su
 *   disponibilidad.
 * - Pago: Reserva registra los pagos realizados.
 * - EstadoReserva: Reserva lo conoce como su estado y le delega las
 *   transiciones, la validación de pagos y si ocupa el recurso.
 * - Categoria: Reserva le pide aplicar el ajuste de la categoría del socio.
 * - Importe: Reserva redondea el pago inicial a centavos.
 * ============================================================================
 */
public class Reserva {
    // Variable de clase: un único contador compartido por todas las reservas del sistema.
    private static int proximoNumero = 1;
    private static final BigDecimal PORCENTAJE_PAGO_INICIAL = new BigDecimal("0.5");

    private final int numeroReserva;
    private final LocalDateTime fechaCreacion;
    private final Socio socio;
    private final Recurso recurso;
    private final LocalDate fechaUso;
    private final int horaInicio;
    private final int duracionHoras;
    private final BigDecimal importeTotal;
    private final ArrayList<Pago> pagos;
    private BigDecimal importeAbonado;
    private BigDecimal saldoPendiente;
    private EstadoReserva estado;

    // Todo se valida antes de asignar el número: si algo falla, el número no se consume.
    public Reserva(Socio socio, Recurso recurso, LocalDate fechaUso, int horaInicio, int duracionHoras,
                   Pago pagoInicial) {
        validarSolicitud(recurso, fechaUso, horaInicio, duracionHoras);
        if (pagoInicial == null) {
            throw new IllegalArgumentException("El pago inicial es obligatorio");
        }
        BigDecimal total = calcularImporteTotal(socio, recurso, duracionHoras);
        BigDecimal pagoEsperado = calcularPagoInicial(total);
        if (pagoInicial.getMonto().compareTo(pagoEsperado) != 0) {
            throw new IllegalArgumentException("El pago inicial debe ser el 50% del importe total: " + pagoEsperado);
        }
        this.numeroReserva = proximoNumero++;
        this.fechaCreacion = LocalDateTime.now();
        this.socio = socio;
        this.recurso = recurso;
        this.fechaUso = fechaUso;
        this.horaInicio = horaInicio;
        this.duracionHoras = duracionHoras;
        this.importeTotal = total;
        this.pagos = new ArrayList<Pago>();
        this.importeAbonado = BigDecimal.ZERO;
        this.saldoPendiente = total;
        this.estado = PagoParcial.getInstancia();
        registrarPago(pagoInicial);
    }

    // Importe del pago inicial (50% del total) que corresponde a una reserva con estos datos.
    // Supone una solicitud ya validada con validarSolicitud.
    public static BigDecimal calcularPagoInicial(Socio socio, Recurso recurso, int duracionHoras) {
        return calcularPagoInicial(calcularImporteTotal(socio, recurso, duracionHoras));
    }

    // Verifica que una reserva con estos datos se pueda realizar. La usa el constructor y también Club,
    // para rechazar la solicitud antes de que el socio cargue el pago.
    public static void validarSolicitud(Recurso recurso, LocalDate fechaUso, int horaInicio, int duracionHoras) {
        if (duracionHoras <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
        if (fechaUso == null) {
            throw new IllegalArgumentException("La fecha de uso es obligatoria");
        }
        if (horaInicio < 0 || horaInicio > 23) {
            throw new IllegalArgumentException("La hora de inicio debe estar entre 0 y 23");
        }
        if (horaInicio + duracionHoras > 24) {
            throw new IllegalArgumentException("La reserva debe terminar dentro del mismo día");
        }
        if (fechaUso.atTime(horaInicio, 0).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y la hora de uso no pueden estar en el pasado");
        }
        if (!recurso.estaDisponible(fechaUso, horaInicio, duracionHoras)) {
            throw new IllegalStateException("El recurso no está disponible en la fecha y horario solicitados");
        }
    }

    private static BigDecimal calcularImporteTotal(Socio socio, Recurso recurso, int duracionHoras) {
        return socio.getCategoria().aplicarAjuste(recurso.getPrecioBaseHora().multiply(BigDecimal.valueOf(duracionHoras)));
    }

    // Si el 50% cae en medio centavo se redondea. El saldo pendiente absorbe la diferencia.
    private static BigDecimal calcularPagoInicial(BigDecimal importeTotal) {
        return Importe.redondear(importeTotal.multiply(PORCENTAJE_PAGO_INICIAL));
    }

    // Solo se admiten pagos si el estado lo permite y el monto no supera el saldo pendiente.
    private void registrarPago(Pago pago) {
        estado.validarPago();
        if (pago.getMonto().compareTo(saldoPendiente) > 0) {
            throw new IllegalArgumentException("El pago supera el saldo pendiente: " + saldoPendiente);
        }
        pagos.add(pago);
        importeAbonado = importeAbonado.add(pago.getMonto());
        saldoPendiente = importeTotal.subtract(importeAbonado);
    }

    // Confirma la reserva con el pago del saldo. Todo se valida antes de modificar la reserva:
    // si algo falla, el pago no queda registrado.
    public void confirmar(Pago pagoSaldo) {
        EstadoReserva siguiente = estado.confirmar();
        if (pagoSaldo.getMonto().compareTo(saldoPendiente) != 0) {
            throw new IllegalArgumentException("El pago debe ser igual al saldo pendiente: " + saldoPendiente);
        }
        registrarPago(pagoSaldo);
        estado = siguiente;
    }

    public void cancelar() {
        estado = estado.cancelar();
    }

    // Solo puede finalizarse una vez que llegó la fecha y la hora de uso.
    public void finalizar() {
        EstadoReserva siguiente = estado.finalizar();
        if (LocalDateTime.now().isBefore(fechaUso.atTime(horaInicio, 0))) {
            throw new IllegalStateException("Todavía no llegó la fecha y la hora de uso de la reserva");
        }
        estado = siguiente;
    }

    // Ocupa la franja si su estado ocupa el recurso y los horarios se pisan.
    public boolean ocupaFranja(LocalDate fecha, int hora, int horas) {
        if (!estado.ocupaRecurso()) {
            return false;
        }
        LocalDateTime inicio = fechaUso.atTime(horaInicio, 0);
        LocalDateTime fin = inicio.plusHours(duracionHoras);
        LocalDateTime otroInicio = fecha.atTime(hora, 0);
        LocalDateTime otroFin = otroInicio.plusHours(horas);
        return inicio.isBefore(otroFin) && otroInicio.isBefore(fin);
    }

    public boolean esDelSocio(int numeroSocio) {
        return socio.getNumeroSocio() == numeroSocio;
    }

    public boolean esDelRecurso(String codigoRecurso) {
        return recurso.getCodigo().equals(codigoRecurso);
    }

    public boolean esDelDia(LocalDate fecha) {
        return fechaUso.equals(fecha);
    }

    public String getCorreoSocio() {
        return socio.getCorreoElectronico();
    }

    // Lista los medios de todos los pagos, porque el saldo puede abonarse con otro medio.
    private String describirMediosPago() {
        String medios = "";
        for (Pago pago : pagos) {
            if (!medios.isEmpty()) {
                medios = medios + ", ";
            }
            medios = medios + pago.getMedioPago();
        }
        return medios;
    }

    public String resumen() {
        return String.format("Reserva #%d | Recurso: %s (%s) | Socio: %d - %s %s | Fecha de uso: %s %02d:00 (%d h) | "
                        + "Total: $%.2f | Abonado: $%.2f | Saldo: $%.2f | Medios de pago: %s | Estado: %s",
                numeroReserva, recurso.getCodigo(), recurso.devolverTipo(), socio.getNumeroSocio(),
                socio.getNombre(), socio.getApellido(), fechaUso, horaInicio, duracionHoras,
                importeTotal, importeAbonado, saldoPendiente, describirMediosPago(), estado.getNombre());
    }

    public int getNumeroReserva() {
        return numeroReserva;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Socio getSocio() {
        return socio;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public LocalDate getFechaUso() {
        return fechaUso;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public int getDuracionHoras() {
        return duracionHoras;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public BigDecimal getImporteAbonado() {
        return importeAbonado;
    }

    public BigDecimal getSaldoPendiente() {
        return saldoPendiente;
    }

    public EstadoReserva getEstado() {
        return estado;
    }
}
