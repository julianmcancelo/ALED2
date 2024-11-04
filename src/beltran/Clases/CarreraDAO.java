package beltran.Clases;

import beltran.Clases.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarreraDAO {

    // Método para listar los nombres de todas las carreras
    public List<String> listarNombresCarreras() throws SQLException {
        List<String> carreras = new ArrayList<>();
        String query = "SELECT nombre FROM carreras";
        try (Connection connection = Conexion.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                carreras.add(rs.getString("nombre"));
            }
        }
        return carreras;
    }

    // Método para obtener el ID de una carrera por su nombre
    public int obtenerIdPorNombre(String nombre) throws SQLException {
        String query = "SELECT id FROM carreras WHERE nombre = ?";
        try (Connection connection = Conexion.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Carrera no encontrada: " + nombre);
    }

    // Método para obtener el nombre de una carrera por su ID
    public String obtenerNombrePorId(int id) throws SQLException {
        String query = "SELECT nombre FROM carreras WHERE id = ?";
        try (Connection connection = Conexion.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        }
        throw new SQLException("Carrera no encontrada con ID: " + id);
    }
}
