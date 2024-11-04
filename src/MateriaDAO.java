import beltran.Clases.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {
    public List<String> listarNombresMaterias() throws SQLException {
        List<String> materias = new ArrayList<>();
        String query = "SELECT nombre FROM materias";
        try (Connection connection = Conexion.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                materias.add(rs.getString("nombre"));
            }
        }
        return materias;
    }

    public int obtenerIdPorNombre(String nombre) throws SQLException {
        String query = "SELECT id FROM materias WHERE nombre = ?";
        try (Connection connection = Conexion.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Materia no encontrada: " + nombre);
    }
}
