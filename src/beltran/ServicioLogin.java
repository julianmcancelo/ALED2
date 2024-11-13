package beltran;

import beltran.Usuario;
import beltran.Administracion;
import static beltran.ServicioLogin.RolUsuario.obtenerConexion;
import beltran.InicioSesion;
import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


/**
 * Servicio para manejar la autenticación de usuarios.
 */
public class ServicioLogin {
    // Variable estática que indica si el usuario ha iniciado sesión
    public static boolean isLoggedIn = false;
    //private static final String URL = "jdbc:mysql://localhost:3306/beltran";
    //private static final String USER = "root";
    //private static final String PASSWORD = ""; // Cambia esto por tu contraseña
    private static final String URL = "jdbc:mysql://190.106.131.13:3306/beltran?useSSL=false";

  public static List<Usuario> obtenerUsuarios() {
    List<Usuario> usuarios = new ArrayList<>();
    try (Connection con = obtenerConexion()) {
        String sql = "SELECT id, usuario, nombre_completo, permisos, validado, email FROM usuarios";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int id = rs.getInt("id");
            String usuario = rs.getString("usuario");
            String nombreCompleto = rs.getString("nombre_completo");
            String permisos = rs.getString("permisos");
            boolean validado = rs.getBoolean("validado");
            String email = rs.getString("email");
            
            Usuario u = new Usuario(id, usuario, nombreCompleto, permisos, email);
            usuarios.add(u);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return usuarios;
}

      // Método para eliminar un usuario (usando un objeto Usuario)
// Método para eliminar un usuario (usando un objeto Usuario)
public static boolean eliminarUsuario(Usuario usuario) {
    String sql = "DELETE FROM usuarios WHERE id = ?";  // La columna en la tabla es 'id', no 'id_usuario'
    
    try (Connection con = obtenerConexion();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        // Usamos el ID del usuario para eliminarlo
        stmt.setInt(1, usuario.getId());  // Usamos el id del objeto Usuario
        
        // Ejecutar la actualización
        int rowsAffected = stmt.executeUpdate();
        
        // Si se afectaron filas, significa que el usuario fue eliminado correctamente
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;  // Si ocurre un error, retornamos false
    }
}



 public String usuario;
    public String legajo;
    public String NuevaContrasena;

    private static final String USER = "beltran2024";
   private static final String PASSWORD = "feelthesky1";
    /**
     * Autentica al usuario en la base de datos y verifica si la cuenta está validada.
     * @param usuario Nombre del usuario.
     * @param contrasena Contraseña del usuario.
     * @return Resultado de autenticación que incluye el nombre completo y el rol del usuario si la autenticación es exitosa; null en caso contrario.
     */
     // Método para autenticar al usuario
    public ResultadoAutenticacion autenticar(String usuario, String contrasena) {
        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String consulta = "SELECT nombre_completo, permisos FROM usuarios WHERE usuario = ? AND contrasena = ?";
            try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
                statement.setString(1, usuario);
                statement.setString(2, contrasena);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String nombreCompleto = resultSet.getString("nombre_completo");
                        String rol = resultSet.getString("permisos");
                        return new ResultadoAutenticacion(nombreCompleto, RolUsuario.fromString(rol));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no encuentra el usuario o hay un error, retorna null
    }
    /**
     * Cambia la contraseña del usuario.
     * @param usuario Nombre del usuario.
     * @param contrasenaActual La contraseña actual del usuario.
     * @param contrasenaNueva La nueva contraseña que se quiere establecer.
     * @return true si el cambio fue exitoso, false en caso contrario.
     */
    public boolean cambiarContrasena(String usuario, String contrasenaActual, String contrasenaNueva) {
        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Verificar la contraseña actual
            if (verificarContrasenaActual(conexion, usuario, contrasenaActual)) {
                // Actualizar la contraseña
                String updateQuery = "UPDATE usuarios SET contrasena = ? WHERE usuario = ?";
                try (PreparedStatement statement = conexion.prepareStatement(updateQuery)) {
                    statement.setString(1, contrasenaNueva);
                    statement.setString(2, usuario);
                    int filasActualizadas = statement.executeUpdate();
                    return filasActualizadas > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes mejorarlo mostrando un mensaje más amigable
        }
        return false;
    }

    /**
     * Verifica si la contraseña actual es correcta.
     * @param conexion Conexión a la base de datos.
     * @param usuario Nombre del usuario.
     * @param contrasenaActual La contraseña actual del usuario.
     * @return true si la contraseña es correcta, false en caso contrario.
     */
    private boolean verificarContrasenaActual(Connection conexion, String usuario, String contrasenaActual) throws SQLException {
        String query = "SELECT 1 FROM usuarios WHERE usuario = ? AND contrasena = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, usuario);
            statement.setString(2, contrasenaActual);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean recuperarContrasena(String usuario, String legajo, String nuevaContrasena) {
        String sqlVerificar = "SELECT legajo FROM usuarios WHERE usuario = ?";
        String sqlActualizar = "UPDATE usuarios SET contrasena = ? WHERE usuario = ?";

        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmtVerificar = conexion.prepareStatement(sqlVerificar);
             PreparedStatement pstmtActualizar = conexion.prepareStatement(sqlActualizar)) {

            // Verificar si el usuario existe y obtener el legajo
            pstmtVerificar.setString(1, usuario);
            try (ResultSet rs = pstmtVerificar.executeQuery()) {
                if (rs.next()) {
                    String legajoEnBD = rs.getString("legajo");
                    // Comprobar si el legajo es correcto
                    if (legajoEnBD.equals(legajo)) {
                        // Actualizar la contraseña
                        pstmtActualizar.setString(1, nuevaContrasena); // Aquí deberías aplicar hashing
                        pstmtActualizar.setString(2, usuario);
                        pstmtActualizar.executeUpdate();
                        return true; // Contraseña actualizada correctamente
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepciones
        }

        return false; // Usuario o legajo incorrectos
    }    

 
  // Método en ServicioLogin para verificar si la cuenta está validada
public boolean verificarCuentaValida(String usuario) {
    try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD)) {
        // Consulta para obtener el campo 'validado' del usuario
        String query = "SELECT validado FROM usuarios WHERE usuario = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, usuario);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int validado = resultSet.getInt("validado");  // Obtenemos el valor de 'validado'
                    return validado == 1;  // Si validado es 1, la cuenta está validada
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error en la verificación de la cuenta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return false; // Si no se encuentra el usuario o el campo validado es diferente de 1
}

  public boolean verificarUsuarioExistente(String usuario) {
        boolean existe = false;
        // Conexión y consulta a la base de datos
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ?"; // Cambia el nombre de la tabla y la columna según tu base de datos
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usuario); // Establece el nombre de usuario como parámetro
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Si el conteo es mayor que 0, significa que el usuario existe
                        existe = rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // En producción, usa un sistema de logging en lugar de printStackTrace
        }
        return existe;
    }
    
 /**
     * Método para verificar si la cuenta del usuario está validada.
     * @param usuario Nombre del usuario.
     * @return true si la cuenta está validada, false en caso contrario.
     */
    /**
     * Clase interna para encapsular el resultado de la autenticación.
     */
      public static class ResultadoAutenticacion {
        private final String nombreCompleto;
        private final RolUsuario rolUsuario;

        public ResultadoAutenticacion(String nombreCompleto, RolUsuario rolUsuario) {
            this.nombreCompleto = nombreCompleto;
            this.rolUsuario = rolUsuario;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public RolUsuario getRolUsuario() {
            return rolUsuario;
        }
    }
    /**
     * Enum para los roles de usuario.
     */
     public enum RolUsuario {
        ADMINISTRADOR, USUARIO;

        public static RolUsuario fromString(String rol) {
            switch (rol) {
                case "Administrador" -> {
                    return ADMINISTRADOR;
                }
                case "Usuario" -> {
                    return USUARIO;
                }
                default -> throw new IllegalArgumentException("Rol desconocido: " + rol);
            }
        }
  
 
 public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

public static List<Usuario> obtenerUsuarios() {
    List<Usuario> usuarios = new ArrayList<>();
    try (Connection con = obtenerConexion()) {
        String sql = "SELECT id, usuario, nombre_completo, permisos, validado, email FROM usuarios";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int id = rs.getInt("id");
            String usuario = rs.getString("usuario");
            String nombreCompleto = rs.getString("nombre_completo");
            String permisos = rs.getString("permisos");
            boolean validado = rs.getBoolean("validado");
            String email = rs.getString("email");
            
            Usuario u = new Usuario(id, usuario, nombreCompleto, permisos, email);
            usuarios.add(u);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return usuarios;
}


    // Método para eliminar un usuario (usando un objeto Usuario)
    public static boolean eliminarUsuario(Usuario usuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        try (Connection con = obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            // Usamos el ID del usuario para eliminarlo
            stmt.setInt(1, usuario.getId());  // Usamos el id del objeto Usuario
            
            // Ejecutar la actualización
            int rowsAffected = stmt.executeUpdate();
            
            // Si se afectaron filas, significa que el usuario fue eliminado correctamente
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Si ocurre un error, retornamos false
        }
    }


    // Método para iniciar sesión (deberías tener una lógica para autenticar al usuario)
    public static void iniciarSesion() {
        // Aquí deberías implementar la lógica para autenticar al usuario
        // Si la autenticación es exitosa:
        isLoggedIn = true;
        // Luego, puedes mostrar la ventana de administración o cualquier otra ventana
        new Administracion().setVisible(true);
    }

    // Método para cerrar sesión
    public static void cerrarSesion() {
        isLoggedIn = false;
    }
}

}