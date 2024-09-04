package beltran.Clases;

import beltran.Administracion;
import beltran.InicioSesion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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


    private static final String USER = "beltran2024";
   private static final String PASSWORD = "feelthesky1";
    /**
     * Autentica al usuario en la base de datos.
     * @param usuario Nombre del usuario.
     * @param contrasena Contraseña del usuario.
     * @return Resultado de autenticación que incluye el nombre completo y el rol del usuario si la autenticación es exitosa; null en caso contrario.
     */
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
            e.printStackTrace(); // Puedes mejorarlo mostrando un mensaje más amigable
        }
        return null;
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
        // Aquí podrías redirigir al usuario a la ventana de inicio de sesión nuevamente
    }
}
public class SessionManager {
    // Variable estática para indicar si el usuario ha iniciado sesión
    public static boolean isLoggedIn = false;
}
}
