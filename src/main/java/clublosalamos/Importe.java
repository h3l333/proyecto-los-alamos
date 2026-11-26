package clublosalamos;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Importe
 *
 * Responsabilidades:
 * 1. Clase utilitaria: redondea los importes a centavos con una única regla
 *   para todo el sistema. No se instancia.
 *
 * Colaboradores:
 * - Ninguno.
 * ============================================================================
 */
public class Importe {
    private static final int ESCALA = 2;
    private static final RoundingMode REDONDEO = RoundingMode.HALF_UP;

    // Constructor privado: es una clase utilitaria y solo tiene métodos de clase.
    private Importe() {
    }

    public static BigDecimal redondear(BigDecimal importe) {
        return importe.setScale(ESCALA, REDONDEO);
    }
}
