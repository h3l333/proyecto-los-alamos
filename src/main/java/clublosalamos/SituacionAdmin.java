package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: SituacionAdmin
 *
 * Responsabilidades:
 * 1. Guarda la descripción de una situación administrativa de un Socio.
 * 2. Declara si la situación administrativa permite reservar, dejando su
 *   implementación a cada situación concreta.
 * 3. Define las transiciones entre situaciones administrativas. Por defecto
 *   toda transición es inválida; cada situación concreta sobrescribe las que
 *   permite y devuelve la situación siguiente (patrón State).
 *
 * Colaboradores:
 * - Socio: Socio la consulta para saber si puede reservar.
 * ============================================================================
 */
public abstract class SituacionAdmin {
    private final String descSituacionAdmin;
    
    protected SituacionAdmin(String descSituacionAdmin) {
        this.descSituacionAdmin = descSituacionAdmin;
    }

    public String getSituacionAdmin() {
        return descSituacionAdmin;
    }

    public abstract boolean puedeReservar();

    public SituacionAdmin suspender() {
        throw new IllegalStateException("Solo un socio habilitado puede ser suspendido");
    }

    public SituacionAdmin marcarMoroso() {
        throw new IllegalStateException("Solo un socio suspendido puede pasar a moroso");
    }

    public SituacionAdmin habilitar() {
        throw new IllegalStateException("Solo un socio suspendido o moroso puede ser habilitado");
    }
}
