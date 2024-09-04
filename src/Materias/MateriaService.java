package Materias;

import beltran.Clases.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaService {

public String[] obtenerCarreras() throws SQLException {
    try (var conn = Conexion.getConnection()) {
        String sql = "SELECT nombre FROM carreras";
        try (PreparedStatement ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
}


    // Obtiene el ID de la carrera por nombre
public String[] obtenerMaterias(String carrera) throws SQLException {
    String query = "SELECT nombre FROM materias WHERE carrera = ?";
    try (Connection conn = Conexion.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, carrera);
        try (ResultSet rs = stmt.executeQuery()) {
            List<String> materias = new ArrayList<>();
            while (rs.next()) {
                materias.add(rs.getString("nombre")); // Asegúrate de que "nombre" es el nombre correcto de la columna
            }
            return materias.toArray(new String[0]);
        }
    }
}



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
private int obtenerCarreraId(String carrera) throws SQLException {
    try (var conn = Conexion.getConnection()) {
        String sql = "SELECT id FROM carreras WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
}

    // Registra una nueva materia
public void registrarMateria(String carrera, Integer ano, String materia) throws SQLException {
    int carreraId = obtenerCarreraId(carrera);
    try (var conn = Conexion.getConnection()) {
        String sql = "INSERT INTO materias (carrera_id, ano, materia) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carreraId); // Carrera ID
            ps.setInt(2, ano);       // Año
            ps.setString(3, materia); // Materia
            ps.executeUpdate();
        }
    }
}

    // Obtiene alumnos según carrera, materia y año
// Obtiene alumnos según carrera y año
public String[] obtenerAlumnos(String carrera, Integer ano) throws SQLException {
    String query = "SELECT id, nombres, apellido, dni, legajo, ano FROM alumnos WHERE carrera = ? AND ano = ?";
    try (Connection conn = Conexion.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, carrera);
        stmt.setInt(2, ano);

        try (ResultSet rs = stmt.executeQuery()) {
            List<String> alumnos = new ArrayList<>();
            while (rs.next()) {
                String alumno = rs.getString("id") + "," +
                                rs.getString("nombres") + "," +
                                rs.getString("apellido") + "," +
                                rs.getString("dni") + "," +
                                rs.getString("legajo") + "," +
                                rs.getInt("ano"); // Incluye el campo 'ano'
                alumnos.add(alumno);
            }
            return alumnos.toArray(new String[0]);
        }
    }
}








private int obtenerMateriaId(String materia, int carreraId, int ano) throws SQLException {
    try (var conn = Conexion.getConnection()) {
        String sql = "SELECT id FROM materias WHERE materia = ? AND carrera_id = ? AND ano = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, materia);
            ps.setInt(2, carreraId);
            ps.setInt(3, ano);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Materia no encontrada: " + materia);
                }
            }
        }
    }
}


    // Registra la asistencia
public void registrarAsistencia(String alumnoId, String carreraNombre, String materiaNombre, Integer ano, String turno, java.sql.Date fecha, boolean presente) throws SQLException {
    int carreraId = obtenerCarreraId(carreraNombre);
    int materiaId = obtenerMateriaId(materiaNombre, carreraId, ano);

    try (var conn = Conexion.getConnection()) {
        String sql = "INSERT INTO asistencia (alumno_id, carrera_id, materia_id, carrera, materia, ano, turno, fecha, presente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, alumnoId);
            ps.setInt(2, carreraId);       // ID de la carrera
            ps.setInt(3, materiaId);       // ID de la materia
            ps.setString(4, carreraNombre); // Nombre de la carrera
            ps.setString(5, materiaNombre); // Nombre de la materia
            ps.setInt(6, ano);
            ps.setString(7, turno);
            ps.setDate(8, fecha);
            ps.setBoolean(9, presente);
            ps.executeUpdate();
        }
    }

}
}
