package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Habilitado
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase Habilitado
 *   e indica si un Socio habilitado puede reservar.
 *
 * Colaboradores:
 * - SituacionAdmin: Sucede por herencia.
 * ============================================================================
 */
public class Habilitado extends SituacionAdmin {
    private static final Habilitado INSTANCIA = new Habilitado();

    private Habilitado() {
        super("Habilitado");
    }

    public static Habilitado getInstancia() {
        return INSTANCIA;
    }

    @Override
    public boolean puedeReservar() {
        return true;
    }
}
