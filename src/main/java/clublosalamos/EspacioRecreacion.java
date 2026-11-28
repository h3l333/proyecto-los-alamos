package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: EspacioRecreacion
 *
 * Responsabilidades:
 * 1. Retorna los datos asociados a un recurso de tipo `EspacioRecreacion`.
 * 2. Redefine el detalle del recurso agregando sus datos propios.
 *
 * Colaboradores:
 * - Recurso: Sucede por herencia.
 * ============================================================================
 */
public class EspacioRecreacion extends Recurso {
    private final String tipoEspacio;

    public EspacioRecreacion(String codigo, String descripcion, int capacidadMaxima, BigDecimal precioBaseHora,
                             boolean habilitado, String tipoEspacio) {
        super(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado);
        this.tipoEspacio = tipoEspacio;
    }

    @Override
    public String devolverTipo() {
        return "EspacioRecreacion";
    }

    @Override
    public String getDetalle() {
        return super.getDetalle() + " | Tipo de espacio: " + tipoEspacio;
    }

    public String getTipoEspacio() {
        return tipoEspacio;
    }
}
