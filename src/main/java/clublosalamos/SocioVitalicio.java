package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: SocioVitalicio
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase
 *   SocioVitalicio, categoría de socio que tiene un descuento en las
 *   reservas.
 *
 * Colaboradores:
 * - TipoSocio: Sucede por herencia.
 * ============================================================================
 */
public class SocioVitalicio extends TipoSocio {
    private static final SocioVitalicio INSTANCIA = new SocioVitalicio();

    private SocioVitalicio() {
        super("Vitalicio", new BigDecimal("-0.5"));
    }

    public static SocioVitalicio getInstancia() {
        return INSTANCIA;
    }
}
