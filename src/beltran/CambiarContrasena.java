package beltran;

import beltran.Clases.ServicioLogin;
import javax.swing.*;
import java.awt.*;

public class CambiarContrasena extends JInternalFrame {
    private final ServicioLogin servicioLogin;
    private final String usuario;
    private final String nombreCompleto;

    public CambiarContrasena(String usuario, String nombreCompleto, ServicioLogin servicioLogin) {
        super("Cambiar Contraseña", true, true, true, true);
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.servicioLogin = servicioLogin;

        initComponents();
        lblUsuario.setText("Usuario: " + usuario);
        lblNombreCompleto.setText("Nombre Completo: " + nombreCompleto);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        configurarCierreVentana();
        setSize(400, 300);
        setLocation(50, 50);
    }

    private void initComponents() {
        txtContraseñaActual = new JPasswordField();
        txtContraseñaNueva = new JPasswordField();
        txtConfirmarContraseña = new JPasswordField();
        lblUsuario = new JLabel();
        lblNombreCompleto = new JLabel();
        btnCambiar = new JButton();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();

        // Estilo visual
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombreCompleto.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCambiar.setFont(new Font("Arial", Font.BOLD, 12));
        jLabel1.setFont(new Font("Arial", Font.PLAIN, 12));
        jLabel2.setFont(new Font("Arial", Font.PLAIN, 12));
        jLabel3.setFont(new Font("Arial", Font.PLAIN, 12));

        btnCambiar.setText("Confirmar cambio");
        btnCambiar.addActionListener(evt -> btnCambiarActionPerformed(evt));

        jLabel1.setText("Confirmar Contraseña:");
        jLabel2.setText("Contraseña Actual:");
        jLabel3.setText("Contraseña Nueva:");

        // Configuración del Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(lblUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblNombreCompleto, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(jLabel2, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        add(txtContraseñaActual, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(jLabel3, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        add(txtContraseñaNueva, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(jLabel1, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        add(txtConfirmarContraseña, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnCambiar, gbc);

        pack();
    }

    private void btnCambiarActionPerformed(java.awt.event.ActionEvent evt) {
        String contrasenaActual = new String(txtContraseñaActual.getPassword());
        String contrasenaNueva = new String(txtContraseñaNueva.getPassword());
        String confirmarContrasena = new String(txtConfirmarContraseña.getPassword());

        if (!contrasenaNueva.equals(confirmarContrasena)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas nuevas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean cambioExitoso = servicioLogin.cambiarContrasena(usuario, contrasenaActual, contrasenaNueva);

        if (cambioExitoso) {
            JOptionPane.showMessageDialog(this, "Contraseña cambiada exitosamente.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "La contraseña actual es incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarCierreVentana() {
        this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas cerrar sin guardar cambios?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose(); // Cierra la ventana
                }
            }
        });
    }

    public static void mostrarCambiarContrasena(String usuario, String nombreCompleto, ServicioLogin servicioLogin, JDesktopPane desktopPane) {
        CambiarContrasena ventana = new CambiarContrasena(usuario, nombreCompleto, servicioLogin);
        desktopPane.add(ventana);
        ventana.setVisible(true);
        ventana.toFront();
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnCambiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblNombreCompleto;
    private javax.swing.JPasswordField txtConfirmarContraseña;
    private javax.swing.JPasswordField txtContraseñaActual;
    private javax.swing.JPasswordField txtContraseñaNueva;
    // End of variables declaration
}
