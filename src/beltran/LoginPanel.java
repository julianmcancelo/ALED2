package beltran;

import beltran.Clases.ServicioLogin;
import beltran.Clases.ServicioLogin.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que maneja la interfaz y lógica del inicio de sesión.
 */
public class LoginPanel extends JPanel {
    private final ServicioLogin servicioLogin; // Servicio para manejar el login
    private JTextField txtUsuario; // Campo para el nombre de usuario
    private JPasswordField txtContrasena; // Campo para la contraseña
    private JButton btnIniciarSesion; // Botón para iniciar sesión
    private JButton btnCancelar; // Botón para cancelar
    private JLabel lblIcono; // Icono en el panel

    /**
     * Constructor del panel de inicio de sesión.
     */
    public LoginPanel(ServicioLogin servicioLogin, InicioSesion parentFrame) {
        this.servicioLogin = servicioLogin; // Asigna el servicio de login
        initComponents(parentFrame); // Inicializa los componentes
    }

    /**
     * Inicializa los componentes de la interfaz.
     */
    private void initComponents(InicioSesion parentFrame) {
        txtUsuario = new JTextField(15); // Campo de texto para el usuario
        txtContrasena = new JPasswordField(15); // Campo de texto para la contraseña
        btnIniciarSesion = new JButton("Iniciar Sesión"); // Botón para iniciar sesión
        btnCancelar = new JButton("Cancelar"); // Botón para cancelar
        lblIcono = new JLabel(); // Label para el icono

        // Configura los eventos de los botones
        btnIniciarSesion.addActionListener(evt -> verificarLogin(parentFrame));
        btnCancelar.addActionListener(evt -> cancelarInicioSesion());

        // Cargar icono
        loadIcon();

        // Layout del panel
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellenar en horizontal

        // Centrar el icono
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER; // Centra el icono
        add(lblIcono, gbc);

        // Configurar los componentes restantes
        gbc.gridwidth = 1; // Restablecer a una columna
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        add(new JLabel("Usuario:"), gbc);
        
        gbc.gridx = 1; 
        add(txtUsuario, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 2; 
        add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1; 
        add(txtContrasena, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 3; 
        add(btnIniciarSesion, gbc);
        
        gbc.gridx = 1; 
        add(btnCancelar, gbc);
    }

    /**
     * Carga el icono en el panel.
     */
    private void loadIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("img/icono.png"));

        // Obtener dimensiones del icono original
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();

        // Calcular el tamaño deseado manteniendo el aspecto
        int newWidth = 400; // Ancho deseado
        int newHeight = (originalHeight * newWidth) / originalWidth; // Mantener aspecto

        // Escalar la imagen
        Image img = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        lblIcono.setIcon(new ImageIcon(img)); // Establecer el icono escalado
    }

    /**
     * Verifica las credenciales del usuario.
     */
    private void verificarLogin(InicioSesion parentFrame) {
        String usuario = txtUsuario.getText(); // Obtiene el usuario
        String contrasena = new String(txtContrasena.getPassword()); // Obtiene la contraseña

        // Autenticación del usuario
        ServicioLogin.ResultadoAutenticacion resultado = servicioLogin.autenticar(usuario, contrasena);
        if (resultado != null) {
            String nombreCompleto = resultado.getNombreCompleto(); // Nombre completo del usuario
            SessionManager.isLoggedIn = true; // Establece que el usuario ha iniciado sesión
            JOptionPane.showMessageDialog(this, "¡Bienvenido, " + nombreCompleto + "!");
            new Principal(nombreCompleto, usuario, resultado.getRolUsuario()).setVisible(true); // Abre la ventana principal
            parentFrame.dispose(); // Cierra la ventana de inicio de sesión
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE); // Mensaje de error
        }
    }

    /**
     * Cancela el inicio de sesión.
     */
    private void cancelarInicioSesion() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea cancelar el inicio de sesión?", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0); // Cierra la aplicación
        }
    }
}