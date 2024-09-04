

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class InformacionAlumno extends JFrame {
    private JLabel lblNombres;
    private JLabel lblApellido;
    private JLabel lblDNI;
    private JLabel lblLegajo;
    private JLabel lblCarrera;
    private JLabel lblDomicilio;
    private JLabel lblAltura;
    private JLabel lblLocalidad;
    private JLabel lblProvincia;
    private JLabel lblFechaNac;
    private JLabel lblAnoIngreso;
    private JLabel lblEmail;
    private JButton btnEditar;
    private JButton btnBorrar;
    private String dni;
    private String userRole;

    public InformacionAlumno(ResultSet rs, String userRole) throws SQLException {
        this.userRole = userRole;
        setTitle("Información del Alumno");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new GridLayout(13, 2));

        // Inicializar etiquetas con datos del ResultSet
        lblNombres = new JLabel(rs.getString("nombres"));
        lblApellido = new JLabel(rs.getString("apellido"));
        lblDNI = new JLabel(rs.getString("dni"));
        lblLegajo = new JLabel(rs.getString("legajo"));
        lblCarrera = new JLabel(rs.getString("carrera"));
        lblDomicilio = new JLabel(rs.getString("domicilio"));
        lblAltura = new JLabel(rs.getString("altura"));
        lblLocalidad = new JLabel(rs.getString("localidad"));
        lblProvincia = new JLabel(rs.getString("provincia"));
        lblFechaNac = new JLabel(rs.getDate("fecha_nacimiento").toString());
        lblAnoIngreso = new JLabel(String.valueOf(rs.getInt("ano_ingreso")));
        lblEmail = new JLabel(rs.getString("email"));

        // Botones de acción
        btnEditar = new JButton("Editar");
        btnBorrar = new JButton("Borrar");

        if (userRole.equals("Administrador")) {
            add(btnEditar);
            add(btnBorrar);

            btnEditar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Implementar lógica de edición
                    // Llama a un método que abra un formulario de edición
                }
            });

            btnBorrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(
                            InformacionAlumno.this,
                            "¿Estás seguro de que deseas borrar este alumno?",
                            "Confirmar borrado",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        borrarAlumno(dni);
                    }
                }
            });
        }

        // Agregar etiquetas al JFrame
        add(new JLabel("Nombres:"));
        add(lblNombres);
        add(new JLabel("Apellido:"));
        add(lblApellido);
        add(new JLabel("DNI:"));
        add(lblDNI);
        add(new JLabel("Legajo:"));
        add(lblLegajo);
        add(new JLabel("Carrera:"));
        add(lblCarrera);
        add(new JLabel("Domicilio:"));
        add(lblDomicilio);
        add(new JLabel("Altura:"));
        add(lblAltura);
        add(new JLabel("Localidad:"));
        add(lblLocalidad);
        add(new JLabel("Provincia:"));
        add(lblProvincia);
        add(new JLabel("Fecha Nac.:"));
        add(lblFechaNac);
        add(new JLabel("Año Ingreso:"));
        add(lblAnoIngreso);
        add(new JLabel("Email:"));
        add(lblEmail);
    }

    private void borrarAlumno(String dni) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/beltran", "root", "");
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Alumnos WHERE dni = ?")) {

            ps.setString(1, dni);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Alumno borrado exitosamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el alumno para borrar.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al borrar el alumno: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
