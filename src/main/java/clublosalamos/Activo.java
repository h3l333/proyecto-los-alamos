package clublosalamos;

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
 * - TipoSocio: Sucede por herencia.
 * ============================================================================
 */
public class Activo extends TipoSocio {
    private static final Activo INSTANCIA = new Activo();

    private Activo() {
        super("Activo", 0.0);
    }

    public static Activo getInstancia() {
        return INSTANCIA;
    }
}
