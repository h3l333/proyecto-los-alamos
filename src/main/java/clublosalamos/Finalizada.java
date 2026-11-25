package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Finalizada
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase Finalizada.
 * 2. No admite transiciones ni pagos: usa el comportamiento por defecto
 *   de EstadoReserva.
 *
 * Colaboradores:
 * - EstadoReserva: Sucede por herencia.
 * ============================================================================
 */
public class Finalizada extends EstadoReserva {
    private static final Finalizada INSTANCIA = new Finalizada();

    private Finalizada() {
        super("Finalizada / Utilizada");
    }

    public static Finalizada getInstancia() {
        return INSTANCIA;
    }
}
