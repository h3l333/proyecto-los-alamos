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
}
