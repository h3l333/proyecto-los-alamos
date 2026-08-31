package clublosalamos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private final String numeroReserva;
    private final LocalDateTime fechaCreacion;
    private final Socio socio;
    private final Recurso recurso;
    private final LocalDate fechaUso;
    private final LocalTime horarioInicio;
    private final int duracionHoras;
    private final double importeTotal;
    private final List<Pago> pagos = new ArrayList<>();
    private double importeAbonado;
    private double saldoPendiente;
    private String estado;

    public Reserva(String numeroReserva, Socio socio, Recurso recurso, LocalDate fechaUso, LocalTime horarioInicio,
                    int duracionHoras, Pago pagoInicial) {
        this.numeroReserva = numeroReserva;
        this.fechaCreacion = LocalDateTime.now();
        this.socio = socio;
        this.recurso = recurso;
        this.fechaUso = fechaUso;
        this.horarioInicio = horarioInicio;
        this.duracionHoras = duracionHoras;
        this.importeTotal = calcularImporteTotal();
        this.estado = "pagoparcial";
        registrarPago(pagoInicial);
    }

    public double calcularImporteTotal() {
        return recurso.getPrecioBaseHora() * duracionHoras;
    }

    public void registrarPago(Pago pago) {
        pagos.add(pago);
        importeAbonado += pago.getMonto();
        saldoPendiente = importeTotal - importeAbonado;
    }

    public void confirmar() {
        if (estado.equals("pagoparcial") && saldoPendiente <= 0) {
            estado = "confirmada";
        }
    }

    public void cancelar() {
        if (estado.equals("pagoparcial") || estado.equals("confirmada")) {
            estado = "cancelada";
        }
    }

    public void finalizar() {
        if (estado.equals("confirmada")) {
            estado = "finalizada";
        }
    }

    public String getNumeroReserva() {
        return numeroReserva;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Socio getSocio() {
        return socio;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public LocalDate getFechaUso() {
        return fechaUso;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public int getDuracionHoras() {
        return duracionHoras;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public double getImporteAbonado() {
        return importeAbonado;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public String getEstado() {
        return estado;
    }
}
