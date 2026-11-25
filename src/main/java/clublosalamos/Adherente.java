package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Adherente
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase Adherente,
 *   categoría de socio que tiene un recargo en las reservas.
 * 2. Solo admite recargo (porcentaje positivo o cero).
 *
 * Colaboradores:
 * - TipoSocio: Sucede por herencia.
 * ============================================================================
 */
public class Adherente extends TipoSocio {
    private static final Adherente INSTANCIA = new Adherente();

    private Adherente() {
        super("Adherente", new BigDecimal("0.2"));
    }

    public static Adherente getInstancia() {
        return INSTANCIA;
    }

    @Override
    protected void validarAjuste(BigDecimal p) {
        if (p.signum() < 0) {
            throw new IllegalArgumentException("La categoría Adherente solo admite recargo (porcentaje positivo o cero)");
        }
    }
}
