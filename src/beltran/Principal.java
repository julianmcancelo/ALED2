/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package beltran;
import Materias.CarrerasPanel;
import Materias.MateriaService;
import Materias.AsistenciaPanel;
import beltran.Clases.ServicioLogin;
import beltran.Clases.ServicioLogin.RolUsuario;
import beltran.Clases.ServicioLogin.SessionManager;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Image;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
/**
 *
 * @author julia
 */
public class Principal extends javax.swing.JFrame {
    private String usuario;
    private ServicioLogin.RolUsuario rolUsuario;
    private boolean isDarkTheme = false;  // Controlar el estado del tema
    private String nombreCompleto;  // Variable para almacenar el nombre completo

 private void actualizarTitulo() {
    // Formatear la fecha actual
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String fechaActual = sdf.format(new Date());

    // Crear el título con el nombre del usuario, fecha y rol
    String rol = (rolUsuario == ServicioLogin.RolUsuario.ADMINISTRADOR) ? "Administrador" : "Usuario";
    String titulo = String.format("Nombre: %s | Usuario: %s | Fecha: %s | Rol: %s", nombreCompleto, usuario, fechaActual, rol);

    // Establecer el título de la ventana
    setTitle(titulo);
}
     public Principal(String nombreCompleto, String usuario, ServicioLogin.RolUsuario rolUsuario) {
        if (!SessionManager.isLoggedIn) {
            JOptionPane.showMessageDialog(this, "Debes iniciar sesión para acceder a esta sección.");
            dispose();
            new InicioSesion().setVisible(true);
            return;
        }

        this.nombreCompleto = nombreCompleto;
        this.usuario = usuario;
        this.rolUsuario = rolUsuario;
        initComponents();
        ajustarInterfazSegunRol();
        actualizarTitulo();

        try {
            UIManager.setLookAndFeel(isDarkTheme ? new FlatDarkLaf() : new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

  private void ajustarInterfazSegunRol() {
        if (rolUsuario == RolUsuario.ADMINISTRADOR) {
            // Mostrar elementos específicos para administradores
            jEAlumnos.setVisible(true); // Hacer visible el menú de Alumnos para administradores
            jAdministrador.setVisible(true);
        } else {
            // Ocultar elementos específicos para usuarios regulares
            jEAlumnos.setVisible(false); // Ocultar el menú de Alumnos para usuarios regulares
            jAdministrador.setVisible(false);
        }
    }
    
    
    
 private void cambiarTema() {
        isDarkTheme = !isDarkTheme;
        try {
            UIManager.setLookAndFeel(isDarkTheme ? new FlatDarkLaf() : new FlatLightLaf());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);  // Actualizar la UI
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
 
 
 
 private void ajustarIcono() {
        // Cargar la imagen
        ImageIcon icon = new ImageIcon(getClass().getResource("/beltran/images/footer.png"));
        
        // Redimensionar la imagen para ajustarse al tamaño del JLabel
        Image img = icon.getImage().getScaledInstance(jLabel1.getWidth(), jLabel1.getHeight(), Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        
        // Establecer el icono en el JLabel
        jLabel1.setIcon(icon);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCambiartema = new javax.swing.JButton();
        btnAdmin = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jEAlumnos = new javax.swing.JMenuItem();
        jmRegistroAlumnos = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        txtCambiarContraseña = new javax.swing.JMenuItem();
        CerrarSesion = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        gCarreras = new javax.swing.JMenuItem();
        gMaterias = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jAdministrador = new javax.swing.JMenu();
        jmCrearUsuario = new javax.swing.JMenuItem();
        jmAcerca = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnCambiartema.setText("☾ / ☀");
        btnCambiartema.setToolTipText("Modo Oscuro / Claro");
        btnCambiartema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiartemaActionPerformed(evt);
            }
        });

        btnAdmin.setText("Administracion");
        btnAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminActionPerformed(evt);
            }
        });

        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabel1.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/beltran/images/footer.png"))); // NOI18N
        jLabel1.setEnabled(false);
        jLabel1.setMaximumSize(new java.awt.Dimension(500, 200));
        jLabel1.setMinimumSize(new java.awt.Dimension(500, 200));
        jLabel1.setPreferredSize(new java.awt.Dimension(500, 200));

