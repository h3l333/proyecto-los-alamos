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
 *
 * Colaboradores:
 * - Socio: Socio la consulta para saber si puede reservar.
 * ============================================================================
 */
public abstract class SituacionAdmin {
    private final String descSituacionAdmin;
    
    SituacionAdmin(String descSituacionAdmin) {
        this.descSituacionAdmin = descSituacionAdmin;
    }

    public String getSituacionAdmin() {
        return descSituacionAdmin;
    }

    public abstract boolean puedeReservar();
}
