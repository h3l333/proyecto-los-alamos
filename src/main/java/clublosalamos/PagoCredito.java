package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: PagoCredito
 *
 * Responsabilidades:
 * 1. Guarda los datos propios de un pago con tarjeta de crédito: banco
 *   emisor y cantidad de cuotas.
 *
 * Colaboradores:
 * - Pago: Sucede por herencia. Se crea solo mediante los factory methods
 *   de Pago (constructor de visibilidad de paquete).
 * ============================================================================
 */
public class PagoCredito extends Pago {
    private final String bancoEmisor;
    private final int cantCuotas;

    PagoCredito(BigDecimal monto, String bancoEmisor, int cantCuotas) {
        super(monto);
        this.bancoEmisor = bancoEmisor;
        this.cantCuotas = cantCuotas;
    }

    @Override
    public String getMedioPago() {
        return "Tarjeta de crédito";
    }

    public String getBancoEmisor() {
        return bancoEmisor;
    }

    public int getCantCuotas() {
        return cantCuotas;
    }
}
