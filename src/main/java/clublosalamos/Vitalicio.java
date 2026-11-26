package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Vitalicio
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase
 *   Vitalicio, categoría de socio que tiene un descuento en las
 *   reservas.
 * 2. Solo admite descuento (porcentaje negativo o cero).
 *
 * Colaboradores:
 * - TipoSocio: Sucede por herencia.
 * ============================================================================
 */
public class Vitalicio extends TipoSocio {
    private static final Vitalicio INSTANCIA = new Vitalicio();

    private Vitalicio() {
        super("Vitalicio", new BigDecimal("-0.5"));
    }

    public static Vitalicio getInstancia() {
        return INSTANCIA;
    }

    @Override
    protected void validarAjuste(BigDecimal p) {
        if (p.signum() > 0) {
            throw new IllegalArgumentException("La categoría Vitalicio solo admite descuento (porcentaje negativo o cero)");
        }
    }
}
