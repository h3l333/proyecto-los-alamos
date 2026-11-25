package clublosalamos;

import java.math.BigDecimal;

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
 * 4. Valida el porcentaje de ajuste; cada categoría puede agregar sus
 *   propias restricciones.
 *
 * Colaboradores:
 * - Socio: Socio la conoce como su categoría.
 * - Reserva: Reserva la consulta para calcular el importe final.
 * - Club: Club modifica su porcentaje de ajuste.
 * ============================================================================
 */
public abstract class TipoSocio {
    private final String descripcion;
    private BigDecimal porcentajeAjuste; // fracción: negativo = descuento, positivo = recargo (0.2 = 20%)

    protected TipoSocio(String descripcion, BigDecimal porcentajeAjuste) {
        this.descripcion = descripcion;
        this.porcentajeAjuste = porcentajeAjuste;
    }

    public String getDescripcion() { return descripcion; }
    public BigDecimal getPorcentajeAjuste() { return porcentajeAjuste; }
    public void setPorcentajeAjuste(BigDecimal p) {
        if (p.compareTo(BigDecimal.ONE.negate()) <= 0) {
            throw new IllegalArgumentException("El descuento no puede ser del 100% o más");
        }
        validarAjuste(p);
        this.porcentajeAjuste = p;
    }

    // Cada categoría puede restringir el signo del ajuste. Por defecto admite descuento y recargo.
    protected void validarAjuste(BigDecimal p) {
    }

    // El resultado se redondea a centavos para que el importe sea siempre exacto.
    public BigDecimal aplicarAjuste(BigDecimal importe) {
        return importe.multiply(BigDecimal.ONE.add(porcentajeAjuste))
                .setScale(Reserva.ESCALA_IMPORTE, Reserva.REDONDEO_IMPORTE);
    }
}
