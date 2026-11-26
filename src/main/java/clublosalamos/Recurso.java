package clublosalamos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Recurso
 *
 * Responsabilidades:
 * 1. Guarda los datos comunes de un recurso alquilable del club y los valida
 *   al crearse.
 * 2. Conoce sus propias reservas (agregación inversa) e indica si está
 *   disponible en una fecha y franja horaria: debe estar habilitado y no
 *   tener reservas que ocupen esa franja.
 * 3. Declara el tipo de recurso que representa cada subclase.
 * 4. Provee el detalle de sus datos; cada subclase le agrega los propios.
 *
 * Colaboradores:
 * - Reserva: Recurso guarda sus reservas y les pregunta si ocupan una
 *   franja horaria.
 * ============================================================================
 */
public abstract class Recurso {
    private final String codigo;
    private final String descripcion;
    private final int capacidadMaxima;
    private final BigDecimal precioBaseHora;
    private final boolean habilitado;
    private final ArrayList<Reserva> reservas;

    protected Recurso(String codigo, String descripcion, int capacidadMaxima, BigDecimal precioBaseHora, boolean habilitado) {
        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException("El código del recurso es obligatorio");
        }
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a cero");
        }
        if (precioBaseHora == null || precioBaseHora.signum() <= 0) {
            throw new IllegalArgumentException("El precio base por hora debe ser mayor a cero");
        }
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.capacidadMaxima = capacidadMaxima;
        this.precioBaseHora = precioBaseHora;
        this.habilitado = habilitado;
        this.reservas = new ArrayList<Reserva>();
    }

    public boolean estaDisponible(LocalDate fecha, int horaInicio, int horas) {
        if (!habilitado) {
            return false;
        }
        for (Reserva reserva : reservas) {
            if (reserva.ocupaFranja(fecha, horaInicio, horas)) {
                return false;
            }
        }
        return true;
    }

    public void agregarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public abstract String devolverTipo();

    // Cada subclase lo redefine agregando sus propios datos a los comunes.
    public String getDetalle() {
        return "Tipo: " + devolverTipo()
                + " | Código: " + codigo
                + " | Descripción: " + descripcion
                + " | Capacidad: " + capacidadMaxima
                + " | Precio por hora: $" + precioBaseHora
                + " | Habilitado: " + (habilitado ? "sí" : "no");
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public BigDecimal getPrecioBaseHora() {
        return precioBaseHora;
    }

    public boolean isHabilitado() {
        return habilitado;
    }
}
