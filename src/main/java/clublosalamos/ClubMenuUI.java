package clublosalamos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: ClubMenuUI
 *
 * Responsabilidades:
 * 1. Maneja interacción con el usuario y delega responsabilidad de lógica del
 *   negocio a la clase Club.
 * 2. Captura las excepciones lanzadas por Club y muestra su mensaje al
 *   usuario.
 * 3. Solicita los datos de recursos, socios y pagos y se los pasa a Club, que
 *   los construye.
 * 4. Muestra el email simulado de confirmación de reserva y el informe de
 *   reservas del día.
 *
 * Colaboradores:
 * - Club: Club gestiona las interacciones entre los objetos representativos
 *   del dominio.
 * - Pago: ClubMenuUI recibe el pago creado por Club y lo pasa a la reserva.
 * - Reserva: ClubMenuUI muestra su resumen.
 * - TipoSocio: ClubMenuUI consulta los porcentajes de cada categoría.
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
        System.out.println("Por favor ingrese la operación que desea realizar:");
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
            System.out.println("Dato ingresado en formato incorrecto.");
            return -1;
        }
    }

    private void ejecutar(byte opcion) {
        try {
            switch(opcion) {
                case 1:
                    registrarRecurso();
                    break;
                case 2:
                    registrarSocio();
                    break;
                case 3:
                    darSocioDeBaja();
                    break;
                case 4:
                    verificarSituacionAdministrativa();
                    break;
                case 5:
                    registrarReserva();
                    break;
                case 6:
                    confirmarReserva();
                    break;
                case 7:
                    cancelarReserva();
                    break;
                case 8:
                    finalizarReserva();
                    break;
                case 9:
                    consultarReservasPorSocio();
                    break;
                case 10:
                    consultarReservasPorRecursoYFecha();
                    break;
                case 11:
                    administrarCategorias();
                    break;
                case 12:
                    generarInformeDelDia();
                    break;
                case 13:
                    marcarSocioMoroso();
                    break;
                case 14:
                    suspenderSocio();
                    break;
                case 15:
                    habilitarSocio();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } catch (InputMismatchException e) {
            System.out.println("Dato ingresado en formato incorrecto");
            input.nextLine();
        } catch (DateTimeParseException e) {
            System.out.println(
                "Fecha u hora ingresada en formato incorrecto. Las fechas se deben ingresar en formato aaaa-mm-dd, y las horas en formato hh:mm."
            );
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Excepciones de validación lanzadas por Club y el dominio.
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registrarSocio() {
        System.out.println("Número de socio:");
        int numeroSocio = leerInt();
        System.out.println("Nombre:");
        String nombre = input.nextLine();
        System.out.println("Apellido:");
        String apellido = input.nextLine();
        System.out.println("Documento:");
        String documento = input.nextLine();
        System.out.println("Teléfono:");
        String telefono = input.nextLine();
        System.out.println("Correo electrónico:");
        String correo = input.nextLine();
        TipoSocio tipo = pedirTipoSocio();
        club.registrarSocio(numeroSocio, nombre, apellido, documento, telefono, correo, tipo);
        System.out.println("Socio registrado correctamente.");
    }

    private TipoSocio pedirTipoSocio() {
        System.out.println("Categoría: 1) Activo  2) Adherente  3) Vitalicio");
        System.out.println("Ingrese la categoría:");
        switch (leerByte()) {
            case 1:
                return Activo.getInstancia();
            case 2:
                return Adherente.getInstancia();
            case 3:
                return SocioVitalicio.getInstancia();
            default:
                throw new IllegalArgumentException("Categoría inválida");
        }
    }

    private void darSocioDeBaja() {
        System.out.println("Número de socio:");
        club.darSocioDeBaja(leerInt());
        System.out.println("Socio dado de baja.");
    }

    private void suspenderSocio() {
        System.out.println("Número de socio:");
        club.suspenderSocio(leerInt());
        System.out.println("Socio suspendido.");
    }

    private void marcarSocioMoroso() {
        System.out.println("Número de socio:");
        club.marcarSocioMoroso(leerInt());
        System.out.println("Socio marcado como moroso.");
    }

    private void habilitarSocio() {
        System.out.println("Número de socio:");
        club.habilitarSocio(leerInt());
        System.out.println("Socio habilitado.");
    }

    private void verificarSituacionAdministrativa() {
        System.out.println("Número de socio:");
        int numeroSocio = leerInt();
        if (club.verificarSituacionAdministrativa(numeroSocio)) {
            System.out.println("El socio está habilitado para reservar.");
        } else {
            System.out.println("El socio NO puede reservar: situación administrativa "
                    + club.obtenerSocio(numeroSocio).getSituacionAdministrativa().getSituacionAdmin() + ".");
        }
    }

    private void registrarReserva() {
        System.out.println("Número de socio:");
        int numeroSocio = leerInt();
        System.out.println("Código del recurso:");
        String codigoRecurso = input.nextLine();
        System.out.println("Fecha de uso (aaaa-mm-dd):");
        LocalDate fechaUso = LocalDate.parse(input.nextLine().trim());
        System.out.println("Horario de inicio (hh:mm):");
        LocalTime horarioInicio = LocalTime.parse(input.nextLine().trim());
        System.out.println("Duración en horas:");
        int duracionHoras = leerInt();

        BigDecimal pagoInicial = club.calcularPagoInicial(numeroSocio, codigoRecurso, duracionHoras);
        System.out.printf("Pago inicial a abonar (50%%): $%.2f%n", pagoInicial);
        Pago pago = pedirPago(pagoInicial);
        Reserva reserva = club.registrarReserva(numeroSocio, codigoRecurso, fechaUso, horarioInicio,
                duracionHoras, pago);
        mostrarEmailConfirmacion(reserva);
        System.out.println("Reserva registrada con el número " + reserva.getNumeroReserva() + ".");
    }

    // Simulación: en lugar de enviar un email real, lo imprime por consola.
    private void mostrarEmailConfirmacion(Reserva reserva) {
        System.out.println("\n--- EMAIL SIMULADO ---");
        System.out.println("Para: " + reserva.getSocio().getCorreoElectronico());
        System.out.println("Asunto: Reserva #" + reserva.getNumeroReserva() + " registrada");
        System.out.println(reserva.resumen());
        System.out.println("----------------------");
    }

    private Pago pedirPago(BigDecimal monto) {
        System.out.println("Medio de pago: 1) Tarjeta de crédito  2) Tarjeta de débito  3) Billetera virtual");
        System.out.println("Ingrese el medio de pago:");
        byte medio = leerByte();

        switch (medio) {
            case 1:
                System.out.println("Banco emisor:");
                String bancoCredito = input.nextLine();
                System.out.println("Cantidad de cuotas:");
                int cuotas = leerInt();
                return club.crearPagoCredito(monto, bancoCredito, cuotas);
            case 2:
                System.out.println("Banco emisor:");
                String bancoDebito = input.nextLine();
                System.out.println("CBU:");
                String cbu = input.nextLine();
                System.out.println("Titular de la cuenta:");
                String titular = input.nextLine();
                return club.crearPagoDebito(monto, bancoDebito, cbu, titular);
            case 3:
                System.out.println("Plataforma:");
                String plataforma = input.nextLine();
                System.out.println("CVU:");
                String cvu = input.nextLine();
                System.out.println("Alias:");
                String alias = input.nextLine();
                return club.crearPagoBilleteraVirtual(monto, plataforma, cvu, alias);
            default:
                throw new IllegalArgumentException("Medio de pago inválido");
        }
    }

    private void confirmarReserva() {
        System.out.println("Número de reserva:");
        int numeroReserva = leerInt();
        Reserva reserva = club.obtenerReserva(numeroReserva);
        System.out.printf("Saldo pendiente a abonar: $%.2f%n", reserva.getSaldoPendiente());
        club.confirmarReserva(numeroReserva, pedirPago(reserva.getSaldoPendiente()));
        System.out.println("Reserva confirmada.");
    }

    private void cancelarReserva() {
        System.out.println("Número de reserva:");
        club.cancelarReserva(leerInt());
        System.out.println("Reserva cancelada.");
    }

    private void finalizarReserva() {
        System.out.println("Número de reserva:");
        club.finalizarReserva(leerInt());
        System.out.println("Reserva finalizada.");
    }

    private void consultarReservasPorSocio() {
        System.out.println("Número de socio:");
        mostrarReservas(club.consultarReservasPorSocio(leerInt()));
    }

    private void consultarReservasPorRecursoYFecha() {
        System.out.println("Código del recurso:");
        String codigoRecurso = input.nextLine();
        System.out.println("Fecha (aaaa-mm-dd):");
        LocalDate fecha = LocalDate.parse(input.nextLine().trim());
        mostrarReservas(club.consultarReservasPorRecursoYFecha(codigoRecurso, fecha));
    }

    private void mostrarReservas(List<Reserva> reservas) {
        if (reservas.isEmpty()) {
            System.out.println("No se encontraron reservas.");
        }
        for (Reserva reserva : reservas) {
            System.out.println(reserva.resumen());
        }
    }

    private void administrarCategorias() {
        System.out.printf("Ajustes actuales: Activo %.1f%% | Adherente %.1f%% | Vitalicio %.1f%%%n",
                Activo.getInstancia().getPorcentajeAjuste().movePointRight(2),
                Adherente.getInstancia().getPorcentajeAjuste().movePointRight(2),
                SocioVitalicio.getInstancia().getPorcentajeAjuste().movePointRight(2));
        TipoSocio tipo = pedirTipoSocio();
        System.out.println("Nuevo porcentaje (positivo = recargo, negativo = descuento):");
        BigDecimal porcentaje = leerBigDecimal();
        if (tipo == Activo.getInstancia()) {
            club.configurarAjusteCategoriaSocioActivo(porcentaje);
        } else if (tipo == Adherente.getInstancia()) {
            club.configurarAjusteCategoriaSocioAdherente(porcentaje);
        } else {
            club.configurarAjusteCategoriaSocioVitalicio(porcentaje);
        }
        System.out.println("Porcentaje actualizado correctamente.");
    }

    private void generarInformeDelDia() {
        System.out.println("Fecha (aaaa-mm-dd):");
        LocalDate fecha = LocalDate.parse(input.nextLine().trim());
        List<Reserva> delDia = club.consultarReservasDelDia(fecha);
        System.out.println("INFORME DE RESERVAS DEL " + fecha + " (en orden de creación)");
        if (delDia.isEmpty()) {
            System.out.println("No hay reservas para este día.");
        }
        for (Reserva reserva : delDia) {
            System.out.println(reserva.resumen());
        }
    }

    private void registrarRecurso() {
        System.out.println("Tipo de recurso:");
        System.out.println("1) Quincho");
        System.out.println("2) Salon");
        System.out.println("3) Cancha");
        System.out.println("4) Parrilla");
        System.out.println("5) Espacio de recreacion");
        System.out.println("Ingrese el tipo:");
        byte tipo = leerByte();

        System.out.println("Codigo:");
        String codigo = input.nextLine();
        System.out.println("Descripcion:");
        String descripcion = input.nextLine();
        System.out.println("Capacidad maxima:");
        int capacidadMaxima = leerInt();
        System.out.println("Precio base por hora:");
        BigDecimal precioBaseHora = leerBigDecimal();
        System.out.println("Habilitado (s/n):");
        boolean habilitado = leerBooleano();

        switch (tipo) {
            case 1:
                System.out.println("Es cerrado (s/n):");
                boolean esCerrado = leerBooleano();
                System.out.println("Tiene freezer (s/n):");
                boolean tieneFreezer = leerBooleano();
                System.out.println("Cantidad de mesas:");
                int cantidadMesas = leerInt();
                club.registrarQuincho(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        esCerrado, tieneFreezer, cantidadMesas);
                break;
            case 2:
                System.out.println("Tiene aire acondicionado (s/n):");
                boolean tieneAireAcondicionado = leerBooleano();
                System.out.println("Posee escenario (s/n):");
                boolean poseeEscenario = leerBooleano();
                club.registrarSalon(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        tieneAireAcondicionado, poseeEscenario);
                break;
            case 3:
                System.out.println("Tipo de deporte:");
                String tipoDeporte = input.nextLine();
                System.out.println("Tiene iluminacion (s/n):");
                boolean tieneIluminacion = leerBooleano();
                club.registrarCancha(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        tipoDeporte, tieneIluminacion);
                break;
            case 4:
                System.out.println("Capacidad en kg de carne:");
                double capacidadKgCarne = leerDouble();
                club.registrarParrilla(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        capacidadKgCarne);
                break;
            case 5:
                System.out.println("Tipo de espacio:");
                String tipoEspacio = input.nextLine();
                club.registrarEspacioRecreacion(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        tipoEspacio);
                break;
            default:
                System.out.println("Tipo de recurso invalido");
                return;
        }
        System.out.println("Recurso agregado correctamente.");
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

    private BigDecimal leerBigDecimal() {
        try {
            BigDecimal valor = input.nextBigDecimal();
            input.nextLine();
            return valor;
        } catch (InputMismatchException e) {
            input.nextLine();
            System.out.println("Dato ingresado en formato incorrecto");
            return BigDecimal.ONE.negate();
        }
    }

    private boolean leerBooleano() {
        String valor = input.nextLine().trim().toLowerCase();
        return valor.equals("s");
    }

}
