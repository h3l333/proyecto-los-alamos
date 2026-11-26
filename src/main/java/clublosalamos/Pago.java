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
 * 1. Guarda los datos de un pago: id, monto y fecha y hora.
 * 2. Genera el id único de cada pago con una variable de clase.
 * 3. Crea los pagos de cada medio mediante factory methods.
 * 4. Declara el nombre del medio de pago, que implementa cada medio de
 *   pago concreto.
 *
 * Colaboradores:
 * - PagoCredito, PagoDebito, PagoBilleteraVirtual: Pago los crea en sus
 *   factory methods.
 * ============================================================================
 */
public abstract class Pago {
    // Variable de clase: un único contador compartido por todos los pagos del sistema.
    private static int proximoId = 1;

    private final int idPago;
    private final BigDecimal monto;
    private final LocalDateTime fechaHora;

    protected Pago(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        this.idPago = proximoId++;
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
    }

    // Factory methods: encapsulan la creación de cada medio de pago.
    // Las cuotas se validan antes de crear el pago para no consumir un id con datos inválidos.
    public static Pago crearCredito(BigDecimal monto, String bancoEmisor, int cantCuotas) {
        if (cantCuotas <= 0) {
            throw new IllegalArgumentException("La cantidad de cuotas debe ser mayor a cero");
        }
        return new PagoCredito(monto, bancoEmisor, cantCuotas);
    }

    public static Pago crearDebito(BigDecimal monto, String bancoEmisor, String cbu, String titularCuenta) {
        return new PagoDebito(monto, bancoEmisor, cbu, titularCuenta);
    }

    public static Pago crearBilleteraVirtual(BigDecimal monto, String nombrePlataforma, String cvu, String alias) {
        return new PagoBilleteraVirtual(monto, nombrePlataforma, cvu, alias);
    }

    public abstract String getMedioPago();

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public int getIdPago() {
        return idPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }
}
