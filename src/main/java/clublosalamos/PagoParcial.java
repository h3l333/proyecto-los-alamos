package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: PagoParcial
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase PagoParcial
 *   y admite pagos.
 * 2. Permite pasar a Confirmada o Cancelada.
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
    public EstadoReserva confirmar() {
        return Confirmada.getInstancia();
    }

    @Override
    public EstadoReserva cancelar() {
        return Cancelada.getInstancia();
    }

    @Override
    public void validarPago() {
        // Una reserva con pago parcial admite pagos.
    }
}
