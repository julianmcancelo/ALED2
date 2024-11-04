package beltran.Clases;

import java.sql.SQLException;

public class Profesor {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private int materiaId;
    private int carreraId; // ID de la carrera
    private String turno; // Turno del profesor

    public Profesor(String nombre, String apellido, String email, String telefono, int materiaId) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.materiaId = materiaId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getMateriaId() {
        return materiaId;
    }

    public int getCarreraId() {
        return carreraId; // Asegúrate de que se establezca correctamente
    }

    public void setCarreraId(int carreraId) {
        this.carreraId = carreraId;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
       public String getCarreraNombre() throws SQLException {
        // Assuming you have a CarreraDAO instance to retrieve the name
        CarreraDAO carreraDAO = new CarreraDAO();
        return carreraDAO.obtenerNombrePorId(this.carreraId);
    }
}

