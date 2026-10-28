package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Salon
 *
 * Responsabilidades:
 * 1. Retorna los datos asociados a un recurso de tipo `Salon`.
 *
 * Colaboradores:
 * - Recurso: Sucede por herencia.
 * ============================================================================
 */
public class Salon extends Recurso {
    private final boolean tieneAireAcondicionado;
    private final boolean poseeEscenario;

    public Salon(String codigo, String descripcion, int capacidadMaxima, double precioBaseHora, boolean habilitado,
                boolean tieneAireAcondicionado, boolean poseeEscenario) {
        super(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado);
        this.tieneAireAcondicionado = tieneAireAcondicionado;
        this.poseeEscenario = poseeEscenario;
    }

    @Override
    public String devolverTipo() {
        return "Salon";
    }

    public boolean isTieneAireAcondicionado() {
        return tieneAireAcondicionado;
    }

    public boolean isPoseeEscenario() {
        return poseeEscenario;
    }
}
