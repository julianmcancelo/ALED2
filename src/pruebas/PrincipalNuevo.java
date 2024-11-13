package pruebas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.text.SimpleDateFormat;
import java.util.Date;
import Materias.CarrerasPanel; 
import Materias.MateriaService;
import beltran.Administracion;
import beltran.AsistenciaPanel;
import beltran.CambiarContrasena;
import beltran.Clases.ServicioLogin;
import beltran.CrearComisionFrame;
import beltran.CrearUsuario;
import beltran.InicioSesion;
import beltran.MateriasPanel;
import beltran.RegistroALumnos;
import beltran.SeleccionarAlumno;
import java.awt.event.ActionListener;

public class PrincipalNuevo extends JFrame {
    private JButton btnCambiartema;
    private JButton btnAdmin;
    private JLabel jLabel1;
    private JDesktopPane desktopPane; 
    private JMenuBar jMenuBar1; // Declaración de jMenuBar1
    private boolean isDarkTheme = false; 
    private String usuario;
    private ServicioLogin.RolUsuario rolUsuario;
    private String nombreCompleto; 

    public PrincipalNuevo(String nombreCompleto, String usuario, ServicioLogin.RolUsuario rolUsuario) {
        this.nombreCompleto = nombreCompleto;
        this.usuario = usuario;
        this.rolUsuario = rolUsuario;

        initComponents();
        setBackgroundImage();
        ajustarInterfazSegunRol();
        actualizarTitulo();
        setLookAndFeel();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        btnCambiartema = createThemeButton();
        btnAdmin = createAdminButton();
        jLabel1 = createLabel();
        desktopPane = new JDesktopPane();
        desktopPane.setOpaque(false); 

        jMenuBar1 = createMenuBar(); // Inicialización de jMenuBar1
        setJMenuBar(jMenuBar1);

        layoutComponents();
    }

    private JButton createThemeButton() {
        JButton button = new JButton("☾ / ☀");
        button.setToolTipText("Modo Oscuro / Claro");
        button.addActionListener(evt -> toggleTheme());
        return button;
    }

    private JButton createAdminButton() {
        JButton button = new JButton("Administración");
        button.addActionListener(evt -> btnAdminActionPerformed(evt));
        return button;
    }

