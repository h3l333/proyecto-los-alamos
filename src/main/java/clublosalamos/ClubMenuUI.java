package clublosalamos;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: ClubMENUUI
 * 
 * Resposabilidades:
 * 1. Maneja interacción con el usuario y delega responsabilidad de lógica del
 *   negocio a la clase Club.
 * 
 * Colaboradores:
 * - Club: Club gestiona las interacciones entre los objetos representativos
 *   dominio.
 * ============================================================================
 */
public class ClubMenuUI {
    private final Club club = new Club();
    private final Scanner input = new Scanner(System.in);

    public void iniciar() {
        byte opcion;
        do {
            mostrarMenu();
            opcion = leerByte();
            ejecutar(opcion);
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("BIENVENIDO AL CLUB LOS ALAMOS\n");
        System.out.println("-----------------------------\n");
        System.out.print("\nPor favor ingrese la operación que desea realizar: ");
        System.out.print("1) Añadir un recurso\n");

    }

    private byte leerByte() {
        try {
            byte valor = input.nextByte(); // nextByte() lee y el siguiente token de la entrada y lo interpreta como número byte
            // antes de devolverlo.
            input.nextLine(); // nextLine() limpia el \n dejado en el buffer por la opresión de Enter por parte del usuario.
            // nextByte() y nextLine() son métodos de instancia de java.util.Scanner, clase previamente importada.
            return valor;
        } catch (InputMismatchException e) {
            // Error de tiempo de ejecución arrojado por la clase Scanner cuando el dato leido de un archivo o buffer
            // no esta en el formato esperado.
            input.nextLine();
            System.out.println("Dato ingresado en formato incorrecto");
            return -1;
        }
    }

    private void ejecutar(byte opcion) {
        try {
            switch(opcion) {
                case 1:
                    Recurso recurso = pedirDatosRecurso();
                    if (recurso != null) {
                        club.agregarRecurso(recurso);
                        System.out.println("Recurso agregado correctamente.");
                    }
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } catch (InputMismatchException e) {
            System.out.println("Dato ingresado en formato incorrecto");
            input.nextLine();
        }
    }

    private Recurso pedirDatosRecurso() {
        System.out.println("\nTipo de recurso:");
        System.out.println("1) Quincho");
        System.out.println("2) Salon");
        System.out.println("3) Cancha");
        System.out.println("4) Parrilla");
        System.out.println("5) Espacio de recreacion");
        System.out.print("Ingrese el tipo: ");
        byte tipo = leerByte();

        System.out.print("Codigo: ");
        String codigo = input.nextLine();
        System.out.print("Descripcion: ");
        String descripcion = input.nextLine();
        System.out.print("Capacidad maxima: ");
        int capacidadMaxima = leerInt();
        System.out.print("Precio base por hora: ");
        double precioBaseHora = leerDouble();
        System.out.print("Habilitado (s/n): ");
        boolean habilitado = leerBooleano();

        switch (tipo) {
            case 1:
                System.out.print("Es cerrado (s/n): ");
                boolean esCerrado = leerBooleano();
                System.out.print("Tiene freezer (s/n): ");
                boolean tieneFreezer = leerBooleano();
                System.out.print("Cantidad de mesas: ");
                int cantidadMesas = leerInt();
                return new Quincho(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        esCerrado, tieneFreezer, cantidadMesas);
            case 2:
                System.out.print("Tiene aire acondicionado (s/n): ");
                boolean tieneAireAcondicionado = leerBooleano();
                System.out.print("Posee escenario (s/n): ");
                boolean poseeEscenario = leerBooleano();
                return new Salon(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        tieneAireAcondicionado, poseeEscenario);
            case 3:
                System.out.print("Tipo de deporte: ");
                String tipoDeporte = input.nextLine();
                System.out.print("Tiene iluminacion (s/n): ");
                boolean tieneIluminacion = leerBooleano();
                return new Cancha(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        tipoDeporte, tieneIluminacion);
            case 4:
                System.out.print("Capacidad en kg de carne: ");
                double capacidadKgCarne = leerDouble();
                return new Parrilla(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        capacidadKgCarne);
            case 5:
                System.out.print("Tipo de espacio: ");
                String tipoEspacio = input.nextLine();
                return new EspacioRecreacion(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        tipoEspacio);
            default:
                System.out.println("Tipo de recurso invalido");
                return null;
        }
    }

    private int leerInt() {
        try {
            int valor = input.nextInt();
            input.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            input.nextLine();
            System.out.println("Dato ingresado en formato incorrecto");
            return -1;
        }
    }

    private double leerDouble() {
        try {
            double valor = input.nextDouble();
            input.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            input.nextLine();
            System.out.println("Dato ingresado en formato incorrecto");
            return -1;
        }
    }

    private boolean leerBooleano() {
        String valor = input.nextLine().trim().toLowerCase();
        return valor.equals("s");
    }

}
