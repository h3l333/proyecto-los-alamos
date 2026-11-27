package clublosalamos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Club
 *
 * Responsabilidades:
 * 1. Guarda y manipula las colecciones de recursos, socios y reservas para su
 *   consulta y gestión, registrándolos y buscándolos por su identificador.
 *   Crea los recursos y socios a partir de los datos recibidos del
 *   controlador. Las reservas usan Doble Almacenamiento: un Hashtable para el
 *   acceso directo por número y un ArrayList para recorrerlas en orden de
 *   creación.
 * 2. Registra reservas, verificando que el socio esté registrado y habilitado
 *   y que el recurso esté registrado; la reserva valida la disponibilidad del
 *   recurso al crearse. Mantiene sincronizados ambos almacenamientos y la
 *   lista de reservas del recurso.
 * 3. Delega en las reservas su confirmación, cancelación y finalización, e
 *   informa su saldo pendiente.
 * 4. Consulta reservas por socio, por recurso y fecha, y por día.
 * 5. Verifica y describe la situación administrativa de un socio, lo da de
 *   baja, lo suspende, lo pasa a moroso y lo habilita, delegando en el socio.
 * 6. Configura el porcentaje de descuento o recargo de una categoría de socio
 *   y describe los porcentajes de todas las categorías.
 *
 * Colaboradores:
 * - Recurso: Club los registra y les agrega sus reservas.
 * - Socio: Club los registra y les delega los cambios de situación.
 * - Reserva: Club las registra, confirma, cancela, finaliza y consulta.
 * - Pago: Club lo recibe y se lo pasa a la reserva.
 * - Categoria: Club configura y describe el porcentaje de ajuste de cada
 *   categoría.
 * ============================================================================
 */
public class Club {
    private final Hashtable<String, Recurso> recursos;
    private final Hashtable<Integer, Socio> socios;
    private final Hashtable<Integer, Reserva> reservas;
    private final ArrayList<Reserva> reservasPorCreacion;

    public Club() {
        recursos = new Hashtable<String, Recurso>();
        socios = new Hashtable<Integer, Socio>();
        reservas = new Hashtable<Integer, Reserva>();
        reservasPorCreacion = new ArrayList<Reserva>();
    }

    public Recurso registrarQuincho(String codigo, String descripcion, int capacidadMaxima, BigDecimal precioBaseHora,
                                    boolean habilitado, boolean esCerrado, boolean tieneFreezer, int cantidadMesas) {
        return registrarRecurso(new Quincho(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                esCerrado, tieneFreezer, cantidadMesas));
    }

    public Recurso registrarSalon(String codigo, String descripcion, int capacidadMaxima, BigDecimal precioBaseHora,
                                  boolean habilitado, boolean tieneAireAcondicionado, boolean poseeEscenario) {
        return registrarRecurso(new Salon(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                tieneAireAcondicionado, poseeEscenario));
    }

    public Recurso registrarCancha(String codigo, String descripcion, int capacidadMaxima, BigDecimal precioBaseHora,
                                   boolean habilitado, String tipoDeporte, boolean tieneIluminacion) {
        return registrarRecurso(new Cancha(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                tipoDeporte, tieneIluminacion));
    }

    public Recurso registrarParrilla(String codigo, String descripcion, int capacidadMaxima, BigDecimal precioBaseHora,
                                     boolean habilitado, double capacidadKgCarne) {
        return registrarRecurso(new Parrilla(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                capacidadKgCarne));
    }

    public Recurso registrarEspacioRecreacion(String codigo, String descripcion, int capacidadMaxima,
                                              BigDecimal precioBaseHora, boolean habilitado, String tipoEspacio) {
        return registrarRecurso(new EspacioRecreacion(codigo, descripcion, capacidadMaxima, precioBaseHora, habilitado,
                tipoEspacio));
    }

