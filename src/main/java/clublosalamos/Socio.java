package clublosalamos;

import java.time.LocalDate;

// Implementación pendiente de aprobación final del profesor en el diagrama de casos de uso.
public abstract class Socio {
    private final int numeroSocio;
    private final String nombre;
    private final String apellido;
    private final String documento;
    private final String telefono;
    private final String correoElectronico;
    private final LocalDate fechaIngreso;
    private final String situacionAdministrativa;

    public Socio(int numeroSocio, String nombre, String apellido, String documento, String telefono,
                     String correoElectronico, LocalDate fechaIngreso, String situacionAdministrativa) {
        this.numeroSocio = numeroSocio;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.fechaIngreso = fechaIngreso;
        this.situacionAdministrativa = situacionAdministrativa;
    }

    public boolean puedeReservar() {
        return situacionAdministrativa.equals("habilitado");
    }

    public int getNumeroSocio() {
        return numeroSocio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public String getSituacionAdministrativa() {
        return situacionAdministrativa;
    }
}
