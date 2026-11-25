package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Confirmada
 *
 * Responsabilidades:
 * 1. Provee acceso a la instancia única en el sistema de la clase Confirmada.
 * 2. Permite pasar a Cancelada o Finalizada.
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
    public EstadoReserva cancelar() {
        return Cancelada.getInstancia();
    }

    @Override
    public EstadoReserva finalizar() {
        return Finalizada.getInstancia();
    }
}
