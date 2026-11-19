package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Confirmada
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase Confirmada
 *   e indica si una Reserva confirmada puede cancelarse.
 *
 * Colaboradores:
 * - EstadoReserva: Sucede por herencia.
 * ============================================================================
 */
public class Confirmada extends EstadoReserva {
    private static final Confirmada INSTANCIA = new Confirmada();

    private Confirmada() {
        super("Confirmada");
    }

    public static Confirmada getInstancia() {
        return INSTANCIA;
    }

    @Override
    public boolean puedeCancelarse() {
        return true;
    }
}
