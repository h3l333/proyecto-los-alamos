package clublosalamos;

import java.time.LocalDate;

/**
 * ============================================================================
 * CARTA CRC
 * ============================================================================
 * Nombre de la clase: Socio
 *
 * Responsabilidades:
 * 1. Guarda los datos personales y administrativos de un socio del club y
 *   los valida al crearse.
 * 2. Indica si el socio puede realizar reservas según su situación
 *   administrativa, y describe esa situación.
 * 3. Cambia de situación administrativa (suspender, marcar moroso, habilitar,
 *   dar de baja) delegando la validación de la transición en su situación
 *   actual.
 *
 * Colaboradores:
 * - SituacionAdmin: Socio le consulta si puede reservar y le delega las
 *   transiciones de situación administrativa.
 * - Categoria: Socio lo conoce como su categoría.
 * ============================================================================
 */
// Implementación pendiente de aprobación final del profesor en el diagrama de casos de uso.
public class Socio {
    private final int numeroSocio;
    private final String nombre;
    private final String apellido;
    private final String documento;
    private final String telefono;
    private final String correoElectronico;
    private final LocalDate fechaIngreso;
    private SituacionAdmin situacionAdministrativa;
    private final Categoria categoria;

    public Socio(int numeroSocio, String nombre, String apellido, String documento, String telefono,
                 String correoElectronico, LocalDate fechaIngreso, SituacionAdmin situacionAdministrativa,
                 Categoria categoria) {
        if (numeroSocio <= 0) {
            throw new IllegalArgumentException("El número de socio debe ser mayor a cero");
        }
        if (esVacio(nombre) || esVacio(apellido) || esVacio(documento)) {
            throw new IllegalArgumentException("El nombre, el apellido y el documento son obligatorios");
        }
        this.numeroSocio = numeroSocio;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.fechaIngreso = fechaIngreso;
        this.situacionAdministrativa = situacionAdministrativa;
        this.categoria = categoria;
    }

    private static boolean esVacio(String texto) {
        return texto == null || texto.isEmpty();
    }

    public boolean puedeReservar() {
        return situacionAdministrativa.puedeReservar();
    }

    public String describirSituacion() {
        return situacionAdministrativa.getSituacionAdmin();
    }

    public void suspender() {
        situacionAdministrativa = situacionAdministrativa.suspender();
    }

    public void marcarMoroso() {
        situacionAdministrativa = situacionAdministrativa.marcarMoroso();
    }

    public void habilitar() {
        situacionAdministrativa = situacionAdministrativa.habilitar();
    }

    public void darDeBaja() {
        situacionAdministrativa = situacionAdministrativa.darDeBaja();
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

    public Categoria getCategoria() {
        return categoria;
    }

    public SituacionAdmin getSituacionAdministrativa() {
        return situacionAdministrativa;
    }
}
