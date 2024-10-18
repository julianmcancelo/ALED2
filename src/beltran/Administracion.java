package beltran;

import beltran.Clases.ServicioLogin.SessionManager;
import com.formdev.flatlaf.FlatLightLaf; // Importar FlatLaf
import javax.swing.*;
import java.awt.*;

/**
 * Ventana de administración de la aplicación.
 */
public class Administracion extends javax.swing.JFrame {

    /**
     * Crea una nueva ventana de administración.
     */
    public Administracion() {
        initComponents();
        verificarSesion(); // Verifica la sesión al iniciar el frame
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    // Método para verificar si el usuario está logueado
    private void verificarSesion() {
        if (!SessionManager.isLoggedIn) {
            // Mensaje de advertencia si no está logueado
            JOptionPane.showMessageDialog(this,
                    "Debes iniciar sesión para acceder a esta sección.",
                    "Acceso Denegado",
                    JOptionPane.WARNING_MESSAGE);
            // Redirigir al inicio de sesión después de que el mensaje sea cerrado
            SwingUtilities.invokeLater(() -> {
                new InicioSesion().setVisible(true);
                this.dispose(); // Cerrar la ventana actual
            });
        }
    }

    /**
     * Este método es llamado desde dentro del constructor para inicializar el formulario.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        openRegistroAlumnos = new javax.swing.JButton();
        btnBuscador = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton(); // Nuevo botón para salir
        btnCerrarSesion = new javax.swing.JButton(); // Nuevo botón para cerrar sesión

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administración"); // Título de la ventana
        setLayout(new GridBagLayout()); // Usamos GridBagLayout para una mejor organización
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre botones
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los botones llenan el espacio horizontal

        // Configuración del botón "Registro Alumno"
        openRegistroAlumnos.setText("Registro Alumno");
        openRegistroAlumnos.addActionListener(evt -> openRegistroAlumnosActionPerformed(evt));

        // Configuración del botón "Buscar Alumno"
        btnBuscador.setText("Buscar Alumno");
        btnBuscador.addActionListener(evt -> btnBuscadorActionPerformed(evt));

        // Configuración del botón "Cerrar Sesión"
        btnCerrarSesion.setText("Cerrar Sesión");
        btnCerrarSesion.addActionListener(evt -> cerrarSesionActionPerformed(evt));

        // Configuración del botón "Salir"
        btnSalir.setText("Salir");
        btnSalir.addActionListener(evt -> salirActionPerformed(evt));

        // Agregamos los botones al layout
        gbc.gridx = 0; gbc.gridy = 0; getContentPane().add(openRegistroAlumnos, gbc);
        gbc.gridx = 0; gbc.gridy = 1; getContentPane().add(btnBuscador, gbc);
        gbc.gridx = 0; gbc.gridy = 2; getContentPane().add(btnCerrarSesion, gbc);
        gbc.gridx = 0; gbc.gridy = 3; getContentPane().add(btnSalir, gbc);

        pack(); // Ajusta el tamaño de la ventana
    }

    private void openRegistroAlumnosActionPerformed(java.awt.event.ActionEvent evt) {
        // Método para abrir la ventana RegistroAlumnos
        RegistroALumnos registro = new RegistroALumnos(this);
        registro.setVisible(true);
        this.setVisible(false); // Ocultar la ventana Administracion
    }

    private void btnBuscadorActionPerformed(java.awt.event.ActionEvent evt) {
        // Crear e inicializar el frame BuscadorAlumnos
        BuscadorAlumnos buscadorAlumnos = new BuscadorAlumnos();
        buscadorAlumnos.setVisible(true); // Mostrar el frame
    }

    private void cerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {
        // Método para cerrar sesión
        SessionManager.isLoggedIn = false; // Actualiza el estado de sesión
        JOptionPane.showMessageDialog(this, "Sesión cerrada exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        new InicioSesion().setVisible(true); // Redirige a inicio de sesión
        this.dispose(); // Cierra la ventana de administración
    }

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {
        // Método para salir de la aplicación
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro que deseas salir?", "Confirmar Salida",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0); // Cierra la aplicación
        }
    }

    public static void main(String args[]) {
        // Configurar FlatLaf como el look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> {
            // Solo se abrirá la ventana de administración si se está logueado
            if (SessionManager.isLoggedIn) {
                new Administracion().setVisible(true);
            } else {
                new InicioSesion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnBuscador;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton openRegistroAlumnos;
    // End of variables declaration
}
