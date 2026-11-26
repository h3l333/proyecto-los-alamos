package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: DadoDeBaja
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase DadoDeBaja
 *   e indica si un Socio dado de baja puede reservar.
 * 2. Rechaza una nueva baja: no admite ninguna transición.
 *
 * Colaboradores:
 * - SituacionAdmin: Sucede por herencia.
 * ============================================================================
 */
public class DadoDeBaja extends SituacionAdmin {
    private static final DadoDeBaja INSTANCIA = new DadoDeBaja();

    private DadoDeBaja() {
        super("Dado de baja");
    }

    public static DadoDeBaja getInstancia() {
        return INSTANCIA;
    }

    @Override
    public boolean puedeReservar() {
        return false;
    }

    @Override
    public SituacionAdmin darDeBaja() {
        throw new IllegalStateException("El socio ya está dado de baja");
    }
}
