package clublosalamos;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Pago
 *
 * Responsabilidades:
 * 1. Guarda los datos de un pago: monto, fecha y hora, código de
 *   autorización y estado.
 * 2. Declara el procesamiento del pago, que implementa cada medio de pago
 *   concreto.
 *
 * Colaboradores:
 * - Ninguno.
 * ============================================================================
 */
public abstract class Pago {
    private final String idPago;
    private final double monto;
    private final LocalDateTime fechaHora;
    private String codigoAutorizacion;
    private String estado;

    protected Pago(String idPago, double monto) {
        this.idPago = idPago;
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
        this.estado = "pendiente";
    }

    public abstract boolean procesarPago();

    protected void setEstado(String estado) {
        this.estado = estado;
    }

    protected void setCodigoAutorizacion(String codigoAutorizacion) {
        this.codigoAutorizacion = codigoAutorizacion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getIdPago() {
        return idPago;
    }

    public double getMonto() {
        return monto;
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    public String getEstado() {
        return estado;
    }
}