    private Recurso registrarRecurso(Recurso recurso) {
        if (recursos.containsKey(recurso.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un recurso con el código " + recurso.getCodigo());
        }
        recursos.put(recurso.getCodigo(), recurso);
        return recurso;
    }

    // El socio se registra habilitado y con la fecha de alta del día.
    public void registrarSocio(int numeroSocio, String nombre, String apellido, String documento, String telefono,
                               String correo, Categoria categoria) {
        if (socios.containsKey(numeroSocio)) {
            throw new IllegalArgumentException("Ya existe un socio con el número " + numeroSocio);
        }
        socios.put(numeroSocio, new Socio(numeroSocio, nombre, apellido, documento, telefono, correo,
                LocalDate.now(), Habilitado.getInstancia(), categoria));
    }

    // Describe si un socio puede reservar o no dependiendo de su situacion administrativa.
    public boolean verificarSituacionAdministrativa(int numeroSocio) {
        return obtenerSocio(numeroSocio).puedeReservar();
    }

    public String describirSituacionAdministrativa(int numeroSocio) {
        return obtenerSocio(numeroSocio).describirSituacion();
    }

    // Importe del pago inicial (50% del total) que debe abonar el socio al reservar. Valida la solicitud
    // completa antes de que el socio cargue el pago.
    public BigDecimal calcularPagoInicial(int numeroSocio, String codigoRecurso, LocalDate fechaUso, int horaInicio,
                                          int duracionHoras) {
        Socio socio = obtenerSocioQuePuedeReservar(numeroSocio);
        Recurso recurso = obtenerRecurso(codigoRecurso);
        Reserva.validarSolicitud(recurso, fechaUso, horaInicio, duracionHoras);
        return Reserva.calcularPagoInicial(socio, recurso, duracionHoras);
    }

    // Registra una reserva a nombre de un socio. La reserva valida sus propios datos y la disponibilidad
    // del recurso al crearse. Los tres almacenamientos se actualizan juntos para no desincronizarse.
    public Reserva registrarReserva(int numeroSocio, String codigoRecurso, LocalDate fechaUso, int horaInicio,
                                    int duracionHoras, Pago pagoInicial) {
        Socio socio = obtenerSocioQuePuedeReservar(numeroSocio);
        Recurso recurso = obtenerRecurso(codigoRecurso);
        Reserva reserva = new Reserva(socio, recurso, fechaUso, horaInicio, duracionHoras, pagoInicial);
        reservas.put(reserva.getNumeroReserva(), reserva);
        reservasPorCreacion.add(reserva);
        recurso.agregarReserva(reserva);
        return reserva;
    }

    public BigDecimal consultarSaldoPendiente(int numeroReserva) {
        return obtenerReserva(numeroReserva).getSaldoPendiente();
    }

    // Registra el pago del saldo restante y confirma la reserva.
    public void confirmarReserva(int numeroReserva, Pago pagoSaldo) {
        obtenerReserva(numeroReserva).confirmar(pagoSaldo);
    }

    public void cancelarReserva(int numeroReserva) {
        obtenerReserva(numeroReserva).cancelar();
    }

    public void finalizarReserva(int numeroReserva) {
        obtenerReserva(numeroReserva).finalizar();
    }

    public ArrayList<Reserva> consultarReservasPorSocio(int numeroSocio) {
        ArrayList<Reserva> resultado = new ArrayList<Reserva>();
        for (Reserva reserva : reservasPorCreacion) {
            if (reserva.esDelSocio(numeroSocio)) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    public ArrayList<Reserva> consultarReservasPorRecursoYFecha(String codigoRecurso, LocalDate fecha) {
        ArrayList<Reserva> resultado = new ArrayList<Reserva>();
        for (Reserva reserva : reservasPorCreacion) {
            if (reserva.esDelRecurso(codigoRecurso) && reserva.esDelDia(fecha)) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    // Reservas con fecha de uso en el día indicado, en el orden en que fueron creadas.
    public ArrayList<Reserva> consultarReservasDelDia(LocalDate fecha) {
        ArrayList<Reserva> delDia = new ArrayList<Reserva>();
        for (Reserva reserva : reservasPorCreacion) {
            if (reserva.esDelDia(fecha)) {
                delDia.add(reserva);
            }
        }
        return delDia;
    }

    // Se admite desde cualquier situación salvo si el socio ya está dado de baja.
    public void darSocioDeBaja(int numeroSocio) {
        obtenerSocio(numeroSocio).darDeBaja();
    }

    // Solo un socio habilitado puede ser suspendido.
    public void suspenderSocio(int numeroSocio) {
        obtenerSocio(numeroSocio).suspender();
    }

    // Solo un socio suspendido puede pasar a moroso.
    public void marcarSocioMoroso(int numeroSocio) {
        obtenerSocio(numeroSocio).marcarMoroso();
    }

    // Solo un socio suspendido o moroso puede volver a ser habilitado.
    public void habilitarSocio(int numeroSocio) {
        obtenerSocio(numeroSocio).habilitar();
    }

    // El porcentaje se expresa en puntos porcentuales: positivo = recargo, negativo = descuento.
    // Cada categoría valida por polimorfismo qué signo admite.
    public void configurarAjusteCategoria(Categoria categoria, BigDecimal porcentaje) {
        categoria.setPorcentajeAjuste(porcentaje.movePointLeft(2));
    }

    public String describirCategorias() {
        return Activo.getInstancia().describir() + " | " + Adherente.getInstancia().describir() + " | "
                + Vitalicio.getInstancia().describir();
    }

    private Socio obtenerSocio(int numeroSocio) {
        Socio socio = socios.get(numeroSocio);
        if (socio == null) {
            throw new IllegalArgumentException("El socio " + numeroSocio + " no está registrado");
        }
        return socio;
    }

    private Socio obtenerSocioQuePuedeReservar(int numeroSocio) {
        Socio socio = obtenerSocio(numeroSocio);
        if (!socio.puedeReservar()) {
            throw new IllegalStateException("La situación administrativa del socio no permite reservar");
        }
        return socio;
    }

    private Recurso obtenerRecurso(String codigoRecurso) {
        Recurso recurso = recursos.get(codigoRecurso);
        if (recurso == null) {
            throw new IllegalArgumentException("El recurso " + codigoRecurso + " no está registrado");
        }
        return recurso;
    }

    private Reserva obtenerReserva(int numeroReserva) {
        Reserva reserva = reservas.get(numeroReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva " + numeroReserva + " no existe");
        }
        return reserva;
    }
}
