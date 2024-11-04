package Materias;

import beltran.Clases.Conexion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.formdev.flatlaf.FlatLightLaf;

public class CarrerasPanel extends JPanel {

    private JTextField txtNuevaCarrera;
    private JComboBox<String> cmbCarreras;

    public CarrerasPanel(MateriaService materiaService) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para crear nuevas carreras
        JPanel panelCreacion = new JPanel();
        panelCreacion.setLayout(new GridBagLayout());
        panelCreacion.setBorder(BorderFactory.createTitledBorder("Crear Nueva Carrera"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
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
        btnRegistrarCarrera.setBackground(new Color(34, 150, 243));
        btnRegistrarCarrera.setForeground(Color.WHITE);
        btnRegistrarCarrera.setFocusPainted(false);
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
        panelSeleccion.setBorder(BorderFactory.createTitledBorder("Cargar Carreras"));

        cmbCarreras = new JComboBox<>();
        cmbCarreras.setPreferredSize(new Dimension(200, 25));
        cargarCarreras(cmbCarreras);
        panelSeleccion.add(cmbCarreras, BorderLayout.CENTER);

        JButton btnCargarCarreras = new JButton("Cargar Carreras");
        btnCargarCarreras.setBackground(new Color(34, 150, 243));
        btnCargarCarreras.setForeground(Color.WHITE);
        btnCargarCarreras.setFocusPainted(false);
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
        comboBox.removeAllItems();
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
                    txtNuevaCarrera.setText("");
                    cargarCarreras(cmbCarreras);
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

                // Crear un JFrame principal
                JFrame mainFrame = new JFrame("Aplicación de Gestión");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(800, 600);
                
                // Crear un JDesktopPane
                JDesktopPane desktopPane = new JDesktopPane();
                mainFrame.add(desktopPane);

                // Crear un JInternalFrame para CarrerasPanel
                JInternalFrame gestionCarrerasFrame = new JInternalFrame("Gestión de Carreras", true, true, true, true);
                gestionCarrerasFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
                gestionCarrerasFrame.setSize(600, 400);
                gestionCarrerasFrame.add(new CarrerasPanel(new MateriaService())); // Pasar MateriaService
                gestionCarrerasFrame.setVisible(true);

                // Añadir el JInternalFrame al JDesktopPane
                desktopPane.add(gestionCarrerasFrame);

                // Hacer visible el JFrame principal
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No se pudo aplicar el tema: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
