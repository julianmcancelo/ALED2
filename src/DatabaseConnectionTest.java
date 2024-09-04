import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    // Datos de conexión
    private static final String URL = "jdbc:mysql://mysql8001.site4now.net/db_aac56f_beltran?useSSL=false";

    private static final String USER = "aac56f_beltran";
    private static final String PASSWORD = "35230531Aa";

    public static void main(String[] args) {
        Connection connection = null;

        try {
            // Intentar establecer la conexión
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa!");

        } catch (SQLException e) {
            // Manejar errores de conexión
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();

        } finally {
            // Cerrar la conexión si está abierta
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
