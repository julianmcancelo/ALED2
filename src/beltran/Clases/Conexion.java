package beltran.Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    //private static final String URL = "jdbc:mysql://localhost:3306/beltran"; // Cambia 'tu_base_de_datos' por el nombre de tu base de datos
    //private static final String USER = "root"; // Cambia 'tu_usuario' por el nombre de usuario de la base de datos
    //private static final String PASSWORD = ""; // Cambia 'tu_contraseña' por la contraseña de la base de datos
    // Datos de conexión
    private static final String URL = "jdbc:mysql://190.106.131.13:3306/beltran?useSSL=false";


    private static final String USER = "beltran2024";
   private static final String PASSWORD = "feelthesky1";

    private static Connection connection;

    static {
        try {
            // Cargar el driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener la conexión
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    // Método para cerrar la conexión
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
