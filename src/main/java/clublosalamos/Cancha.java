package clublosalamos;

public class Cancha extends Recurso {
    private final String tipoDeporte;
    private final boolean tieneIluminacion;

    public Cancha(String codigo, String descripcion, int capacidadMaxima, double precioBaseHora, boolean habilitado,
                  String tipoDeporte, boolean tieneIluminacion) {
        super(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado);
        this.tipoDeporte = tipoDeporte;
        this.tieneIluminacion = tieneIluminacion;
    }

    @Override
    public String devolverTipo() {
        return "Cancha";
    }

    public String getTipoDeporte() {
        return tipoDeporte;
    }

    public boolean isTieneIluminacion() {
        return tieneIluminacion;
    }
}
