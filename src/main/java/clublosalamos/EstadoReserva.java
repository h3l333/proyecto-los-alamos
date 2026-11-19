package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: EstadoReserva
 *
 * Responsabilidades:
 * 1. Guarda el nombre de un estado de una Reserva.
 * 2. Declara si una reserva en ese estado puede cancelarse, dejando su
 *   implementación a cada estado concreto.
 *
 * Colaboradores:
 * - Reserva: Reserva lo consulta para conocer su estado y saber si puede
 *   cancelarse.
 * ============================================================================
 */
public abstract class EstadoReserva {
    private final String nombre;

    protected EstadoReserva(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract boolean puedeCancelarse();
}
