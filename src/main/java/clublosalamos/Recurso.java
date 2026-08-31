package clublosalamos;

import java.time.LocalDate;
import java.time.LocalTime;

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
