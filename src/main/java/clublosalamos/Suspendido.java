package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Suspendido
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase Suspendido
 *   e indica si un Socio suspendido puede reservar.
 * 2. Permite pasar a Moroso o volver a Habilitado.
 *
 * Colaboradores:
 * - SituacionAdmin: Sucede por herencia.
 * ============================================================================
 */
public class Suspendido extends SituacionAdmin {
    private static final Suspendido INSTANCIA = new Suspendido();

    private Suspendido() {
        super("Suspendido");
    }

    public static Suspendido getInstancia() {
        return INSTANCIA;
    }

    @Override
    public boolean puedeReservar() {
        return false;
    }

    @Override
    public SituacionAdmin marcarMoroso() {
        return Moroso.getInstancia();
    }

    @Override
    public SituacionAdmin habilitar() {
        return Habilitado.getInstancia();
    }
}
