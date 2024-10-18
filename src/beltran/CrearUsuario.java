package beltran;

import beltran.Clases.Conexion;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Clase que representa el formulario para crear un nuevo usuario.
 * Hereda de JFrame para hacer una ventana gráfica.
 */
public class CrearUsuario extends javax.swing.JFrame {

    private static final String ERROR_TITULO = "Error";
    private static final String EXITO_TITULO = "Éxito";
    private static final String CAMPO_VACIO_MENSAJE = "Todos los campos tienen que estar completados.";
    private static final String USUARIO_INVALIDO_MENSAJE = "El usuario debe tener al menos 3 caracteres.";
    private static final String CONTRASENA_INVALIDA_MENSAJE = "La contraseña debe tener mínimo 6 caracteres y al menos un número.";
    private static final String NOMBRE_INVALIDO_MENSAJE = "El nombre completo debe tener mínimo 3 caracteres.";
    private static final String DNI_INVALIDO_MENSAJE = "El DNI debe tener exactamente 8 dígitos.";
    private static final String LEGAJO_INVALIDO_MENSAJE = "El legajo tiene que ser un número y no puede estar vacío.";
    private static final String REGISTRO_EXITO_MENSAJE = "Usuario registrado exitosamente.";
    private static final String REGISTRO_FALLIDO_MENSAJE = "No se pudo registrar el usuario.";
    private static final String CONEXION_ERROR_MENSAJE = "Error en la conexión a la base de datos: ";

    public CrearUsuario() {
        initComponents();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Registro de Usuario");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(240, 240, 240)); // Color de fondo suave
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        btnGuardar = new javax.swing.JButton("Crear");
        btnCancelar = new javax.swing.JButton("Cancelar");
        jLabel1 = new JLabel("Usuario:");
        jLabel2 = new JLabel("Contraseña:");
        jLabel3 = new JLabel("Nombre Completo:");
        jLabel4 = new JLabel("DNI:");
        jLabel5 = new JLabel("Legajo:");
        jLabel6 = new JLabel("Permiso:");
        txtUsuario = new JTextField();
        txtContrasena = new JPasswordField();
        txtNombreCompleto = new JTextField();
        txtDni = new JTextField();
        txtLegajo = new JTextField();
        cmbPermisos = new JComboBox<>(new String[]{"Usuario", "Administrador"});

        // Configuración de botones
        btnGuardar.setBackground(new Color(76, 175, 80)); // Color verde
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(evt -> btnGuardarActionPerformed(evt));

        btnCancelar.setBackground(new Color(255, 82, 82)); // Color rojo
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(evt -> this.dispose());

        layoutComponents();
        pack();
    }

    private void layoutComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes

        gbc.gridx = 0; gbc.gridy = 0; panel.add(jLabel1, gbc);
        gbc.gridx = 1; panel.add(txtUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(jLabel2, gbc);
        gbc.gridx = 1; panel.add(txtContrasena, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(jLabel3, gbc);
        gbc.gridx = 1; panel.add(txtNombreCompleto, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panel.add(jLabel4, gbc);
        gbc.gridx = 1; panel.add(txtDni, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panel.add(jLabel5, gbc);
        gbc.gridx = 1; panel.add(txtLegajo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; panel.add(jLabel6, gbc);
        gbc.gridx = 1; panel.add(cmbPermisos, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10); // Espaciado adicional para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();
        String nombreCompleto = txtNombreCompleto.getText().trim();
        String dni = txtDni.getText().trim();
        String legajo = txtLegajo.getText().trim();
        String permisos = (String) cmbPermisos.getSelectedItem();

        // Validaciones de campos
        if (usuario.isEmpty() || contrasena.isEmpty() || nombreCompleto.isEmpty() || dni.isEmpty() || legajo.isEmpty()) {
            JOptionPane.showMessageDialog(this, CAMPO_VACIO_MENSAJE, ERROR_TITULO, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (usuario.length() < 3) {
            JOptionPane.showMessageDialog(this, USUARIO_INVALIDO_MENSAJE, ERROR_TITULO, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isContraseñaValida(contrasena)) {
            JOptionPane.showMessageDialog(this, CONTRASENA_INVALIDA_MENSAJE, ERROR_TITULO, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (nombreCompleto.length() < 3) {
            JOptionPane.showMessageDialog(this, NOMBRE_INVALIDO_MENSAJE, ERROR_TITULO, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isDniValido(dni)) {
            JOptionPane.showMessageDialog(this, DNI_INVALIDO_MENSAJE, ERROR_TITULO, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isLegajoValido(legajo)) {
            JOptionPane.showMessageDialog(this, LEGAJO_INVALIDO_MENSAJE, ERROR_TITULO, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // SQL para registrar el nuevo usuario
        String sql = "INSERT INTO usuarios (usuario, contrasena, nombre_completo, dni, legajo, permisos) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ps.setString(3, nombreCompleto);
            ps.setString(4, dni);
            ps.setString(5, legajo);
            ps.setString(6, permisos);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, REGISTRO_EXITO_MENSAJE, EXITO_TITULO, JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, REGISTRO_FALLIDO_MENSAJE, ERROR_TITULO, JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Error de entrada duplicada
                JOptionPane.showMessageDialog(this, "Error: El usuario o el DNI ya están registrados.", ERROR_TITULO, JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, CONEXION_ERROR_MENSAJE + e.getMessage(), ERROR_TITULO, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean isContraseñaValida(String contrasena) {
        return contrasena.length() >= 6 && Pattern.compile("[0-9]").matcher(contrasena).find();
    }

    private boolean isDniValido(String dni) {
        return dni.matches("\\d{8}"); // Verifica que tenga exactamente 8 dígitos
    }

    private boolean isLegajoValido(String legajo) {
        return legajo.matches("\\d+"); // Verifica que sea solo dígitos
    }

    private void limpiarCampos() {
        txtUsuario.setText("");
        txtContrasena.setText("");
        txtNombreCompleto.setText("");
        txtDni.setText("");
        txtLegajo.setText("");
        cmbPermisos.setSelectedIndex(0); // Resetea el combo de permisos
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new CrearUsuario().setVisible(true));
    }

    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<String> cmbPermisos;
    private javax.swing.JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6;
    private javax.swing.JTextField txtUsuario, txtNombreCompleto, txtDni, txtLegajo;
    private javax.swing.JPasswordField txtContrasena;
}
