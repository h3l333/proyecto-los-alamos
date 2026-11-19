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
 * - Pago: Sucede por herencia.
 * ============================================================================
 */
public class PagoDebito extends Pago {
    private final String bancoEmisor;
    private final String cbu;
    private final String titularCuenta;

    public PagoDebito(String idPago, BigDecimal monto, String bancoEmisor, String cbu, String titularCuenta) {
        super(idPago, monto);
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
