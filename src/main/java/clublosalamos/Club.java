package clublosalamos;

import java.util.HashMap;
import java.util.Map;

public class Club {
    private final Map<String, Recurso> recursos = new HashMap<>();

    public void agregarRecurso(Recurso recurso) {
        recursos.put(recurso.getCodigo(), recurso);
    }
}
