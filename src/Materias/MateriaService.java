package Materias;

import beltran.Clases.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaService {

    // Obtiene todas las carreras
    public String[] obtenerCarreras() throws SQLException {
        try (var conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM carreras", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = ps.executeQuery()) {

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            String[] carreras = new String[rowCount];
            int index = 0;
            while (rs.next()) {
                carreras[index++] = rs.getString("nombre");
            }
            return carreras;
        }
    }

    // Obtiene las materias para una carrera
    public String[] obtenerMaterias(String carrera) throws SQLException {
        String query = "SELECT nombre FROM materias WHERE carrera = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carrera);
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> materias = new ArrayList<>();
                while (rs.next()) {
                    materias.add(rs.getString("nombre"));
                }
                return materias.toArray(new String[0]);
            }
        }
    }

    // Verifica datos en la tabla alumnos
    public boolean verificarDatos(String carrera, Integer ano, String materia) throws SQLException {
        String query = "SELECT COUNT(*) FROM alumnos WHERE carrera = ? AND ano = ? AND materia = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carrera);
            stmt.setInt(2, ano);
            stmt.setString(3, materia);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Obtiene el ID de la carrera por nombre
    public int obtenerCarreraId(String carrera) throws SQLException {
        try (var conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM carreras WHERE nombre = ?")) {
            ps.setString(1, carrera);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Carrera no encontrada: " + carrera);
                }
            }
        }
    }

    // Registra una nueva materia
    public void registrarMateria(String carrera, Integer ano, String materia) throws SQLException {
        int carreraId = obtenerCarreraId(carrera);
        try (var conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO materias (carrera_id, carrera, ano, materia, nombre) VALUES (?, ?, ?, ?, ?)")) {
            ps.setInt(1, carreraId);
            ps.setString(2, carrera);
            ps.setInt(3, ano);
            ps.setString(4, materia);
            ps.setString(5, materia);
            ps.executeUpdate();
        }
    }

    // Obtiene alumnos según carrera, año, comisión y turno (sin filtrar por materia)
    public String[] obtenerAlumnos(String carrera, Integer ano, String comision, String turno) throws SQLException {
        // Implementar la consulta SQL para filtrar por turno y comisión, sin filtrar por materia
        String sql = "SELECT id, nombres, apellido, dni, legajo, ano, comision FROM alumnos WHERE carrera = ? AND ano = ? AND comision = ? AND turno = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, carrera);
            ps.setInt(2, ano);
            ps.setString(3, comision);
            ps.setString(4, turno);
            
            try (ResultSet rs = ps.executeQuery()) {
                List<String> alumnos = new ArrayList<>();
                while (rs.next()) {
                    alumnos.add(String.format("%s,%s,%s,%s,%s,%s,%s",
                            rs.getString("id"),
                            rs.getString("nombres"),
                            rs.getString("apellido"),
                            rs.getString("dni"),
                            rs.getString("legajo"),
                            rs.getString("ano"),
                            rs.getString("comision")));
                }
                return alumnos.toArray(new String[0]);
            }
        }
    }

    // Registra la asistencia
    public void registrarAsistencia(String alumnoId, String carrera, String materia, Integer ano, String turno, java.sql.Date fecha, String comision, boolean presente) throws SQLException {
        String sql = "INSERT INTO asistencia (alumno_id, carrera, materia, ano, turno, fecha, comision, presente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, alumnoId);
            ps.setString(2, carrera);
            ps.setString(3, materia);
            ps.setInt(4, ano);
            ps.setString(5, turno);
            ps.setDate(6, fecha);
            ps.setString(7, comision);
            ps.setBoolean(8, presente);
            
            ps.executeUpdate();
        }
    }

    // Obtiene las comisiones para una carrera y año específicos
    public String[] obtenerComisiones(String carrera, Integer ano) throws SQLException {
        String query = "SELECT DISTINCT comision FROM comision WHERE carrera = ? AND ano = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carrera);
            stmt.setInt(2, ano);
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> comisiones = new ArrayList<>();
                while (rs.next()) {
                    comisiones.add(rs.getString("comision"));
                }
                return comisiones.toArray(new String[0]);
            }
        }
    }
}
