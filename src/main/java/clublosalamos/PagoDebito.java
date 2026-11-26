package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: PagoDebito
 *
 * Responsabilidades:
 * 1. Guarda los datos propios de un pago con tarjeta de débito: banco
 *   emisor, CBU y titular de la cuenta.
 *
 * Colaboradores:
 * - Pago: Sucede por herencia. Se crea solo mediante los factory methods
 *   de Pago (constructor de visibilidad de paquete).
 * ============================================================================
 */
public class PagoDebito extends Pago {
    private final String bancoEmisor;
    private final String cbu;
    private final String titularCuenta;

    PagoDebito(BigDecimal monto, String bancoEmisor, String cbu, String titularCuenta) {
        super(monto);
        this.bancoEmisor = bancoEmisor;
        this.cbu = cbu;
        this.titularCuenta = titularCuenta;
    }

    @Override
    public String getMedioPago() {
        return "Tarjeta de débito";
    }

    public String getBancoEmisor() {
        return bancoEmisor;
    }

    public String getCbu() {
        return cbu;
    }

    public String getTitularCuenta() {
        return titularCuenta;
    }
}
