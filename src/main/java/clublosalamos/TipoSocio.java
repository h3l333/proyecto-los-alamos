package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: TipoSocio
 *
 * Responsabilidades:
 * 1. Guarda la descripción de una categoría de socio y su porcentaje de
 *   ajuste (negativo = descuento, positivo = recargo).
 * 2. Permite modificar el porcentaje de ajuste desde el sistema.
 * 3. Aplica el ajuste de la categoría a un importe dado.
 *
 * Colaboradores:
 * - Socio: Socio la conoce como su categoría.
 * - Reserva: Reserva la consulta para calcular el importe final.
 * ============================================================================
 */
public abstract class TipoSocio {
    private final String descripcion;
    private double porcentajeAjuste; // negativo = descuento, positivo = recargo

    protected TipoSocio(String descripcion, double porcentajeAjuste) {
        this.descripcion = descripcion;
        this.porcentajeAjuste = porcentajeAjuste;
    }

    public String getDescripcion() { return descripcion; }
    public double getPorcentajeAjuste() { return porcentajeAjuste; }
    public void setPorcentajeAjuste(double p) { this.porcentajeAjuste = p; }

    public double aplicarAjuste(double importe) {
        return importe * (1 + porcentajeAjuste);
    }
}
