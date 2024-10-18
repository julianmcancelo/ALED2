package beltran;

import beltran.Clases.ServicioLogin;
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
