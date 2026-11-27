package clublosalamos;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: ClubController
 *
 * Responsabilidades:
 * 1. Ejecuta el menú principal y dispara cada caso de uso.
 * 2. Pide los datos a la vista y le delega a Club la lógica del negocio.
 * 3. Crea los pagos mediante los factory methods de Pago.
 * 4. Captura las excepciones de validación del dominio y le pide a la vista
 *   que muestre su mensaje.
 *
 * Colaboradores:
 * - Club: es el modelo; ClubController le delega cada caso de uso.
 * - ConsoleView: es la vista; ClubController le pide datos y le pasa lo que
 *   debe mostrar.
 * - Pago: ClubController lo crea con sus factory methods.
 * - Reserva: ClubController la recibe de Club para que la vista la muestre.
 * - Categoria: ClubController traduce la opción elegida a una categoría.
 * ============================================================================
 */
public class ClubController {
    private final Club modelo;
    private final ConsoleView vista;

    public ClubController(Club modelo, ConsoleView vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.mostrarMenuPrincipal();
            try {
                ejecutar(opcion);
            } catch (IllegalArgumentException | IllegalStateException e) {
                // Excepciones de validación lanzadas por Club y el dominio.
                vista.mostrarMensaje("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void ejecutar(int opcion) {
        switch (opcion) {
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
                vista.mostrarMensaje("Programa finalizado.");
                break;
            default:
                vista.mostrarMensaje("Opción inválida.");
        }
    }

    private void registrarRecurso() {
        int tipo;
        do {
            tipo = vista.mostrarMenuTipoRecurso();
        } while (tipo < 1 || tipo > 5);

        String codigo = vista.pedirTexto("Código: ");
        String descripcion = vista.pedirTexto("Descripción: ");
        int capacidadMaxima = vista.pedirEntero("Capacidad máxima: ");
        BigDecimal precioBaseHora = vista.pedirBigDecimal("Precio base por hora: ");
        boolean habilitado = vista.pedirBooleano("Habilitado ");

        Recurso recurso = null;
        switch (tipo) {
            case 1:
                boolean esCerrado = vista.pedirBooleano("Es cerrado ");
                boolean tieneFreezer = vista.pedirBooleano("Tiene freezer ");
                int cantidadMesas = vista.pedirEntero("Cantidad de mesas: ");
                recurso = modelo.registrarQuincho(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        esCerrado, tieneFreezer, cantidadMesas);
                break;
            case 2:
                boolean tieneAireAcondicionado = vista.pedirBooleano("Tiene aire acondicionado ");
                boolean poseeEscenario = vista.pedirBooleano("Posee escenario ");
                recurso = modelo.registrarSalon(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        tieneAireAcondicionado, poseeEscenario);
                break;
            case 3:
                String tipoDeporte = vista.pedirTexto("Tipo de deporte: ");
                boolean tieneIluminacion = vista.pedirBooleano("Tiene iluminación ");
                recurso = modelo.registrarCancha(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        tipoDeporte, tieneIluminacion);
                break;
            case 4:
                double capacidadKgCarne = vista.pedirDouble("Capacidad en kg de carne: ");
                recurso = modelo.registrarParrilla(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                        capacidadKgCarne);
                break;
            case 5:
                String tipoEspacio = vista.pedirTexto("Tipo de espacio: ");
                recurso = modelo.registrarEspacioRecreacion(codigo, descripcion, capacidadMaxima, precioBaseHora,
                        habilitado, tipoEspacio);
                break;
        }
        vista.mostrarMensaje("Recurso agregado correctamente: " + recurso.getDetalle());
    }

    private void registrarSocio() {
        int numeroSocio = vista.pedirEntero("Número de socio: ");
        String nombre = vista.pedirTexto("Nombre: ");
        String apellido = vista.pedirTexto("Apellido: ");
        String documento = vista.pedirTexto("Documento: ");
        String telefono = vista.pedirTexto("Teléfono: ");
        String correo = vista.pedirTexto("Correo electrónico: ");
        Categoria categoria = pedirCategoria();
        modelo.registrarSocio(numeroSocio, nombre, apellido, documento, telefono, correo, categoria);
        vista.mostrarMensaje("Socio registrado correctamente.");
    }

    // Traduce la opción elegida en la vista a la categoría correspondiente.
    private Categoria pedirCategoria() {
        while (true) {
            switch (vista.mostrarMenuCategoria()) {
                case 1:
                    return Activo.getInstancia();
                case 2:
                    return Adherente.getInstancia();
                case 3:
                    return Vitalicio.getInstancia();
                default:
                    vista.mostrarMensaje("Categoría inválida.");
            }
        }
    }

    private void darSocioDeBaja() {
        modelo.darSocioDeBaja(vista.pedirEntero("Número de socio: "));
        vista.mostrarMensaje("Socio dado de baja.");
    }

    private void suspenderSocio() {
        modelo.suspenderSocio(vista.pedirEntero("Número de socio: "));
        vista.mostrarMensaje("Socio suspendido.");
    }

    private void marcarSocioMoroso() {
        modelo.marcarSocioMoroso(vista.pedirEntero("Número de socio: "));
        vista.mostrarMensaje("Socio marcado como moroso.");
    }

    private void habilitarSocio() {
        modelo.habilitarSocio(vista.pedirEntero("Número de socio: "));
        vista.mostrarMensaje("Socio habilitado.");
    }

    private void verificarSituacionAdministrativa() {
        int numeroSocio = vista.pedirEntero("Número de socio: ");
        if (modelo.verificarSituacionAdministrativa(numeroSocio)) {
            vista.mostrarMensaje("El socio está habilitado para reservar.");
        } else {
            vista.mostrarMensaje("El socio NO puede reservar: situación administrativa "
                    + modelo.describirSituacionAdministrativa(numeroSocio) + ".");
        }
    }

    private void registrarReserva() {
        int numeroSocio = vista.pedirEntero("Número de socio: ");
        String codigoRecurso = vista.pedirTexto("Código del recurso: ");
        LocalDate fechaUso = vista.pedirFecha("Fecha de uso:");
        int horaInicio = vista.pedirEntero("Hora de inicio (0 a 23): ");
        int duracionHoras = vista.pedirEntero("Duración en horas: ");

        modelo.validarSolicitudReserva(numeroSocio, codigoRecurso, fechaUso, horaInicio, duracionHoras);
        BigDecimal pagoInicial = modelo.calcularPagoInicial(numeroSocio, codigoRecurso, duracionHoras);
        vista.mostrarMensaje("Pago inicial a abonar (50%): $" + pagoInicial);
        Pago pago = pedirPago(pagoInicial);
        Reserva reserva = modelo.registrarReserva(numeroSocio, codigoRecurso, fechaUso, horaInicio, duracionHoras,
                pago);
        vista.mostrarEmailConfirmacion(reserva);
        vista.mostrarMensaje("Reserva registrada con el número " + reserva.getNumeroReserva() + ".");
    }

    // Pide el medio de pago hasta recibir uno válido y crea el pago con el factory method correspondiente.
    private Pago pedirPago(BigDecimal monto) {
        while (true) {
            switch (vista.mostrarMenuMedioPago()) {
                case 1:
                    String bancoCredito = vista.pedirTexto("Banco emisor: ");
                    int cuotas = vista.pedirEntero("Cantidad de cuotas: ");
                    return Pago.crearCredito(monto, bancoCredito, cuotas);
                case 2:
                    String bancoDebito = vista.pedirTexto("Banco emisor: ");
                    String cbu = vista.pedirTexto("CBU: ");
                    String titular = vista.pedirTexto("Titular de la cuenta: ");
                    return Pago.crearDebito(monto, bancoDebito, cbu, titular);
                case 3:
                    String plataforma = vista.pedirTexto("Plataforma: ");
                    String cvu = vista.pedirTexto("CVU: ");
                    String alias = vista.pedirTexto("Alias: ");
                    return Pago.crearBilleteraVirtual(monto, plataforma, cvu, alias);
                default:
                    vista.mostrarMensaje("Medio de pago inválido.");
            }
        }
    }

    private void confirmarReserva() {
        int numeroReserva = vista.pedirEntero("Número de reserva: ");
        BigDecimal saldo = modelo.consultarSaldoPendiente(numeroReserva);
        vista.mostrarMensaje("Saldo pendiente a abonar: $" + saldo);
        // Java evalúa los argumentos antes de invocar el método modelo.confirmarReserva.
        modelo.confirmarReserva(numeroReserva, pedirPago(saldo));
        vista.mostrarMensaje("Reserva confirmada.");
    }

    private void cancelarReserva() {
        modelo.cancelarReserva(vista.pedirEntero("Número de reserva: "));
        vista.mostrarMensaje("Reserva cancelada.");
    }

    private void finalizarReserva() {
        modelo.finalizarReserva(vista.pedirEntero("Número de reserva: "));
        vista.mostrarMensaje("Reserva finalizada.");
    }

    private void consultarReservasPorSocio() {
        int numeroSocio = vista.pedirEntero("Número de socio: ");
        vista.mostrarReservas(modelo.consultarReservasPorSocio(numeroSocio), "RESERVAS DEL SOCIO " + numeroSocio);
    }

    private void consultarReservasPorRecursoYFecha() {
        String codigoRecurso = vista.pedirTexto("Código del recurso: ");
        LocalDate fecha = vista.pedirFecha("Fecha:");
        vista.mostrarReservas(modelo.consultarReservasPorRecursoYFecha(codigoRecurso, fecha),
                "RESERVAS DEL RECURSO " + codigoRecurso + " EL " + fecha);
    }

    private void administrarCategorias() {
        vista.mostrarMensaje("Ajustes actuales: " + modelo.describirCategorias());
        Categoria categoria = pedirCategoria();
        BigDecimal porcentaje = vista.pedirBigDecimal("Nuevo porcentaje (positivo = recargo, negativo = descuento): ");
        modelo.configurarAjusteCategoria(categoria, porcentaje);
        vista.mostrarMensaje("Porcentaje actualizado correctamente.");
    }

    private void generarInformeDelDia() {
        LocalDate fecha = vista.pedirFecha("Fecha del informe:");
        vista.mostrarReservas(modelo.consultarReservasDelDia(fecha),
                "INFORME DE RESERVAS DEL " + fecha + " (en orden de creación)");
    }
}
