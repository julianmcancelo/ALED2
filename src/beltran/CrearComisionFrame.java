package beltran;

import beltran.Clases.Conexion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrearComisionFrame extends JInternalFrame {

    private JLabel lblNombre;
    private JTextField txtNombre;
    private JLabel lblCarrera;
    private JComboBox<String> cmbCarrera;
    private JLabel lblAno;
    private JTextField txtAno;
    private JLabel lblComision;
    private JTextField txtComision;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JPanel panel;

    public CrearComisionFrame() {
        super("Crear Comisión", true, true, true, true);
        setSize(400, 300);
        setLocation(50, 50); // Puedes ajustar la posición inicial
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Inicialización de componentes
        lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(20);
        lblCarrera = new JLabel("Carrera:");
        cmbCarrera = new JComboBox<>();
        lblAno = new JLabel("Año:");
        txtAno = new JTextField(4);
        lblComision = new JLabel("Comisión:");
        txtComision = new JTextField(20);
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        // Configuración del panel
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);

        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblCarrera, gbc);

        gbc.gridx = 1;
        panel.add(cmbCarrera, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblAno, gbc);

        gbc.gridx = 1;
        panel.add(txtAno, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblComision, gbc);

        gbc.gridx = 1;
        panel.add(txtComision, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnGuardar, gbc);

        gbc.gridy = 5;
        panel.add(btnCancelar, gbc);

        // Añadir panel al JInternalFrame
        add(panel);

        // Cargar carreras en el JComboBox
        cargarCarreras();

        // Añadir acciones a los botones
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarComision();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void cargarCarreras() {
        List<String> carreras = new ArrayList<>();
        carreras.add("Seleccione una carrera");

        try (Connection con = Conexion.getConnection()) {
            String sql = "SELECT nombre FROM carreras";
            try (PreparedStatement pst = con.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    String nombreCarrera = rs.getString("nombre");
                    carreras.add(nombreCarrera);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar las carreras: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Llenar el JComboBox con las carreras
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(carreras.toArray(new String[0]));
        cmbCarrera.setModel(model);
    }

    private void guardarComision() {
        String nombre = txtNombre.getText().trim();
        String carrera = (String) cmbCarrera.getSelectedItem();
        String ano = txtAno.getText().trim();
        String comision = txtComision.getText().trim();

        // Validar campos
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (carrera == null || carrera.equals("Seleccione una carrera")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una carrera.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ano.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El año es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (comision.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La comisión es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Guardar en la base de datos
        try (Connection con = Conexion.getConnection()) {
            String sql = "INSERT INTO comision (nombre, carrera, ano, comision) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, nombre);
                pst.setString(2, carrera);
                pst.setString(3, ano);
                pst.setString(4, comision);
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Comisión guardada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo guardar la comisión.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la comisión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
