package clublosalamos;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Recurso
 *
 * Responsabilidades:
 * 1. Guarda los datos comunes de un recurso alquilable del club.
 * 2. Indica si el recurso está disponible para ser reservado.
 * 3. Declara el tipo de recurso que representa cada subclase.
 *
 * Colaboradores:
 * - Ninguno.
 * ============================================================================
 */
public abstract class Recurso {
    private final String codigo;
    private final String descripcion;
    private final int capacidadMaxima;
    private final double precioBaseHora;
    private final boolean habilitado;

    protected Recurso(String codigo, String descripcion, int capacidadMaxima, double precioBaseHora, boolean habilitado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.capacidadMaxima = capacidadMaxima;
        this.precioBaseHora = precioBaseHora;
        this.habilitado = habilitado;
    }

    public boolean estaDisponible(LocalDate fecha, LocalTime horaInicio, int horas) {
        return habilitado;
    }

    public abstract String devolverTipo();

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public double getPrecioBaseHora() {
        return precioBaseHora;
    }

    public boolean isHabilitado() {
        return habilitado;
    }
}
