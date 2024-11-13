package beltran;

import beltran.Clases.Conexion;
import javax.swing.*;
import java.awt.*;
import javax.mail.PasswordAuthentication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.UUID;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

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
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(240, 240, 240));
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
        jLabel7 = new JLabel("Correo Electrónico:"); // Nuevo JLabel
        txtUsuario = new JTextField();
        txtContrasena = new JPasswordField();
        txtNombreCompleto = new JTextField();
        txtDni = new JTextField();
        txtLegajo = new JTextField();
        txtEmail = new JTextField(); // Nuevo JTextField
        cmbPermisos = new JComboBox<>(new String[]{"Usuario", "Administrador"});

        // Estilo de botones
        btnGuardar.setBackground(new Color(76, 175, 80));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(evt -> btnGuardarActionPerformed(evt));

        btnCancelar.setBackground(new Color(255, 82, 82));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(evt -> this.dispose());

        // Panel principal con borde
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Crear Nuevo Usuario"));
        panel.setBackground(new Color(240, 240, 240));
        layoutComponents(panel);

        add(panel);
        pack();
    }

    private void layoutComponents(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

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

        gbc.gridx = 0; gbc.gridy = 6; panel.add(jLabel7, gbc); // Agregar correo electrónico
        gbc.gridx = 1; panel.add(txtEmail, gbc); // Campo para el correo

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; 
        gbc.insets = new Insets(20, 10, 10, 10); // Espaciado adicional para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        panel.add(buttonPanel, gbc);
    }

   private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
    String usuario = txtUsuario.getText().trim();
    String contrasena = new String(txtContrasena.getPassword()).trim();
    String nombreCompleto = txtNombreCompleto.getText().trim();
    String dni = txtDni.getText().trim();
    String legajo = txtLegajo.getText().trim();
    String permisos = (String) cmbPermisos.getSelectedItem();
    String email = txtEmail.getText().trim(); // Obtener el correo electrónico

    // Validaciones de campos
    if (usuario.isEmpty() || contrasena.isEmpty() || nombreCompleto.isEmpty() || dni.isEmpty() || legajo.isEmpty() || email.isEmpty()) {
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
    if (!isEmailValido(email)) {
        JOptionPane.showMessageDialog(this, "El correo electrónico no es válido.", ERROR_TITULO, JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Generar un token único para la validación
    String tokenValidacion = generarTokenValidacion();

    // SQL para registrar el nuevo usuario (con el token de validación y el correo electrónico)
    String sql = "INSERT INTO usuarios (usuario, contrasena, nombre_completo, dni, legajo, permisos, email, token_validacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = Conexion.getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, usuario);
        ps.setString(2, contrasena);
        ps.setString(3, nombreCompleto);
        ps.setString(4, dni);
        ps.setString(5, legajo);
        ps.setString(6, permisos);
        ps.setString(7, email); // Agregar el correo electrónico al registro
        ps.setString(8, tokenValidacion); // Agregar el token de validación

        int filasAfectadas = ps.executeUpdate();
        if (filasAfectadas > 0) {
            // Enviar el correo con el enlace de validación
            boolean correoEnviado = enviarCorreoDeValidacion(email, tokenValidacion);

            if (correoEnviado) {
                JOptionPane.showMessageDialog(this, 
                    "Usuario registrado exitosamente. Se ha enviado un correo electrónico de validación. Por favor, revisa tu bandeja de entrada.",
                    EXITO_TITULO, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Usuario registrado exitosamente, pero no se pudo enviar el correo de validación. Por favor, intenta más tarde.",
                    EXITO_TITULO, JOptionPane.WARNING_MESSAGE);
            }
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

    public static String generarTokenValidacion() {
        // Usamos UUID para generar un identificador único
        UUID uuid = UUID.randomUUID();
        return uuid.toString(); // Retorna el UUID como un string
    }

  private boolean enviarCorreoDeValidacion(String correo, String token) {
    String asunto = "Verificación de Correo Electrónico - ¡Bienvenido a [Aplicación para Examen Final ALED 2 - Instituto Beltrán]!";
    String mensaje = "<html>"
            + "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6; background-color: #f4f4f4; padding: 20px;'>"
            // Se cambia la imagen de fondo por un gris claro (#f4f4f4)
            + "<div style='background-color: #ffffff; color: #333333; padding: 20px; text-align: center; border-radius: 10px;'>"
            // Logo agrandado
            + "<img src='https://ibeltran.com.ar/img/logo/logo1.png' alt='Logo' style='max-width: 300px; height: auto; margin-bottom: 15px; filter: brightness(0) invert(1);'>"
            + "</div>"
            + "<div style='background-color: rgba(255, 255, 255, 0.9); padding: 20px; border-radius: 8px; margin-top: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>"
            + "<h2 style='color: #4CAF50;'>¡Gracias por registrarte en el Examen Final de ALED 2!</h2>"
            + "<p>¡Bienvenido! Hemos recibido una solicitud para crear una cuenta con tu dirección de correo electrónico en el sistema de Examen Final de Algoritmos.</p>"
            + "<p>Para poder continuar con el proceso, es necesario que verifiques tu cuenta haciendo clic en el siguiente enlace:</p>"
            + "<p style='text-align: center;'>"
            + "<a href='http://www.jmcancelo.xyz/validar2.php?token=" + token + "' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Verificar mi cuenta</a>"
            + "</p>"
            + "<p style='font-size: 12px;'>Este enlace de verificación será válido solo por <strong>24 horas</strong>. Si no solicitaste este registro, por favor ignora este mensaje.</p>"
            + "<hr>"
            + "<p style='font-size: 14px;'>Si tienes algún problema o necesitas asistencia adicional con la verificación, no dudes en contactar a nuestro equipo de soporte:</p>"
            + "<p style='font-size: 12px;'>"
            + "📧 <strong>Soporte:</strong> <a href='mailto:soporte@jmcancelo.xyz' style='color: #4CAF50;'>soporte@jmcancelo.xyz</a><br>"
            + "</p>"
            + "</div>"
            // Footer con fondo negro y texto blanco
            + "<div style='font-size: 12px; color: white; text-align: center; margin-top: 30px; padding: 20px; background-color: #000;'>"
            + "<p><em>Este correo es parte de un proyecto de prueba para el <strong>Examen Final de Algoritmos en Java</strong> en el curso de 2°1, dictado por la profesora <strong>Gabriela Tajes</strong> en el Instituto Beltrán. Este correo no tiene validez oficial.</em></p>"
            + "</div>"
            + "</body>"
            + "</html>";
    

    // Utiliza JavaMail API para enviar el correo
    try {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Usar el servidor SMTP de tu proveedor
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Habilitar TLS

        // Crear la sesión de correo con autenticación
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("juliancancelo@gmail.com", "veji hvkz lnep jhie");
            }
        });

        // Crear el mensaje
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("juliancancelo@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo));
        message.setSubject(asunto);
        message.setContent(mensaje, "text/html; charset=UTF-8");

        // Enviar el mensaje
        Transport.send(message);
        return true;
    } catch (MessagingException e) {
        e.printStackTrace(); // En producción, deberías manejarlo de manera adecuada
        return false;
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

    private boolean isEmailValido(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void limpiarCampos() {
        txtUsuario.setText("");
        txtContrasena.setText("");
        txtNombreCompleto.setText("");
        txtDni.setText("");
        txtLegajo.setText("");
        cmbPermisos.setSelectedIndex(0); // Resetea el combo de permisos
        txtEmail.setText(""); // Limpiar el campo de correo
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new CrearUsuario().setVisible(true));
    }

    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<String> cmbPermisos;
    private javax.swing.JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7;
    private javax.swing.JTextField txtUsuario, txtNombreCompleto, txtDni, txtLegajo, txtEmail;
    private javax.swing.JPasswordField txtContrasena;
}
