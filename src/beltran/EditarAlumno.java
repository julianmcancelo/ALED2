package beltran;

import beltran.Clases.Conexion;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EditarAlumno extends JFrame {

    private JTextField txtDNI;
    private JTextField txtApellido;
    private JTextField txtCarrera;
    private JTextField txtNombres;
    private JTextField txtLegajo;
    private JTextField txtFotoRuta;
    private JTextField txtDomicilio;
    private JTextField txtAltura;
    private JTextField txtLocalidad;
    private JTextField txtProvincia;
    private JTextField txtFechaNacimiento;
    private JTextField txtAnoIngreso;
    private JTextField txtEmail;
    private JTextField txtEstado;
    private JButton btnGuardar;
    private String dniOriginal;

    public EditarAlumno(String dni) {
        this.dniOriginal = dni;

        setTitle("Editar Alumno");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST; // Alinear los componentes a la izquierda

        // Inicialización de los componentes
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
        btnGuardar = new JButton("Guardar");

        // Hacer campos no editables
        txtDNI.setEditable(false);

        // Estilo del botón
        btnGuardar.setBackground(new Color(0, 123, 255));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        // Agregar componentes al marco
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1;
        add(txtDNI, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Nombres:"), gbc);
        gbc.gridx = 1;
        add(txtNombres, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        add(txtApellido, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Carrera:"), gbc);
        gbc.gridx = 1;
        add(txtCarrera, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Legajo:"), gbc);
        gbc.gridx = 1;
        add(txtLegajo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Foto Ruta:"), gbc);
        gbc.gridx = 1;
        add(txtFotoRuta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Domicilio:"), gbc);
        gbc.gridx = 1;
        add(txtDomicilio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Altura:"), gbc);
        gbc.gridx = 1;
        add(txtAltura, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        add(new JLabel("Localidad:"), gbc);
        gbc.gridx = 1;
        add(txtLocalidad, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        add(new JLabel("Provincia:"), gbc);
        gbc.gridx = 1;
        add(txtProvincia, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        add(new JLabel("Fecha Nacimiento:"), gbc);
        gbc.gridx = 1;
        add(txtFechaNacimiento, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        add(new JLabel("Año Ingreso:"), gbc);
        gbc.gridx = 1;
        add(txtAnoIngreso, gbc);

        gbc.gridx = 0;
        gbc.gridy = 12;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 13;
        add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        add(txtEstado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Centrar el botón
        add(btnGuardar, gbc);

        // Ajustar el tamaño de la ventana automáticamente
        pack();
        setLocationRelativeTo(null); // Centrar la ventana

        loadAlumnoData();

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAlumnoData();
            }
        });
    }

    private void loadAlumnoData() {
        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT * FROM alumnos WHERE dni = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dniOriginal);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        txtDNI.setText(rs.getString("dni"));
                        txtNombres.setText(rs.getString("nombres"));
                        txtApellido.setText(rs.getString("apellido"));
                        txtCarrera.setText(rs.getString("carrera"));
                        txtLegajo.setText(rs.getString("legajo"));
                        txtFotoRuta.setText(rs.getString("foto_ruta"));
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

    private void saveAlumnoData() {
        // Obtener los datos nuevos
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

        if (nombres.isEmpty() || apellido.isEmpty() || carrera.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombres, Apellido y Carrera son campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los datos actuales
        String datosAntiguos = getDatosAntiguos();

        // Construir el mensaje para el diálogo de confirmación con resaltado de cambios
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

        int confirm = JOptionPane.showConfirmDialog(
            this,
            mensajeConfirmacion,
            "Confirmar cambios",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );

        if (confirm == JOptionPane.OK_OPTION) {
            // Guardar los datos si el usuario confirma
            try (Connection conn = Conexion.getConnection()) {
                String sqlUpdate = "UPDATE alumnos SET nombres = ?, apellido = ?, carrera = ?, foto_ruta = ?, domicilio = ?, altura = ?, localidad = ?, provincia = ?, fecha_nacimiento = ?, ano_ingreso = ?, email = ?, estado = ? WHERE dni = ?";
                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
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
                    stmtUpdate.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Datos del alumno actualizados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar los datos del alumno: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getDatosAntiguos() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT * FROM alumnos WHERE dni = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dniOriginal);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
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
                            rs.getString("foto_ruta"),
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
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new EditarAlumno("12345678").setVisible(true));
    }
}
