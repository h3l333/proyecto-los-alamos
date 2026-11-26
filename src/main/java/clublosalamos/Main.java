package clublosalamos;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Main
 *
 * Responsabilidades:
 * 1. Inicia el programa creando el modelo, la vista y el controlador.
 *
 * Colaboradores:
 * - ClubController: Main lo crea con el modelo y la vista y le delega la
 *   ejecución.
 * ============================================================================
 */
public class Main {
    public static void main(String[] args) {
        new ClubController(new Club(), new ConsoleView()).iniciar();
    }
}
