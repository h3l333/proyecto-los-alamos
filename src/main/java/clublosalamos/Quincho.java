package clublosalamos;

public class Quincho extends Recurso {
    private final boolean esCerrado;
    private final boolean tieneFreezer;
    private final int cantidadMesas;

    public Quincho(String codigo, String descripcion, int capacidadMaxima, double precioBaseHora, boolean habilitado,
                    boolean esCerrado, boolean tieneFreezer, int cantidadMesas) {
        super(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado);
        this.esCerrado = esCerrado;
        this.tieneFreezer = tieneFreezer;
        this.cantidadMesas = cantidadMesas;
    }

    @Override
    public String devolverTipo() {
        return "Quincho";
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
