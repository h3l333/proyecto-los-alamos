package clublosalamos;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Reserva
 *
 * Responsabilidades:
 * 1. Guarda los datos de una reserva de un recurso realizada por un socio.
 * 2. Calcula el importe total y registra los pagos, actualizando el
 *   importe abonado y el saldo pendiente.
 * 3. Gestiona el estado de la reserva: la confirma, cancela y finaliza,
 *   rechazando los cambios de estado inválidos.
 * 4. Provee un resumen de sus datos y el medio de pago utilizado.
 * 5. Valida sus propios datos al crearse: duración, fecha y horario de uso
 *   y que el pago inicial sea el 50% del importe total.
 * 6. Indica si se superpone con otra reserva del mismo recurso.
 *
 * Colaboradores:
 * - Socio: Es el socio solicitante de la reserva.
 * - Recurso: Es el recurso reservado y provee su precio base por hora.
 * - Pago: Reserva registra los pagos realizados.
 * - EstadoReserva: Reserva lo conoce como su estado y le delega las
 *   transiciones y la validación de pagos.
 * ============================================================================
 */
public class Reserva {
    private final int numeroReserva;
    private final LocalDateTime fechaCreacion;
    private final Socio socio;
    private final Recurso recurso;
    private final LocalDate fechaUso;
    private final LocalTime horarioInicio;
    private final int duracionHoras;
    private final BigDecimal importeTotal;
    private final List<Pago> pagos = new ArrayList<>();
    private BigDecimal importeAbonado = BigDecimal.ZERO;
    private BigDecimal saldoPendiente;
    private EstadoReserva estado;

    // Los importes se manejan en centavos: todo cálculo que genere decimales se redondea con esta regla.
    static final int ESCALA_IMPORTE = 2;
    static final RoundingMode REDONDEO_IMPORTE = RoundingMode.HALF_UP;
    private static final BigDecimal PORCENTAJE_PAGO_INICIAL = new BigDecimal("0.5");

    public Reserva(int numeroReserva, Socio socio, Recurso recurso, LocalDate fechaUso, LocalTime horarioInicio,
                    int duracionHoras, Pago pagoInicial) {
        validarDuracion(duracionHoras);
        if (fechaUso == null || horarioInicio == null) {
            throw new IllegalArgumentException("La fecha y el horario de uso son obligatorios");
        }
        if (fechaUso.atTime(horarioInicio).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y el horario de uso no pueden estar en el pasado");
        }
        this.numeroReserva = numeroReserva;
        this.fechaCreacion = LocalDateTime.now();
        this.socio = socio;
        this.recurso = recurso;
        this.fechaUso = fechaUso;
        this.horarioInicio = horarioInicio;
        this.duracionHoras = duracionHoras;
        this.importeTotal = calcularImporteTotal(socio, recurso, duracionHoras);
        BigDecimal pagoEsperado = calcularPagoInicial(importeTotal);
        if (pagoInicial.getMonto().compareTo(pagoEsperado) != 0) {
            throw new IllegalArgumentException("El pago inicial debe ser el 50% del importe total: " + pagoEsperado);
        }
        this.saldoPendiente = importeTotal;
        this.estado = PagoParcial.getInstancia();
        registrarPago(pagoInicial);
    }

    // Importe del pago inicial (50% del total) que corresponde a una reserva con estos datos.
    static BigDecimal calcularPagoInicial(Socio socio, Recurso recurso, int duracionHoras) {
        validarDuracion(duracionHoras);
        return calcularPagoInicial(calcularImporteTotal(socio, recurso, duracionHoras));
    }

    private static void validarDuracion(int duracionHoras) {
        if (duracionHoras <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
    }

    private static BigDecimal calcularImporteTotal(Socio socio, Recurso recurso, int duracionHoras) {
        return socio.getTipoSocio().aplicarAjuste(recurso.getPrecioBaseHora().multiply(BigDecimal.valueOf(duracionHoras)));
    }

    // Si el 50% cae en medio centavo se redondea. El saldo pendiente absorbe la diferencia.
    private static BigDecimal calcularPagoInicial(BigDecimal importeTotal) {
        return importeTotal.multiply(PORCENTAJE_PAGO_INICIAL).setScale(ESCALA_IMPORTE, REDONDEO_IMPORTE);
    }

    // Solo se admiten pagos si el estado lo permite y el monto no supera el saldo pendiente.
    public void registrarPago(Pago pago) {
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
        cambiarEstado(siguiente);
    }

    public void cancelar() {
        cambiarEstado(estado.cancelar());
    }

    // Solo puede finalizarse una vez que llegó la fecha y el horario de uso.
    public void finalizar() {
        EstadoReserva siguiente = estado.finalizar();
        if (LocalDateTime.now().isBefore(fechaUso.atTime(horarioInicio))) {
            throw new IllegalStateException("Todavía no llegó la fecha y el horario de uso de la reserva");
        }
        cambiarEstado(siguiente);
    }

    // Dos reservas se superponen si son del mismo recurso y sus franjas horarias se pisan.
    public boolean seSuperponeCon(Reserva otra) {
        if (!recurso.getCodigo().equals(otra.recurso.getCodigo())) {
            return false;
        }
        LocalDateTime inicio = fechaUso.atTime(horarioInicio);
        LocalDateTime fin = inicio.plusHours(duracionHoras);
        LocalDateTime otroInicio = otra.fechaUso.atTime(otra.horarioInicio);
        LocalDateTime otroFin = otroInicio.plusHours(otra.duracionHoras);
        return inicio.isBefore(otroFin) && otroInicio.isBefore(fin);
    }

    private void cambiarEstado(EstadoReserva nuevo) {
        this.estado = nuevo;
    }

    // El medio de pago de la reserva es el del pago inicial, que siempre existe.
    public String getMedioPago() {
        return pagos.get(0).getMedioPago();
    }

    public String resumen() {
        return String.format("Reserva #%d | Recurso: %s (%s) | Socio: %d - %s %s | Fecha de uso: %s %s (%d h) | "
                        + "Total: $%.2f | Abonado: $%.2f | Saldo: $%.2f | Medio de pago: %s | Estado: %s",
                numeroReserva, recurso.getCodigo(), recurso.devolverTipo(), socio.getNumeroSocio(),
                socio.getNombre(), socio.getApellido(), fechaUso, horarioInicio, duracionHoras,
                importeTotal, importeAbonado, saldoPendiente, getMedioPago(), estado.getNombre());
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

    public LocalTime getHorarioInicio() {
        return horarioInicio;
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
