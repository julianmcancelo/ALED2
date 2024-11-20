package beltran;

import static beltran.Clases.Conexion.getConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class GestionUsuariosPanel extends JPanel {
    private JTable tablaUsuarios;
    private JButton btnReenviarValidacion;
    private JButton btnEliminarUsuario;
    private JButton btnModificarUsuario;
    private JButton btnGenerarNuevoToken;  // Declarar el nuevo botón
    private DefaultTableModel tableModel;

    public GestionUsuariosPanel() {
        setLayout(new BorderLayout());

        // Configurar la tabla de usuarios
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Usuario", "Rol", "Estado", "Correo", "Token"}, 0);
        tablaUsuarios = new JTable(tableModel);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Cargar los usuarios desde la base de datos usando ServicioLogin
        cargarUsuarios();

        // Panel de botones
        JPanel panelBotones = new JPanel();
        btnReenviarValidacion = new JButton("Reenviar Enlace de Validación");
        btnEliminarUsuario = new JButton("Eliminar Usuario");
        btnModificarUsuario = new JButton("Modificar Usuario");
        btnGenerarNuevoToken = new JButton("Generar Nuevo Token");

        // Agregar listeners
        btnReenviarValidacion.addActionListener(this::reenviarValidacion);
        btnEliminarUsuario.addActionListener(this::eliminarUsuario);
        btnModificarUsuario.addActionListener(this::modificarUsuario);
        btnGenerarNuevoToken.addActionListener(this::generarNuevoToken); // Acción para generar nuevo token

        panelBotones.add(btnReenviarValidacion);
        panelBotones.add(btnEliminarUsuario);
        panelBotones.add(btnModificarUsuario);
        panelBotones.add(btnGenerarNuevoToken); // Agregar el botón al panel

        add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Método para cargar los usuarios desde la base de datos
    private void cargarUsuarios() {
        List<Usuario> usuarios = ServicioLogin.obtenerUsuarios();

        // Verifica si los usuarios se están obteniendo correctamente
        System.out.println("Usuarios obtenidos: " + usuarios.size());

        // Vaciar la tabla antes de agregar los nuevos usuarios
        tableModel.setRowCount(0);

        // Iterar sobre la lista de usuarios y agregar las filas a la tabla
for (Usuario usuario : usuarios) {
    String estadoValidacion = usuario.isValidado() ? "Validado" : "No Validado"; // Verificar si está validado
    tableModel.addRow(new Object[]{
        usuario.getId(),
        usuario.getNombreCompleto(),
        usuario.getUsuario(),
        usuario.getPermisos(),
        estadoValidacion,
        usuario.getEmail(),
        usuario.getTokenValidacion() // Asegúrate de agregar el token en la columna correcta
    });
}
    }

    // Método para actualizar el token en la base de datos
    public static boolean actualizarTokenUsuario(int idUsuario, String nuevoToken) {
        // Aquí va la lógica para actualizar el token en la base de datos
        // Por ejemplo:
        String query = "UPDATE usuarios SET token_validacion = ? WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, nuevoToken);
            pst.setInt(2, idUsuario);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Acción para generar un nuevo token
    private void generarNuevoToken(ActionEvent evt) {
        int selectedRow = tablaUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            int idUsuario = (int) tableModel.getValueAt(selectedRow, 0);  // Obtener el id del usuario
            String email = (String) tableModel.getValueAt(selectedRow, 5);  // Obtener el correo del usuario
            String nombreUsuario = (String) tableModel.getValueAt(selectedRow, 1);  // Nombre de usuario

            // Generar un nuevo token (por ejemplo, usando UUID)
            String nuevoToken = UUID.randomUUID().toString();

            // Actualizar el token en la base de datos
            if (actualizarTokenUsuario(idUsuario, nuevoToken)) {
                // Actualizar la tabla con el nuevo token
                tableModel.setValueAt(nuevoToken, selectedRow, 6); // Actualiza la columna del token
                JOptionPane.showMessageDialog(this, "Nuevo token generado para el usuario " + nombreUsuario);

                // Si deseas reenviar el correo con el nuevo token
                boolean correoEnviado = reenviarCorreoDeValidacion(email, nuevoToken);
                if (correoEnviado) {
                    JOptionPane.showMessageDialog(this, "Reenviando enlace de validación a " + email);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo reenviar el enlace de validación.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el token en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario.");
        }
    }

    // Acción para reenviar el enlace de validación
    private void reenviarValidacion(ActionEvent evt) {
        int selectedRow = tablaUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            String email = (String) tableModel.getValueAt(selectedRow, 5);  // Obtener el correo del usuario
            String token = (String) tableModel.getValueAt(selectedRow, 6);  // Obtener el token (índice 6)

            if (email != null && !email.isEmpty()) {
                boolean correoEnviado = reenviarCorreoDeValidacion(email, token);
                if (correoEnviado) {
                    JOptionPane.showMessageDialog(this, "Reenviando enlace de validación a " + email);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo reenviar el enlace de validación.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "El correo electrónico del usuario no está disponible.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario.");
        }
    }


// Método para reenviar el correo de validación (utilizando JavaMail)
private boolean reenviarCorreoDeValidacion(String correo, String token) {
    String asunto = "✅ Reenvío de Verificación de Correo Electrónico - Examen Final de Algoritmos en Java";
    String mensaje = "<html>"
            + "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6; background-color: #f4f4f4; padding: 20px;'>"
            // Contenedor con fondo blanco para el logo, mejorando la visibilidad sobre cualquier fondo
            + "<div style='background-color: #ffffff; color: #333333; padding: 20px; text-align: center; border-radius: 10px;'>"
            // Logo agrandado para hacerlo más visible y mejorar la legibilidad
            + "<img src='https://ibeltran.com.ar/img/logo/logo1.png' alt='Logo' style='max-width: 300px; height: auto; margin-bottom: 15px; filter: brightness(0) invert(1);'>"
            + "</div>"
            + "<div style='background-color: rgba(255, 255, 255, 0.9); padding: 20px; border-radius: 8px; margin-top: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>"
            + "<p>¡Hola! 👋</p>"
            // Mensaje inicial modificado para ser más amigable
            + "<p>Gracias por registrarte para presentar el <strong>Examen Final de Algoritmos - Java</strong> 💻. Para poder continuar, necesitamos que verifiques tu cuenta.</p>"
            + "<p><strong>Por favor, haz clic en el siguiente enlace para validar tu correo y activar tu acceso:</strong></p>"
            + "<p style='text-align: center;'>"
            // Botón con mayor padding y estilo mejorado
            + "<a href='http://www.jmcancelo.xyz/validar2.php?token=" + token + "' style='background-color: #1071B5; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-weight: bold; font-size: 18px; display: inline-block;'>"
            + "✅ Verificar mi cuenta"
            + "</a>"
            + "</p>"
            + "<p style='font-size: 14px; color: #555;'>⏳ Este enlace será válido solo por <strong>24 horas</strong>, ¡así que no lo dejes pasar! Si no solicitaste este registro, simplemente ignora este correo.</p>"
            + "<hr style='border: 1px solid #ddd; margin-top: 30px;'>"
            + "<p style='font-size: 16px; font-weight: bold;'>📝 Detalles del Examen:</p>"
            + "<ul style='list-style-type: none; padding: 0; font-size: 14px;'>"
            + "<li><strong>🖥 Examen:</strong> Algoritmos en Java - 2°1</li>"
            + "<li><strong>👨‍🏫 Alumno:</strong> Julian Manuel Cancelo</li>"
            + "<li><strong>📚 Profesora Responsable:</strong> Gabriela Tajes</li>"
            + "<li><strong>⏰ Fecha límite para completar la verificación:</strong> 24 horas.</li>"
            + "</ul>"
            + "<p style='font-size: 14px; color: #555;'>Si tienes alguna duda o necesitas ayuda, no dudes en ponerte en contacto con nosotros:</p>"
            + "<p style='font-size: 14px; color: #555;'>"
            // Enlaces ahora tendrán el mismo color
            + "📧 <strong>Email de Soporte:</strong> <a href='mailto:soporte@examenfinal.com' style='color: #1071B5;'>soporte@examenfinal.com</a><br>"
            + "📱 <strong>Teléfono de Soporte:</strong> <a href='tel:+1126354636' style='color: #1071B5;'>1126354636</a><br>"
            + "💬 <strong>WhatsApp:</strong> <a href='https://wa.me/1126354636' style='color: #1071B5;' target='_blank'>Chatea con nosotros en WhatsApp</a>"
            + "</p>"
            + "<br>"
            + "<p style='font-style: italic; font-size: 16px; color: #555;'>¡Te deseamos mucha suerte en el examen! 🍀</p>"
            + "</div>"
            // Footer con fondo negro y texto blanco para mejorar la legibilidad
            + "<div style='font-size: 12px; color: white; text-align: center; margin-top: 30px; padding: 20px; background-color: #000;'>"
            + "<p><em>Este correo es parte de una aplicación de prueba para el <strong>Examen Final de Algoritmos - Java</strong> en el curso de 2°1, dictado por <strong>Gabriela Tajes</strong>. No es un correo oficial de ninguna institución educativa, sino un proyecto para evaluar la implementación de una solución en Java.</em></p>"
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





    // Acción para eliminar un usuario
    private void eliminarUsuario(ActionEvent evt) {
        int selectedRow = tablaUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener los datos de la fila seleccionada
            int idUsuario = (int) tableModel.getValueAt(selectedRow, 0);  // Obtener el id del usuario
            String nombreUsuario = (String) tableModel.getValueAt(selectedRow, 1);  // Obtener el nombre del usuario
            String nombreCompleto = (String) tableModel.getValueAt(selectedRow, 2);  // Nombre completo
            String permisos = (String) tableModel.getValueAt(selectedRow, 3);  // Obtener el rol del usuario
            String email = (String) tableModel.getValueAt(selectedRow, 5);  // Obtener el email
            boolean validado = "Validado".equals(tableModel.getValueAt(selectedRow, 4));  // Obtener estado de validación

            // Crear el objeto Usuario
            Usuario usuario = new Usuario(idUsuario, nombreUsuario, nombreCompleto, permisos, email);

            int response = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al usuario " + nombreUsuario + "?",
                    "Confirmación de eliminación", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                boolean eliminado = ServicioLogin.eliminarUsuario(usuario);  // Ahora pasamos el objeto Usuario
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Usuario " + nombreUsuario + " eliminado.");
                    tableModel.removeRow(selectedRow);  // Elimina la fila de la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario.");
        }
    }

    // Acción para modificar un usuario
    private void modificarUsuario(ActionEvent evt) {
        int selectedRow = tablaUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            int idUsuario = (int) tableModel.getValueAt(selectedRow, 0);  // Obtener el id del usuario
            // Aquí deberías abrir un formulario o ventana para modificar los datos del usuario
            JOptionPane.showMessageDialog(this, "Modificando usuario con ID: " + idUsuario);
            // Lógica para modificar el usuario (puedes abrir un cuadro de diálogo o ventana nueva)
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario.");
        }
    }
}
