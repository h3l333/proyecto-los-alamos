package clublosalamos;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: ConsoleView
 *
 * Responsabilidades:
 * 1. Muestra el menú, los mensajes y los listados por consola.
 * 2. Pide datos al usuario y vuelve a pedirlos mientras el formato sea
 *   incorrecto, de modo que nunca devuelve un valor inválido por formato.
 * 3. Muestra el email simulado de confirmación de una reserva.
 *
 * Colaboradores:
 * - Reserva: ConsoleView muestra su resumen.
 * ============================================================================
 */
public class ConsoleView {
    private final Scanner entrada;

    public ConsoleView() {
        entrada = new Scanner(System.in);
        // Cada token es una línea completa: permite ingresar textos con espacios.
        entrada.useDelimiter(System.getProperty("line.separator"));
    }

    public int mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("BIENVENIDO AL CLUB LOS ALAMOS");
        System.out.println("-----------------------------");
        System.out.println("1) Añadir un recurso");
        System.out.println("2) Registrar socio");
        System.out.println("3) Dar de baja a un socio");
        System.out.println("4) Verificar situación administrativa de un socio");
        System.out.println("5) Registrar reserva");
        System.out.println("6) Confirmar reserva (pago del saldo)");
        System.out.println("7) Cancelar reserva");
        System.out.println("8) Finalizar reserva");
        System.out.println("9) Consultar reservas por socio");
        System.out.println("10) Consultar reservas por recurso y fecha");
        System.out.println("11) Administrar categorías de socio");
        System.out.println("12) Generar informe de reservas del día");
        System.out.println("13) Pasar un socio suspendido a moroso");
        System.out.println("14) Suspender a un socio");
        System.out.println("15) Habilitar a un socio suspendido o moroso");
        System.out.println("0) Salir");
        return pedirEntero("Por favor ingrese la operación que desea realizar: ");
    }

    public int mostrarMenuTipoRecurso() {
        System.out.println("Tipo de recurso: 1) Quincho  2) Salón  3) Cancha  4) Parrilla  5) Espacio de recreación");
        return pedirEntero("Ingrese el tipo: ");
    }

    public int mostrarMenuCategoria() {
        System.out.println("Categoría: 1) Activo  2) Adherente  3) Vitalicio");
        return pedirEntero("Ingrese la categoría: ");
    }

    public int mostrarMenuMedioPago() {
        System.out.println("Medio de pago: 1) Tarjeta de crédito  2) Tarjeta de débito  3) Billetera virtual");
        return pedirEntero("Ingrese el medio de pago: ");
    }

    public String pedirTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.next().trim();
    }

    // Reintenta mientras el texto ingresado no sea un número entero.
    public int pedirEntero(String mensaje) {
        while (true) {
            String texto = pedirTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("'" + texto + "' no es un número entero.");
            }
        }
    }

    public double pedirDouble(String mensaje) {
        while (true) {
            String texto = pedirTexto(mensaje);
            try {
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("'" + texto + "' no es un número.");
            }
        }
    }

    public BigDecimal pedirBigDecimal(String mensaje) {
        while (true) {
            String texto = pedirTexto(mensaje);
            try {
                return new BigDecimal(texto);
            } catch (NumberFormatException e) {
                System.out.println("'" + texto + "' no es un número.");
            }
        }
    }

    public boolean pedirBooleano(String mensaje) {
        while (true) {
            String texto = pedirTexto(mensaje + "(s/n): ").toLowerCase();
            if (texto.equals("s")) {
                return true;
            }
            if (texto.equals("n")) {
                return false;
            }
            System.out.println("Responda 's' o 'n'.");
        }
    }

    // Reintenta mientras el día, mes y año no formen una fecha existente (por ejemplo 31/2).
    public LocalDate pedirFecha(String mensaje) {
        System.out.println(mensaje);
        while (true) {
            int dia = pedirEntero("Día: ");
            int mes = pedirEntero("Mes: ");
            int anio = pedirEntero("Año: ");
            try {
                return LocalDate.of(anio, mes, dia);
            } catch (DateTimeException e) {
                System.out.println("La fecha " + dia + "/" + mes + "/" + anio + " no existe. Ingrésela nuevamente.");
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarReservas(ArrayList<Reserva> reservas, String titulo) {
        System.out.println(titulo);
        if (reservas.isEmpty()) {
            System.out.println("No se encontraron reservas.");
        }
        for (Reserva reserva : reservas) {
            System.out.println(reserva.resumen());
        }
    }

    // Simulación: en lugar de enviar un email real, lo imprime por consola.
    public void mostrarEmailConfirmacion(Reserva reserva) {
        System.out.println();
        System.out.println("--- EMAIL SIMULADO ---");
        System.out.println("Para: " + reserva.getCorreoSocio());
        System.out.println("Asunto: Reserva #" + reserva.getNumeroReserva() + " registrada");
        System.out.println(reserva.resumen());
        System.out.println("----------------------");
    }
}
