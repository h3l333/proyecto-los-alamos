package clublosalamos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Pago
 *
 * Responsabilidades:
 * 1. Guarda los datos de un pago: monto y fecha y hora.
 * 2. Declara el nombre del medio de pago, que implementa cada medio de
 *   pago concreto.
 *
 * Colaboradores:
 * - Ninguno.
 * ============================================================================
 */
public abstract class Pago {
    private final String idPago;
    private final BigDecimal monto;
    private final LocalDateTime fechaHora;

    protected Pago(String idPago, BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        this.idPago = idPago;
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
    }

    public abstract String getMedioPago();

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getIdPago() {
        return idPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }
    
}
