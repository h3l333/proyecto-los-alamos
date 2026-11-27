package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Activo
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase Activo,
 *   categoría de socio que abona la tarifa estándar.
 *
 * Colaboradores:
 * - Categoria: Sucede por herencia.
 * ============================================================================
 */
public class Activo extends Categoria {
    private static final Activo INSTANCIA = new Activo();

    private Activo() {
        super("Activo", BigDecimal.ZERO);
    }

    public static Activo getInstancia() {
        return INSTANCIA;
    }
}
