package clublosalamos;

public class SocioVitalicio extends TipoSocio {
    private static final SocioVitalicio INSTANCIA = new SocioVitalicio();
    private SocioVitalicio() { super("Vitalicio", -0.5); }
    public static SocioVitalicio getInstancia() { return INSTANCIA; }
}