    private JLabel createLabel() {
        JLabel label = new JLabel();
        label.setEnabled(false);
        label.setMaximumSize(new Dimension(500, 200));
        label.setMinimumSize(new Dimension(500, 200));
        label.setPreferredSize(new Dimension(500, 200));
        return label;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createArchivoMenu());
        menuBar.add(createCarrerasMenu());
        menuBar.add(createOpcionesMenu());
        return menuBar;
    }

    private JMenu createArchivoMenu() {
        JMenu menu = new JMenu("Archivo");
        menu.add(createMenuItem("Cambiar Contraseña", this::txtCambiarContraseñaActionPerformed));
        menu.add(createMenuItem("Cerrar Sesión", this::CerrarSesionActionPerformed));
        menu.add(createAlumnosMenu());
        return menu;
    }

    private JMenu createAlumnosMenu() {
        JMenu menu = new JMenu("Alumnos");
        menu.add(createMenuItem("Edición de Alumnos", this::jEAlumnosActionPerformed));
        menu.add(createMenuItem("Registrar Alumnos", this::jmRegistroAlumnosActionPerformed));
        menu.add(createMenuItem("Control de Asistencia", this::jMenuItem1ActionPerformed));
        return menu;
    }

    private JMenu createCarrerasMenu() {
        JMenu menu = new JMenu("Carreras");
        menu.add(createMenuItem("Gestión Carreras", this::gCarrerasActionPerformed));
        menu.add(createMenuItem("Gestión Materias", this::gMateriasActionPerformed));
        menu.add(createMenuItem("Gestión Comisiones", this::jMComisionActionPerformed));
        return menu;
    }

    private JMenu createOpcionesMenu() {
        JMenu menu = new JMenu("Opciones");
        JMenu adminMenu = new JMenu("Administrador");
        adminMenu.add(createMenuItem("Crear Usuario", this::jmCrearUsuarioActionPerformed));
        menu.add(adminMenu);
        menu.add(createMenuItem("Acerca De", this::jmAcercaActionPerformed));
        return menu;
    }

    private JMenuItem createMenuItem(String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(action);
        return item;
    }

    private void layoutComponents() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(btnCambiartema)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAdmin)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 1483, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 459, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCambiartema)
                    .addComponent(btnAdmin))
                .addGap(71, 71, 71))
            .addComponent(desktopPane)
        );

        pack();
    }

    private void setBackgroundImage() {
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/beltran/images/footer.png")); 
        jLabel1.setIcon(backgroundImage);
        jLabel1.setLayout(new BorderLayout());
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(isDarkTheme ? new FlatDarkLaf() : new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        setLookAndFeel();
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void actualizarTitulo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = sdf.format(new Date());
        String rol = (rolUsuario == ServicioLogin.RolUsuario.ADMINISTRADOR) ? "Administrador" : "Usuario";
        setTitle(String.format("Nombre: %s | Usuario: %s | Fecha: %s | Rol: %s", nombreCompleto, usuario, fechaActual, rol));
    }

    private void ajustarInterfazSegunRol() {
 if (rolUsuario == ServicioLogin.RolUsuario.ADMINISTRADOR) {
            // Mostrar elementos específicos para administradores
            btnAdmin.setVisible(true);
         } else {
            // Ocultar elementos específicos para usuarios regulares
            btnAdmin.setVisible(false);
        }    }

    private void gCarrerasActionPerformed(ActionEvent evt) {
        JInternalFrame gestionCarrerasFrame = new JInternalFrame("Gestión de Carreras", true, true, true, true);
        gestionCarrerasFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        gestionCarrerasFrame.setSize(600, 400);
        gestionCarrerasFrame.add(new CarrerasPanel(new MateriaService()));
        desktopPane.add(gestionCarrerasFrame);
        gestionCarrerasFrame.setVisible(true);
        desktopPane.moveToFront(gestionCarrerasFrame);
    }

    private void btnAdminActionPerformed(ActionEvent evt) {
        if (rolUsuario != ServicioLogin.RolUsuario.ADMINISTRADOR) {
            JOptionPane.showMessageDialog(this, "No tiene permisos para acceder a esta función.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Administracion admin = new Administracion();
        desktopPane.add(admin);
        admin.setVisible(true);
    }

    private void jEAlumnosActionPerformed(ActionEvent evt) {
 // Crear una nueva instancia de SeleccionarAlumno
    SeleccionarAlumno seleccionarAlumno = new SeleccionarAlumno();

    // Mostrar la ventana de SeleccionarAlumno
    seleccionarAlumno.setVisible(true);    }

    private void jmRegistroAlumnosActionPerformed(ActionEvent evt) {
    RegistroALumnos registroAlumnos = new RegistroALumnos();
    
    // Configura la ventana para que esté visible
    registroAlumnos.setVisible(true);        }

    private void jMenuItem1ActionPerformed(ActionEvent evt) {
 MateriaService materiaService = new MateriaService();
    AsistenciaPanel asistenciaPanel = new AsistenciaPanel(materiaService);
    
    JFrame frame = new JFrame("Gestión de Asistencia");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);
    frame.add(asistenciaPanel);
    frame.setVisible(true);    }

    private void txtCambiarContraseñaActionPerformed(ActionEvent evt) {
        CambiarContrasena.mostrarCambiarContrasena(usuario, nombreCompleto, new ServicioLogin(), desktopPane);
    }

    private void CerrarSesionActionPerformed(ActionEvent evt) {
        int response = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea cerrar sesión?", "Confirmar Cierre de Sesión", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            dispose();
            InicioSesion inicioSesion = new InicioSesion();
            inicioSesion.setVisible(true);
        }
    }

    private void gMateriasActionPerformed(ActionEvent evt) {
          MateriaService materiaService = new MateriaService(); // Crear una instancia de MateriaService
    MateriasPanel materiasPanel = new MateriasPanel(materiaService); // Pasar la instancia al constructor
    // Si quieres mostrar el panel en una ventana, necesitarás un JFrame
    JFrame frame = new JFrame("Gestión de Materias");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(600, 400);
    frame.add(materiasPanel);
    frame.setVisible(true);    }

    private void jMComisionActionPerformed(ActionEvent evt) {
        CrearComisionFrame crearComisionFrame = new CrearComisionFrame();
        desktopPane.add(crearComisionFrame);
        crearComisionFrame.setVisible(true);
        crearComisionFrame.toFront();
    }

    private void jmCrearUsuarioActionPerformed(ActionEvent evt) {
// Verifica si el rol del usuario no es ADMINISTRADOR
    if (rolUsuario != ServicioLogin.RolUsuario.ADMINISTRADOR) {
        // Si no tiene permisos, muestra un mensaje de error
        JOptionPane.showMessageDialog(this, 
            "No tiene permisos para acceder a esta función.", 
            "Acceso Denegado", 
            JOptionPane.ERROR_MESSAGE);
        return; // Salir del método para evitar que se ejecute el código adicional
    }
    
    // Si el usuario es administrador, se puede abrir la ventana CrearUsuario
    CrearUsuario crearUsuario = new CrearUsuario();
    crearUsuario.setVisible(true); // Hacer visible la ventana de CrearUsuario
        }

    private void jmAcercaActionPerformed(ActionEvent evt) {
        String mensaje = """
        <html>
          <body style='width: 400px; font-family: Arial, sans-serif; text-align: center;'>
            <h2 style='color: #2c3e50; margin-bottom: 10px;'>Acerca de la Aplicación</h2>
            <p style='font-size: 15px; color: #34495e;'>Proyecto académico desarrollado en <strong>Java</strong>.</p>
            <hr style='border: none; height: 1px; background-color: #bdc3c7; margin: 10px 0;'>
            <p><strong>Alumno:</strong> Cancelo Julián Manuel</p>
            <p><strong>Curso:</strong> 2do Año - 1era TN</p>
            <p><strong>Programa:</strong> Analista de Sistemas</p>
            <hr style='border: none; height: 1px; background-color: #bdc3c7; margin: 10px 0;'>
            <p><strong>Profesora:</strong> Gabriela Tajes</p>
            <p><strong>Materia:</strong> Algoritmos y Estructura de Datos</p>
            <p><strong>Evaluación:</strong> Segundo Parcial y Examen Final</p>
            <hr style='border: none; height: 1px; background-color: #bdc3c7; margin: 10px 0;'>
            <p><strong>Institución:</strong> Instituto Beltrán</p>
            <p style='font-size: 14px; color: #34495e;'>Gracias por utilizar nuestra aplicación.</p>
            <p style='font-size: 12px; font-style: italic; color: #7f8c8d;'>© 2024 Cancelo Julián Manuel. Todos los derechos reservados.</p>
          </body>
        </html>
        """;
        JOptionPane.showMessageDialog(null, mensaje, "Acerca de la Aplicación", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String nombreCompleto = "Nombre Completo"; 
            String usuario = "usuario"; 
            ServicioLogin.RolUsuario rolUsuario = ServicioLogin.RolUsuario.USUARIO; 

            PrincipalNuevo mainFrame = new PrincipalNuevo(nombreCompleto, usuario, rolUsuario);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);
        });
    }
}
