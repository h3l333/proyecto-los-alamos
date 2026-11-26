package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: PagoFactory
 *
 * Responsabilidades:
 * 1. Crea el pago concreto según el medio de pago elegido.
 * 2. Genera el id único de cada pago.
 *
 * Colaboradores:
 * - Pago: PagoFactory crea sus subtipos.
 * ============================================================================
 */
public class PagoFactory {
    private int proximoIdPago = 1;

    public Pago crearCredito(BigDecimal monto, String banco, int cuotas) {
        return new PagoCredito(siguienteId(), monto, banco, cuotas);
    }

    public Pago crearDebito(BigDecimal monto, String banco, String cbu, String titular) {
        return new PagoDebito(siguienteId(), monto, banco, cbu, titular);
    }

    public Pago crearBilleteraVirtual(BigDecimal monto, String plataforma, String cvu, String alias) {
        return new PagoBilleteraVirtual(siguienteId(), monto, plataforma, cvu, alias);
    }

    private String siguienteId() {
        return String.valueOf(proximoIdPago++);
    }
}
