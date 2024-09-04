package Materias;

public class Alumno {
    private int id;
    private String nombreCompleto;
    private String dni;
    private String legajo;
    private boolean estado;

    public Alumno(int id, String nombreCompleto, String dni, String legajo, boolean estado) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.legajo = legajo;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public String getLegajo() {
        return legajo;
    }

    public boolean getEstado() {
        return estado;
    }
}