        jMenu1.setText("Archivo");

        jMenu3.setText("Alumnos");

        jEAlumnos.setText("Edición de Alumnos");
        jEAlumnos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEAlumnosActionPerformed(evt);
            }
        });
        jMenu3.add(jEAlumnos);

        jmRegistroAlumnos.setText("Registrar Alumnos");
        jmRegistroAlumnos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmRegistroAlumnosActionPerformed(evt);
            }
        });
        jMenu3.add(jmRegistroAlumnos);

        jMenuItem1.setText("Control de Asistencia");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenu1.add(jMenu3);

        txtCambiarContraseña.setText("Cambiar Contraseña");
        txtCambiarContraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCambiarContraseñaActionPerformed(evt);
            }
        });
        jMenu1.add(txtCambiarContraseña);

        CerrarSesion.setText("Cerrar Sesion");
        CerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CerrarSesionActionPerformed(evt);
            }
        });
        jMenu1.add(CerrarSesion);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Carreras");

        gCarreras.setText("Gestión Carreras");
        gCarreras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gCarrerasActionPerformed(evt);
            }
        });
        jMenu4.add(gCarreras);

        gMaterias.setText("Gestión Materias");
        gMaterias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gMateriasActionPerformed(evt);
            }
        });
        jMenu4.add(gMaterias);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("Opciones");

        jAdministrador.setText("Administrador");

        jmCrearUsuario.setText("Crear Usuario");
        jmCrearUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmCrearUsuarioActionPerformed(evt);
            }
        });
        jAdministrador.add(jmCrearUsuario);

        jMenu2.add(jAdministrador);

        jmAcerca.setText("Acerca De");
        jmAcerca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmAcercaActionPerformed(evt);
            }
        });
        jMenu2.add(jmAcerca);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(btnCambiartema)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAdmin)
                .addGap(21, 21, 21))
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1483, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCambiartema)
                    .addComponent(btnAdmin))
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmAcercaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmAcercaActionPerformed
         // Crea un mensaje en formato HTML para mostrar la información en un diálogo
    String mensaje = "<html><body style='width: 300px; text-align: center;'>"
            + "<h2>Acerca de la Aplicación</h2>"
            + "<p>Aplicación desarrollada en Java por:</p>"
            + "<p><strong>Alumno:</strong> Cancelo Julian Manuel</p>"
            + "<p><strong>2do Año 1era TN</strong></p>"
            + "<p><strong>Analista de Sistemas</strong></p>"
            + "<p><strong>Profesora:</strong> Gabriela Tajes</p>"
            + "<p><strong>Segundo Parcial y Final de Materia:</strong> Algoritmos y Estructura de Datos</p>"
            + "<p><strong>Instituto:</strong> Beltran</p>"
            + "<p>Gracias por utilizar nuestra aplicación.</p>"
            + "</body></html>";

    // Muestra el diálogo con la información
    JOptionPane.showMessageDialog(this, mensaje, "Acerca de la Aplicación", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jmAcercaActionPerformed

    private void btnCambiartemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiartemaActionPerformed
                cambiarTema();  // Llamar al método cambiarTema al hacer clic

    }//GEN-LAST:event_btnCambiartemaActionPerformed

    private void txtCambiarContraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCambiarContraseñaActionPerformed
  // Obtén el nombre de usuario desde el contexto de la sesión actual
    String usuarioActual = usuario; // Implementa este método según tu lógica

    // Crea una instancia de ServicioLogin
    ServicioLogin servicioLogin = new ServicioLogin(); // Asegúrate de que esté configurado correctamente

    // Muestra la ventana para cambiar la contraseña
    CambiarContrasena.mostrarCambiarContrasena(usuarioActual, servicioLogin);
    }//GEN-LAST:event_txtCambiarContraseñaActionPerformed

    private void btnAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminActionPerformed
  // Crear una nueva instancia de Administracion y mostrarla
        Administracion admin = new Administracion();
        admin.setVisible(true);    }//GEN-LAST:event_btnAdminActionPerformed

    private void jEAlumnosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEAlumnosActionPerformed
  // Crear una nueva instancia de SeleccionarAlumno
    SeleccionarAlumno seleccionarAlumno = new SeleccionarAlumno();

    // Mostrar la ventana de SeleccionarAlumno
    seleccionarAlumno.setVisible(true);    }//GEN-LAST:event_jEAlumnosActionPerformed

    private void jmCrearUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmCrearUsuarioActionPerformed
    if (rolUsuario != RolUsuario.ADMINISTRADOR) {
        // Mostrar mensaje de error si no es administrador
        JOptionPane.showMessageDialog(this, "No tiene permisos para acceder a esta función.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
        return; // Evitar que se ejecute el código de la acción
    }
    
    // Aquí va el código que debe ejecutarse si el usuario es administrador
    // TODO add your handling code here:
    }//GEN-LAST:event_jmCrearUsuarioActionPerformed

    private void jmRegistroAlumnosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmRegistroAlumnosActionPerformed
   // Crea una instancia del formulario RegistroAlumnos
    RegistroALumnos registroAlumnos = new RegistroALumnos();
    
    // Configura la ventana para que esté visible
    registroAlumnos.setVisible(true);    }//GEN-LAST:event_jmRegistroAlumnosActionPerformed

    private void CerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CerrarSesionActionPerformed
  // Cierra la ventana actual


    // Muestra la ventana de inicio de sesión
    java.awt.EventQueue.invokeLater(() -> {
        // Crea una nueva instancia de InicioSesion
        InicioSesion inicioSesion = new InicioSesion();

        // Muestra la ventana de inicio de sesión
        inicioSesion.setVisible(true);
    });        // TODO add your handling code here:
    }//GEN-LAST:event_CerrarSesionActionPerformed

    private void gMateriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gMateriasActionPerformed
          MateriaService materiaService = new MateriaService(); // Crear una instancia de MateriaService
    MateriasPanel materiasPanel = new MateriasPanel(materiaService); // Pasar la instancia al constructor
    // Si quieres mostrar el panel en una ventana, necesitarás un JFrame
    JFrame frame = new JFrame("Gestión de Materias");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(600, 400);
    frame.add(materiasPanel);
    frame.setVisible(true);
    }//GEN-LAST:event_gMateriasActionPerformed

    private void gCarrerasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gCarrerasActionPerformed
    MateriaService materiaService = new MateriaService(); // Crear una instancia de MateriaService
    CarrerasPanel gestionCarrerasPanel = new CarrerasPanel(materiaService); // Pasar la instancia al constructor
    
    // Crear un nuevo JFrame para mostrar el panel
    JFrame frame = new JFrame("Gestión de Carreras");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(600, 400);
    frame.add(gestionCarrerasPanel);
    frame.setVisible(true);    }//GEN-LAST:event_gCarrerasActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
 MateriaService materiaService = new MateriaService();
    AsistenciaPanel asistenciaPanel = new AsistenciaPanel(materiaService);
    
    JFrame frame = new JFrame("Gestión de Asistencia");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);
    frame.add(asistenciaPanel);
    frame.setVisible(true);
       }//GEN-LAST:event_jMenuItem1ActionPerformed
    
    /**
     * @param args the command line arguments
     */

     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem CerrarSesion;
    private javax.swing.JButton btnAdmin;
    private javax.swing.JButton btnCambiartema;
    private javax.swing.JMenuItem gCarreras;
    private javax.swing.JMenuItem gMaterias;
    private javax.swing.JMenu jAdministrador;
    private javax.swing.JMenuItem jEAlumnos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jmAcerca;
    private javax.swing.JMenuItem jmCrearUsuario;
    private javax.swing.JMenuItem jmRegistroAlumnos;
    private javax.swing.JMenuItem txtCambiarContraseña;
    // End of variables declaration//GEN-END:variables

}