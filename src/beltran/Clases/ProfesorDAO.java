package beltran.Clases;

import beltran.Clases.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {
    public void agregarProfesor(Profesor profesor) throws SQLException {
        String sql = "INSERT INTO profesores (nombre, apellido, email, telefono, materia_id, carrera_id, turno) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getApellido());
            ps.setString(3, profesor.getEmail());
            ps.setString(4, profesor.getTelefono());
            ps.setInt(5, profesor.getMateriaId());
            ps.setInt(6, profesor.getCarreraId()); // Guardar carrera
            ps.setString(7, profesor.getTurno()); // Guardar turno
            ps.executeUpdate();
        }
    }
    

    public List<Profesor> listarProfesores() throws SQLException {
        List<Profesor> profesores = new ArrayList<>();
        String sql = "SELECT * FROM profesores"; // Aquí puedes modificar la consulta para unirte a otras tablas y obtener más información
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Profesor profesor = new Profesor(
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getInt("materia_id")
                );
                profesor.setCarreraId(rs.getInt("carrera_id")); // Obtener carrera
                profesor.setTurno(rs.getString("turno")); // Obtener turno
                profesores.add(profesor);
            }
        }
        return profesores;
    }
}
