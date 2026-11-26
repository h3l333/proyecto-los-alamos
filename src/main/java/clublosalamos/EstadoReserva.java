package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: EstadoReserva
 *
 * Responsabilidades:
 * 1. Guarda el nombre de un estado de una Reserva.
 * 2. Define las transiciones entre estados (confirmar, cancelar, finalizar)
 *   y si se admiten pagos. Por defecto toda operación es inválida; cada
 *   estado concreto sobrescribe las que permite y devuelve el estado
 *   siguiente (patrón State).
 * 3. Indica si una reserva en este estado ocupa el recurso. Por defecto sí.
 *
 * Colaboradores:
 * - Reserva: Reserva le delega las transiciones de estado y la validación
 *   de pagos.
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

    public EstadoReserva confirmar() {
        throw new IllegalStateException("No se puede confirmar una reserva en estado " + nombre);
    }

    public EstadoReserva cancelar() {
        throw new IllegalStateException("No se puede cancelar una reserva en estado " + nombre);
    }

    public EstadoReserva finalizar() {
        throw new IllegalStateException("No se puede finalizar una reserva en estado " + nombre);
    }

    public void validarPago() {
        throw new IllegalStateException("No se pueden registrar pagos en una reserva en estado " + nombre);
    }

    // Por defecto una reserva ocupa el recurso en su franja horaria; los estados que lo liberan lo redefinen.
    public boolean ocupaRecurso() {
        return true;
    }
}
