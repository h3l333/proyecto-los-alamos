package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Cancelada
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase Cancelada.
 * 2. No admite transiciones ni pagos: usa el comportamiento por defecto
 *   de EstadoReserva.
 * 3. Indica que una reserva cancelada no ocupa el recurso.
 *
 * Colaboradores:
 * - EstadoReserva: Sucede por herencia.
 * ============================================================================
 */
public class Cancelada extends EstadoReserva {
    private static final Cancelada INSTANCIA = new Cancelada();

    private Cancelada() {
        super("Cancelada");
    }

    public static Cancelada getInstancia() {
        return INSTANCIA;
    }

    // Una reserva cancelada libera el recurso.
    @Override
    public boolean ocupaRecurso() {
        return false;
    }
}
