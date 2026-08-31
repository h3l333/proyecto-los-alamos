package clublosalamos;

public class EspacioRecreacion extends Recurso {
    private final String tipoEspacio;

    public EspacioRecreacion(String codigo, String descripcion, int capacidadMaxima, double precioBaseHora, boolean habilitado,
                              String tipoEspacio) {
        super(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado);
        this.tipoEspacio = tipoEspacio;
    }

    @Override
    public String devolverTipo() {
        return "EspacioRecreacion";
    }

    public String getTipoEspacio() {
        return tipoEspacio;
    }
}
