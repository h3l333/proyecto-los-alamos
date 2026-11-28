package clublosalamos;

import java.math.BigDecimal;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Quincho
 *
 * Responsabilidades:
 * 1. Retorna los datos asociados a un recurso de tipo `Quincho`.
 * 2. Redefine el detalle del recurso agregando sus datos propios.
 *
 * Colaboradores:
 * - Recurso: Sucede por herencia.
 * ============================================================================
 */
public class Quincho extends Recurso {
    private final boolean esCerrado;
    private final boolean tieneFreezer;
    private final int cantidadMesas;

    public Quincho(String codigo, String descripcion, int capacidadMaxima, BigDecimal precioBaseHora,
                   boolean habilitado, boolean esCerrado, boolean tieneFreezer, int cantidadMesas) {
        super(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado);
        this.esCerrado = esCerrado;
        this.tieneFreezer = tieneFreezer;
        this.cantidadMesas = cantidadMesas;
    }

    @Override
    public String devolverTipo() {
        return "Quincho";
    }

    @Override
    public String getDetalle() {
        return super.getDetalle()
                + " | Cerrado: " + (esCerrado ? "sí" : "no")
                + " | Freezer: " + (tieneFreezer ? "sí" : "no")
                + " | Mesas: " + cantidadMesas;
    }

    public boolean isEsCerrado() {
        return esCerrado;
    }

    public boolean isTieneFreezer() {
        return tieneFreezer;
    }

    public int getCantidadMesas() {
        return cantidadMesas;
    }
}
