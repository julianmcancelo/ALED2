package beltran;

import beltran.Clases.Conexion;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EditarAlumno extends JFrame {

    private JTextField txtDNI; // Campo para el DNI del alumno
    private JTextField txtApellido; // Campo para el apellido del alumno
    private JTextField txtCarrera; // Campo para la carrera del alumno
    private JTextField txtNombres; // Campo para los nombres del alumno
    private JTextField txtLegajo; // Campo para el legajo del alumno
    private JTextField txtFotoRuta; // Campo para la ruta de la foto del alumno
    private JTextField txtDomicilio; // Campo para el domicilio del alumno
    private JTextField txtAltura; // Campo para la altura del alumno
    private JTextField txtLocalidad; // Campo para la localidad del alumno
    private JTextField txtProvincia; // Campo para la provincia del alumno
    private JTextField txtFechaNacimiento; // Campo para la fecha de nacimiento del alumno
    private JTextField txtAnoIngreso; // Campo para el año de ingreso del alumno
    private JTextField txtEmail; // Campo para el email del alumno
    private JTextField txtEstado; // Campo para el estado del alumno
    private JButton btnGuardar; // Botón para guardar los cambios
    private String dniOriginal; // DNI original del alumno (para actualizaciones)

    public EditarAlumno(String dni) {
        this.dniOriginal = dni; // Guarda el DNI del alumno que se va a editar

        setTitle("Editar Alumno"); // Título de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLayout(new GridBagLayout()); // Disposición en cuadrícula
        GridBagConstraints gbc = new GridBagConstraints(); // Para alinear los componentes
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los componentes llenan horizontalmente
        gbc.anchor = GridBagConstraints.WEST; // Alinear los componentes a la izquierda

        // Inicialización de los campos de texto
        txtDNI = new JTextField(20);
        txtApellido = new JTextField(20);
        txtCarrera = new JTextField(20);
        txtNombres = new JTextField(20);
        txtLegajo = new JTextField(20);
        txtFotoRuta = new JTextField(20);
        txtDomicilio = new JTextField(20);
        txtAltura = new JTextField(20);
        txtLocalidad = new JTextField(20);
        txtProvincia = new JTextField(20);
        txtFechaNacimiento = new JTextField(20);
        txtAnoIngreso = new JTextField(20);
        txtEmail = new JTextField(20);
        txtEstado = new JTextField(20);
        btnGuardar = new JButton("Guardar"); // Botón para guardar los cambios

        // Hacer campos no editables
        txtDNI.setEditable(false); // El DNI no debe ser editable

        // Estilo del botón
        btnGuardar.setBackground(new Color(0, 123, 255)); // Color de fondo
        btnGuardar.setForeground(Color.WHITE); // Color del texto
        btnGuardar.setFocusPainted(false); // Quitar borde de enfoque

        // Agregar componentes al marco
        // Se añaden etiquetas y campos de texto en la disposición de cuadrícula
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        add(new JLabel("DNI:"), gbc); gbc.gridx = 1; add(txtDNI, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Nombres:"), gbc); gbc.gridx = 1; add(txtNombres, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Apellido:"), gbc); gbc.gridx = 1; add(txtApellido, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Carrera:"), gbc); gbc.gridx = 1; add(txtCarrera, gbc);
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Legajo:"), gbc); gbc.gridx = 1; add(txtLegajo, gbc);
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Foto Ruta:"), gbc); gbc.gridx = 1; add(txtFotoRuta, gbc);
        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Domicilio:"), gbc); gbc.gridx = 1; add(txtDomicilio, gbc);
        gbc.gridx = 0; gbc.gridy = 7; add(new JLabel("Altura:"), gbc); gbc.gridx = 1; add(txtAltura, gbc);
        gbc.gridx = 0; gbc.gridy = 8; add(new JLabel("Localidad:"), gbc); gbc.gridx = 1; add(txtLocalidad, gbc);
        gbc.gridx = 0; gbc.gridy = 9; add(new JLabel("Provincia:"), gbc); gbc.gridx = 1; add(txtProvincia, gbc);
        gbc.gridx = 0; gbc.gridy = 10; add(new JLabel("Fecha Nacimiento:"), gbc); gbc.gridx = 1; add(txtFechaNacimiento, gbc);
        gbc.gridx = 0; gbc.gridy = 11; add(new JLabel("Año Ingreso:"), gbc); gbc.gridx = 1; add(txtAnoIngreso, gbc);
        gbc.gridx = 0; gbc.gridy = 12; add(new JLabel("Email:"), gbc); gbc.gridx = 1; add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 13; add(new JLabel("Estado:"), gbc); gbc.gridx = 1; add(txtEstado, gbc);
        
        // Centrar el botón "Guardar"
        gbc.gridx = 0; gbc.gridy = 14; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; 
        add(btnGuardar, gbc);

        pack(); // Ajustar el tamaño de la ventana automáticamente
        setLocationRelativeTo(null); // Centrar la ventana

        loadAlumnoData(); // Cargar datos del alumno

        // Acción para el botón "Guardar"
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAlumnoData(); // Llamar al método para guardar datos
            }
        });
    }

    /**
     * Carga los datos del alumno en los campos de texto.
     */
    private void loadAlumnoData() {
        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT * FROM alumnos WHERE dni = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dniOriginal); // Establecer el DNI en la consulta
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Llenar los campos con los datos del alumno
                        txtDNI.setText(rs.getString("dni"));
                        txtNombres.setText(rs.getString("nombres"));
                        txtApellido.setText(rs.getString("apellido"));
                        txtCarrera.setText(rs.getString("carrera"));
                        txtLegajo.setText(rs.getString("legajo"));
                        txtFotoRuta.setText(rs.getString("ruta_foto"));
                        txtDomicilio.setText(rs.getString("domicilio"));
                        txtAltura.setText(rs.getString("altura"));
                        txtLocalidad.setText(rs.getString("localidad"));
                        txtProvincia.setText(rs.getString("provincia"));
                        txtFechaNacimiento.setText(rs.getString("fecha_nacimiento"));
                        txtAnoIngreso.setText(rs.getString("ano_ingreso"));
                        txtEmail.setText(rs.getString("email"));
                        txtEstado.setText(rs.getString("estado"));
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontraron datos para el DNI proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos del alumno: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Guarda los datos del alumno después de validarlos.
     */
    private void saveAlumnoData() {
        // Obtener los datos ingresados
        String dni = txtDNI.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellido = txtApellido.getText().trim();
        String carrera = txtCarrera.getText().trim();
        String legajo = txtLegajo.getText().trim();
        String fotoRuta = txtFotoRuta.getText().trim();
        String domicilio = txtDomicilio.getText().trim();
        String altura = txtAltura.getText().trim();
        String localidad = txtLocalidad.getText().trim();
        String provincia = txtProvincia.getText().trim();
        String fechaNacimiento = txtFechaNacimiento.getText().trim();
        String anoIngreso = txtAnoIngreso.getText().trim();
        String email = txtEmail.getText().trim();
        String estado = txtEstado.getText().trim();

        // Validaciones de campos obligatorios
        if (nombres.isEmpty() || apellido.isEmpty() || carrera.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombres, Apellido y Carrera son campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Detener ejecución si hay campos obligatorios vacíos
        }

        // Validar formato de email
        if (!email.isEmpty() && !isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "El formato del email no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Detener si el email no es válido
        }

        // Validar fecha de nacimiento
        if (!fechaNacimiento.isEmpty() && !isValidDate(fechaNacimiento)) {
            JOptionPane.showMessageDialog(this, "El formato de la fecha de nacimiento no es válido. Use dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Detener si la fecha no es válida
        }

        // Obtener los datos actuales para comparación
        String datosAntiguos = getDatosAntiguos();

        // Construir mensaje de confirmación
        String mensajeConfirmacion = String.format(
            "<html><body>" +
            "<h3>Datos actuales:</h3>" +
            "<pre>%s</pre>" +
            "<h3>Datos nuevos:</h3>" +
            "<pre><b>Nombres:</b> %s<br>" +
            "<b>Apellido:</b> %s<br>" +
            "<b>Carrera:</b> %s<br>" +
            "<b>Foto Ruta:</b> %s<br>" +
            "<b>Domicilio:</b> %s<br>" +
            "<b>Altura:</b> %s<br>" +
            "<b>Localidad:</b> %s<br>" +
            "<b>Provincia:</b> %s<br>" +
            "<b>Fecha Nacimiento:</b> %s<br>" +
            "<b>Año Ingreso:</b> %s<br>" +
            "<b>Email:</b> %s<br>" +
            "<b>Estado:</b> %s<br></pre>" +
            "</body></html>",
            datosAntiguos,
            nombres, apellido, carrera, fotoRuta, domicilio, altura, localidad, provincia, fechaNacimiento, anoIngreso, email, estado
        );

        // Diálogo de confirmación
        int confirm = JOptionPane.showConfirmDialog(
            this,
            mensajeConfirmacion,
            "Confirmar cambios",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );

        if (confirm == JOptionPane.OK_OPTION) {
            // Guardar los datos si se confirma
            try (Connection conn = Conexion.getConnection()) {
                String sqlUpdate = "UPDATE alumnos SET nombres = ?, apellido = ?, carrera = ?, ruta_foto = ?, domicilio = ?, altura = ?, localidad = ?, provincia = ?, fecha_nacimiento = ?, ano_ingreso = ?, email = ?, estado = ? WHERE dni = ?";
                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    // Establecer los parámetros en la consulta
                    stmtUpdate.setString(1, nombres);
                    stmtUpdate.setString(2, apellido);
                    stmtUpdate.setString(3, carrera);
                    stmtUpdate.setString(4, fotoRuta);
                    stmtUpdate.setString(5, domicilio);
                    stmtUpdate.setString(6, altura);
                    stmtUpdate.setString(7, localidad);
                    stmtUpdate.setString(8, provincia);
                    stmtUpdate.setString(9, fechaNacimiento);
                    stmtUpdate.setString(10, anoIngreso);
                    stmtUpdate.setString(11, email);
                    stmtUpdate.setString(12, estado);
                    stmtUpdate.setString(13, dniOriginal);
                    stmtUpdate.executeUpdate(); // Ejecutar actualización

                    JOptionPane.showMessageDialog(this, "Datos del alumno actualizados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar los datos del alumno: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Obtiene los datos actuales del alumno para mostrar en la confirmación.
     */
    private String getDatosAntiguos() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT * FROM alumnos WHERE dni = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dniOriginal); // Establecer el DNI en la consulta
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Agregar los datos actuales al StringBuilder
                        sb.append(String.format(
                            "Nombres: %s%n" +
                            "Apellido: %s%n" +
                            "Carrera: %s%n" +
                            "Foto Ruta: %s%n" +
                            "Domicilio: %s%n" +
                            "Altura: %s%n" +
                            "Localidad: %s%n" +
                            "Provincia: %s%n" +
                            "Fecha Nacimiento: %s%n" +
                            "Año Ingreso: %s%n" +
                            "Email: %s%n" +
                            "Estado: %s%n",
                            rs.getString("nombres"),
                            rs.getString("apellido"),
                            rs.getString("carrera"),
                            rs.getString("ruta_foto"),
                            rs.getString("domicilio"),
                            rs.getString("altura"),
                            rs.getString("localidad"),
                            rs.getString("provincia"),
                            rs.getString("fecha_nacimiento"),
                            rs.getString("ano_ingreso"),
                            rs.getString("email"),
                            rs.getString("estado")
                        ));
                    } else {
                        sb.append("No se encontraron datos para el DNI proporcionado.");
                    }
                }
            }
        } catch (SQLException e) {
            sb.append("Error al obtener los datos antiguos: ").append(e.getMessage());
        }
        return sb.toString(); // Retornar los datos antiguos
    }

    /**
     * Método para validar el formato de un email.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$"; // Expresión regular para validar emails
        return email.matches(emailRegex);
    }

    /**
     * Método para validar el formato de una fecha.
     * Debe estar en formato dd/MM/yyyy.
     */
    private boolean isValidDate(String date) {
        String dateRegex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$"; // Expresión regular para validar fechas
        return date.matches(dateRegex);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Establecer el look and feel
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace(); // Manejo de errores
        }
        // Crear y mostrar la ventana para editar un alumno
        SwingUtilities.invokeLater(() -> new EditarAlumno("12345678").setVisible(true));
    }
}
