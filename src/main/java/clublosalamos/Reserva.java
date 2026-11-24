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
 *
 * Colaboradores:
 * - Socio: Es el socio solicitante de la reserva.
 * - Recurso: Es el recurso reservado y provee su precio base por hora.
 * - Pago: Reserva registra los pagos realizados.
 * - EstadoReserva: Reserva lo conoce como su estado y lo consulta para saber
 *   si puede cancelarse.
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
        this.numeroReserva = numeroReserva;
        this.fechaCreacion = LocalDateTime.now();
        this.socio = socio;
        this.recurso = recurso;
        this.fechaUso = fechaUso;
        this.horarioInicio = horarioInicio;
        this.duracionHoras = duracionHoras;
        this.importeTotal = calcularImporteTotal(socio, recurso, duracionHoras);
        this.estado = PagoParcial.getInstancia();
        registrarPago(pagoInicial);
    }

    static BigDecimal calcularImporteTotal(Socio socio, Recurso recurso, int duracionHoras) {
        return socio.getTipoSocio().aplicarAjuste(recurso.getPrecioBaseHora().multiply(BigDecimal.valueOf(duracionHoras)));
    }

    // Si el 50% cae en medio centavo se redondea. El saldo pendiente absorbe la diferencia.
    static BigDecimal calcularPagoInicial(BigDecimal importeTotal) {
        return importeTotal.multiply(PORCENTAJE_PAGO_INICIAL).setScale(ESCALA_IMPORTE, REDONDEO_IMPORTE);
    }

    public void registrarPago(Pago pago) {
        pagos.add(pago);
        importeAbonado = importeAbonado.add(pago.getMonto());
        saldoPendiente = importeTotal.subtract(importeAbonado);
    }

    public void confirmar() {
        if (estado != PagoParcial.getInstancia()) {
            throw new IllegalStateException("No se puede confirmar una reserva en estado " + estado.getNombre());
        }
        if (saldoPendiente.signum() > 0) {
            throw new IllegalStateException("No se puede confirmar: queda un saldo pendiente de " + saldoPendiente);
        }
        cambiarEstado(Confirmada.getInstancia());
    }

    public void cancelar() {
        if (!estado.puedeCancelarse()) {
            throw new IllegalStateException("No se puede cancelar una reserva en estado " + estado.getNombre());
        }
        cambiarEstado(Cancelada.getInstancia());
    }

    public void finalizar() {
        if (estado != Confirmada.getInstancia()) {
            throw new IllegalStateException("No se puede finalizar una reserva en estado " + estado.getNombre());
        }
        cambiarEstado(Finalizada.getInstancia());
    }

    public void cambiarEstado(EstadoReserva nuevo) {
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
