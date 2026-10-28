package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Moroso
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase Moroso
 *   e indica si un Socio moroso puede reservar.
 *
 * Colaboradores:
 * - SituacionAdmin: Sucede por herencia.
 * ============================================================================
 */
public class Moroso extends SituacionAdmin {
    private static final Moroso INSTANCIA = new Moroso();

    private Moroso() {
        super("Moroso");
    }

    public static Moroso getInstancia() {
        return INSTANCIA;
    }

    @Override
    public boolean puedeReservar() {
        return false;
    }
}
