package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Cancha
 *
 * Responsabilidades:
 * 1. Retorna los datos asociados a un recurso de tipo `Cancha`.
 * 2. Redefine el detalle del recurso agregando sus datos propios.
 *
 * Colaboradores:
 * - Recurso: Sucede por herencia.
 * ============================================================================
 */
public class Cancha extends Recurso {
    private final String tipoDeporte;
    private final boolean tieneIluminacion;

    public Cancha(String codigo, String descripcion, int capacidadMaxima, BigDecimal precioBaseHora,
                  boolean habilitado, String tipoDeporte, boolean tieneIluminacion) {
        super(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado);
        this.tipoDeporte = tipoDeporte;
        this.tieneIluminacion = tieneIluminacion;
    }

    @Override
    public String devolverTipo() {
        return "Cancha";
    }

    @Override
    public String getDetalle() {
        return super.getDetalle()
                + " | Deporte: " + tipoDeporte
                + " | Iluminación: " + (tieneIluminacion ? "sí" : "no");
    }

    public String getTipoDeporte() {
        return tipoDeporte;
    }

    public boolean isTieneIluminacion() {
        return tieneIluminacion;
    }
}
