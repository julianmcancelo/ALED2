package beltran;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;

/**
 * Clase principal para la ventana de inicio de sesión.
 */
public class InicioSesion extends JFrame {
    private final ServicioLogin servicioLogin;

    /**
     * Constructor de la clase InicioSesion.
     */
    public InicioSesion() {
        servicioLogin = new ServicioLogin(); // Inicializa el servicio de login
        initComponents(); // Inicializa los componentes de la ventana
        centrarVentana(); // Centra la ventana
    }

    /**
     * Inicializa los componentes de la ventana.
     */
    private void initComponents() {
        LoginPanel loginPanel = new LoginPanel(servicioLogin, this); // Crea el panel de inicio de sesión
        add(loginPanel); // Agrega el panel a la ventana
        setTitle("Inicio de Sesión"); // Título de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana
        pack(); // Ajusta el tamaño de la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    /**
     * Método para centrar la ventana en la pantalla.
     */
    private void centrarVentana() {
        setLocationRelativeTo(null); // Centra la ventana
    }

    /**
     * Método para verificar si la cuenta del usuario está validada.
     * Si la cuenta no está validada, muestra un mensaje de error.
     * @param usuario Nombre del usuario.
     * @param contrasena Contraseña del usuario.
     */
  public void verificarCuenta(String usuario, String contrasena) {
    System.out.println("Verificando cuenta para el usuario: " + usuario);  // Depuración
    boolean cuentaValida = servicioLogin.verificarCuentaValida(usuario);

    if (!cuentaValida) {
        // Si la cuenta no está validada, mostrar mensaje de error
        JOptionPane.showMessageDialog(this, 
                "La cuenta de usuario no está validada. Por favor, valide su cuenta primero.", 
                "Cuenta no validada", 
                JOptionPane.ERROR_MESSAGE);
    } else {
        // Si la cuenta está validada, proceder con el inicio de sesión
        System.out.println("Cuenta validada. Verificando credenciales...");
        ServicioLogin.ResultadoAutenticacion resultado = servicioLogin.autenticar(usuario, contrasena);
        if (resultado != null) {
            // Si las credenciales son correctas
            JOptionPane.showMessageDialog(this, 
                    "Bienvenido, " + resultado.getNombreCompleto() + "!", 
                    "Inicio de sesión exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);
            // Aquí puedes redirigir a otra ventana si el inicio de sesión es exitoso
            new Administracion().setVisible(true); // Cambia según tu flujo de la aplicación
            this.setVisible(false); // Cierra la ventana de inicio de sesión
        } else {
            // Si las credenciales son incorrectas
            JOptionPane.showMessageDialog(this, 
                    "Usuario o contraseña incorrectos.", 
                    "Error de autenticación", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}   

    /**
     * Método principal para ejecutar la aplicación.
     */
    public static void main(String[] args) {
        // Configurar el tema FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, "Error al configurar el tema: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        SwingUtilities.invokeLater(() -> new InicioSesion().setVisible(true)); // Muestra la ventana de inicio de sesión
    }
}
