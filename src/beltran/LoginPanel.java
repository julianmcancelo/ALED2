package beltran;

import beltran.Clases.SessionManager;

import javax.swing.*;
import java.awt.*;
import pruebas.PrincipalNuevo;

/**
 * Panel que maneja la interfaz y lógica del inicio de sesión.
 */
public class LoginPanel extends JPanel {
    private final ServicioLogin servicioLogin; // Servicio para manejar el login
    private JTextField txtUsuario; // Campo para el nombre de usuario
    private JPasswordField txtContrasena; // Campo para la contraseña
    private JButton btnIniciarSesion; // Botón para iniciar sesión
    private JButton btnCancelar; // Botón para cancelar
    public JButton btnRecuperarContrasena; // Botón para recuperar contraseña
    private JLabel lblIcono; // Icono en el panel
    
   

    /**
     * Constructor del panel de inicio de sesión.
     * @param servicioLogin
     * @param parentFrame
     */
public LoginPanel(ServicioLogin servicioLogin, InicioSesion parentFrame) {
            servicioLogin = new ServicioLogin(); // Inicialización

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
        btnRecuperarContrasena = new JButton("Recuperar Contraseña"); // Botón para recuperar contraseña
        lblIcono = new JLabel(); // Label para el icono

        // Configura los eventos de los botones
        btnIniciarSesion.addActionListener(evt -> verificarLogin(parentFrame));
        btnCancelar.addActionListener(evt -> cancelarInicioSesion());
        btnRecuperarContrasena.addActionListener(evt -> mostrarRecuperarContrasena());

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
        
        gbc.gridx = 0; 
        gbc.gridy = 4; 
        gbc.gridwidth = 2; // Ocupa dos columnas
        add(btnRecuperarContrasena, gbc); // Añadir el botón de recuperación
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

   // Verificar si el usuario existe
boolean usuarioExiste = servicioLogin.verificarUsuarioExistente(usuario);
if (!usuarioExiste) {
    // Si el usuario no existe, mostrar mensaje de error
    JOptionPane.showMessageDialog(this, 
        "El usuario no existe. Por favor, registre su cuenta o verifique el nombre de usuario.", 
        "Usuario no encontrado", 
        JOptionPane.ERROR_MESSAGE);
    return; // Salir del método sin continuar
}

// Verificar si la cuenta está validada
boolean cuentaValida = servicioLogin.verificarCuentaValida(usuario);
if (!cuentaValida) {
    // Si la cuenta no está validada, mostrar mensaje de error
    JOptionPane.showMessageDialog(this, 
        "La cuenta de usuario no está validada. Por favor, valide su cuenta primero.", 
        "Cuenta no validada", 
        JOptionPane.ERROR_MESSAGE);
    return; // Salir del método sin intentar autenticar
}

// Si el usuario existe y la cuenta está validada, proceder con la autenticación
ServicioLogin.ResultadoAutenticacion resultado = servicioLogin.autenticar(usuario, contrasena);
if (resultado != null) {
    String nombreCompleto = resultado.getNombreCompleto(); // Nombre completo del usuario
    SessionManager.isLoggedIn = true; // Establece que el usuario ha iniciado sesión
    JOptionPane.showMessageDialog(this, "¡Bienvenido, " + nombreCompleto + "!");
    new PrincipalNuevo(nombreCompleto, usuario, resultado.getRolUsuario()).setVisible(true); // Abre la ventana principal
    parentFrame.dispose(); // Cierra la ventana de inicio de sesión
} else {
    JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE); // Mensaje de error
}
}   

    /**
     * Muestra el diálogo para recuperar la contraseña.
     */
    public void mostrarRecuperarContrasena() {
    JDialog dialog = new JDialog((Frame) null, "Recuperar Contraseña", true);
    dialog.setSize(350, 300);
    dialog.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10); // Espaciado
    gbc.fill = GridBagConstraints.HORIZONTAL; // Rellenar en horizontal

    JTextField txtUsuario = new JTextField(15);
    JTextField txtLegajo = new JTextField(15);
    JPasswordField txtNuevaContrasena = new JPasswordField(15);
    JPasswordField txtConfirmarContrasena = new JPasswordField(15);

    gbc.gridx = 0; gbc.gridy = 0;
    dialog.add(new JLabel("Usuario:"), gbc);
    gbc.gridx = 1; 
    dialog.add(txtUsuario, gbc);

    gbc.gridx = 0; gbc.gridy = 1;
    dialog.add(new JLabel("Legajo:"), gbc);
    gbc.gridx = 1; 
    dialog.add(txtLegajo, gbc);

    gbc.gridx = 0; gbc.gridy = 2;
    dialog.add(new JLabel("Nueva Contraseña:"), gbc);
    gbc.gridx = 1; 
    dialog.add(txtNuevaContrasena, gbc);

    gbc.gridx = 0; gbc.gridy = 3;
    dialog.add(new JLabel("Confirmar Contraseña:"), gbc);
    gbc.gridx = 1; 
    dialog.add(txtConfirmarContrasena, gbc);

    JButton btnAceptar = new JButton("Aceptar");
    JButton btnCancelar = new JButton("Cancelar");

    btnAceptar.addActionListener(evt -> {
        String usuario = txtUsuario.getText();
        String legajo = txtLegajo.getText();
        String nuevaContrasena = new String(txtNuevaContrasena.getPassword());
        String confirmarContrasena = new String(txtConfirmarContrasena.getPassword());

        if (nuevaContrasena.equals(confirmarContrasena)) {
            boolean actualizado = servicioLogin.recuperarContrasena(usuario, legajo, nuevaContrasena);
            if (actualizado) {
                JOptionPane.showMessageDialog(dialog, "Contraseña actualizada exitosamente.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al actualizar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(dialog, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    btnCancelar.addActionListener(evt -> dialog.dispose());

    gbc.gridx = 0; gbc.gridy = 4;
    gbc.gridwidth = 2; // Ocupa dos columnas
    dialog.add(btnAceptar, gbc);
    gbc.gridy = 5;
    dialog.add(btnCancelar, gbc);

    dialog.setLocationRelativeTo(null); // Centra el diálogo
    dialog.setVisible(true);
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
