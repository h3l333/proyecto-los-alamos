package clublosalamos;

import java.util.HashMap;
import java.util.Map;

public class Club {
    private final Map<String, Recurso> recursos = new HashMap<>();

    public void registrarRecurso(Recurso recurso) {
        recursos.put(recurso.getCodigo(), recurso);
    }
}
