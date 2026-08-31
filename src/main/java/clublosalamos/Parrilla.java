package clublosalamos;

public class Parrilla extends Recurso {
    private final double capacidadKgCarne;

    public Parrilla(String codigo, String descripcion, int capacidadMaxima, double precioBaseHora, boolean habilitado,
                     double capacidadKgCarne) {
        super(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado);
        this.capacidadKgCarne = capacidadKgCarne;
    }

    @Override
    public String devolverTipo() {
        return "Parrilla";
    }

    public double getCapacidadKgCarne() {
        return capacidadKgCarne;
    }
}
