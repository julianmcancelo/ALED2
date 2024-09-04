package Materias;

import beltran.Clases.Conexion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.formdev.flatlaf.FlatLightLaf; // Importa el look and feel de FlatLaf en modo claro

public class CarrerasPanel extends JPanel {

    private JTextField txtNuevaCarrera;
    private JComboBox<String> cmbCarreras;

    public CarrerasPanel(MateriaService materiaService) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Añadir un borde vacío alrededor del panel

        // Panel para crear nuevas carreras
        JPanel panelCreacion = new JPanel();
        panelCreacion.setLayout(new GridBagLayout());
        panelCreacion.setBorder(BorderFactory.createTitledBorder("Crear Nueva Carrera")); // Añadir borde con título

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCreacion.add(new JLabel("Nombre de la Carrera:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtNuevaCarrera = new JTextField(20);
        panelCreacion.add(txtNuevaCarrera, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JButton btnRegistrarCarrera = new JButton("Registrar Carrera");
        btnRegistrarCarrera.setBackground(new Color(34, 150, 243)); // Color de fondo del botón
        btnRegistrarCarrera.setForeground(Color.WHITE); // Color del texto del botón
        btnRegistrarCarrera.setFocusPainted(false); // Quitar el borde del botón al recibir el foco
        btnRegistrarCarrera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarCarrera(txtNuevaCarrera.getText());
            }
        });
        panelCreacion.add(btnRegistrarCarrera, gbc);

        // Panel para seleccionar y cargar carreras
        JPanel panelSeleccion = new JPanel();
        panelSeleccion.setLayout(new BorderLayout());
        panelSeleccion.setBorder(BorderFactory.createTitledBorder("Cargar Carreras")); // Añadir borde con título

        cmbCarreras = new JComboBox<>();
        cmbCarreras.setPreferredSize(new Dimension(200, 25));
        cargarCarreras(cmbCarreras);
        panelSeleccion.add(cmbCarreras, BorderLayout.CENTER);

        JButton btnCargarCarreras = new JButton("Cargar Carreras");
        btnCargarCarreras.setBackground(new Color(34, 150, 243)); // Color de fondo del botón
        btnCargarCarreras.setForeground(Color.WHITE); // Color del texto del botón
        btnCargarCarreras.setFocusPainted(false); // Quitar el borde del botón al recibir el foco
        btnCargarCarreras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarCarreras(cmbCarreras);
            }
        });
        panelSeleccion.add(btnCargarCarreras, BorderLayout.EAST);

        // Añadir los paneles al panel principal
        add(panelCreacion, BorderLayout.NORTH);
        add(panelSeleccion, BorderLayout.SOUTH);
    }

    private void cargarCarreras(JComboBox<String> comboBox) {
        comboBox.removeAllItems(); // Limpiar el comboBox antes de cargar nuevos elementos
        try (var conn = Conexion.getConnection()) {
            String sql = "SELECT nombre FROM carreras";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    comboBox.addItem(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarCarrera(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            try (var conn = Conexion.getConnection()) {
                String sql = "INSERT INTO carreras (nombre) VALUES (?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, nombre);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Carrera registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    txtNuevaCarrera.setText(""); // Limpiar el campo de texto
                    cargarCarreras(cmbCarreras); // Actualizar la lista de carreras
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al registrar carrera: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un nombre de carrera válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            // Configurar el look and feel de FlatLaf en modo claro
            UIManager.setLookAndFeel(new FlatLightLaf());

            // Crear una instancia de MateriaService
            MateriaService materiaService = new MateriaService();

            JFrame frame = new JFrame("Gestión de Carreras");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            frame.add(new CarrerasPanel(materiaService));
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo aplicar el tema: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}
}
