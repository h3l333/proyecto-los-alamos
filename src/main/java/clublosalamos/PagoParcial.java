package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: PagoParcial
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase PagoParcial
 *   e indica si una Reserva con pago parcial puede cancelarse.
 *
 * Colaboradores:
 * - EstadoReserva: Sucede por herencia.
 * ============================================================================
 */
public class PagoParcial extends EstadoReserva {
    private static final PagoParcial INSTANCIA = new PagoParcial();

    private PagoParcial() {
        super("Pago parcial");
    }

    public static PagoParcial getInstancia() {
        return INSTANCIA;
    }

    @Override
    public boolean puedeCancelarse() {
        return true;
    }
}
