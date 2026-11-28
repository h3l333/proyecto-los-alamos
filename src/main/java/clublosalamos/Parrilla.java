package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Parrilla
 *
 * Responsabilidades:
 * 1. Retorna los datos asociados a un recurso de tipo `Parrilla`.
 * 2. Redefine el detalle del recurso agregando sus datos propios.
 *
 * Colaboradores:
 * - Recurso: Sucede por herencia.
 * ============================================================================
 */
public class Parrilla extends Recurso {
    private final double capacidadKgCarne;

    public Parrilla(String codigo, String descripcion, int capacidadMaxima, BigDecimal precioBaseHora,
                    boolean habilitado, double capacidadKgCarne) {
        super(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado);
        this.capacidadKgCarne = capacidadKgCarne;
    }

    @Override
    public String devolverTipo() {
        return "Parrilla";
    }

    @Override
    public String getDetalle() {
        return super.getDetalle() + " | Capacidad de carne: " + capacidadKgCarne + " kg";
    }

    public double getCapacidadKgCarne() {
        return capacidadKgCarne;
    }
}
