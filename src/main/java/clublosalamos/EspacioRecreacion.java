package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: EspacioRecreacion
 *
 * Responsabilidades:
 * 1. Retorna los datos asociados a un recurso de tipo `EspacioRecreacion`.
 *
 * Colaboradores:
 * - Recurso: Sucede por herencia.
 * ============================================================================
 */
public class EspacioRecreacion extends Recurso {
    private final String tipoEspacio;

    public EspacioRecreacion(String codigo, String descripcion, int capacidadMaxima, double precioBaseHora, boolean habilitado,
                              String tipoEspacio) {
        super(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado);
        this.tipoEspacio = tipoEspacio;
    }

    @Override
    public String devolverTipo() {
        return "EspacioRecreacion";
    }

    public String getTipoEspacio() {
        return tipoEspacio;
    }
}
