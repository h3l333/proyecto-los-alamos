package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: PagoBilleteraVirtual
 *
 * Responsabilidades:
 * 1. Guarda los datos propios de un pago con billetera virtual: plataforma,
 *   CVU y alias.
 *
 * Colaboradores:
 * - Pago: Sucede por herencia.
 * ============================================================================
 */
public class PagoBilleteraVirtual extends Pago {
    private final String nombrePlataforma;
    private final String cvu;
    private final String alias;

    public PagoBilleteraVirtual(String idPago, BigDecimal monto, String nombrePlataforma, String cvu, String alias) {
        super(idPago, monto);
        this.nombrePlataforma = nombrePlataforma;
        this.cvu = cvu;
        this.alias = alias;
    }

    @Override
    public String getMedioPago() {
        return "Billetera virtual";
    }

    public String getNombrePlataforma() {
        return nombrePlataforma;
    }

    public String getCvu() {
        return cvu;
    }

    public String getAlias() {
        return alias;
    }
}
