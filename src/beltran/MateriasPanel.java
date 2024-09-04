package beltran;

import Materias.CarreraComboBox;
import Materias.MateriaService;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MateriasPanel extends JPanel {

    private CarreraComboBox cmbCarrera;
    private AnoComboBox cmbAno;
    private JTextArea txtMaterias;
    private JTextField txtNuevaMateria;
    private MateriaService materiaService;
    private JComboBox<String> cmbMateria;

    public MateriasPanel(MateriaService materiaService) {
        this.materiaService = materiaService;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para seleccionar carrera y año
        JPanel panelSeleccion = new JPanel();
        panelSeleccion.setLayout(new GridBagLayout());
        panelSeleccion.setBorder(BorderFactory.createTitledBorder("Seleccionar Materias"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelSeleccion.add(new JLabel("Carrera:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        cmbCarrera = new CarreraComboBox(materiaService);
        panelSeleccion.add(cmbCarrera, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelSeleccion.add(new JLabel("Año:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        cmbAno = new AnoComboBox();
        panelSeleccion.add(cmbAno, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton btnCargarMaterias = new JButton("Cargar Materias");
        btnCargarMaterias.setBackground(new Color(0, 123, 255));
        btnCargarMaterias.setForeground(Color.WHITE);
        btnCargarMaterias.setFocusPainted(false);
        btnCargarMaterias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarMaterias();
            }
        });
        panelSeleccion.add(btnCargarMaterias, gbc);

        add(panelSeleccion, BorderLayout.NORTH);

        txtMaterias = new JTextArea();
        txtMaterias.setEditable(false);
        txtMaterias.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(new JScrollPane(txtMaterias), BorderLayout.CENTER);

        JPanel panelRegistro = new JPanel();
        panelRegistro.setLayout(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registrar Nueva Materia"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelRegistro.add(new JLabel("Nueva Materia:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtNuevaMateria = new JTextField(20);
        panelRegistro.add(txtNuevaMateria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelRegistro.add(new JLabel("Carrera:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JComboBox<String> cmbRegistroCarrera = new CarreraComboBox(materiaService);
        panelRegistro.add(cmbRegistroCarrera, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelRegistro.add(new JLabel("Año:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JComboBox<Integer> cmbRegistroAno = new AnoComboBox();
        panelRegistro.add(cmbRegistroAno, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton btnRegistrarMateria = new JButton("Registrar Materia");
        btnRegistrarMateria.setBackground(new Color(0, 123, 255));
        btnRegistrarMateria.setForeground(Color.WHITE);
        btnRegistrarMateria.setFocusPainted(false);
        btnRegistrarMateria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarMateria(
                    (String) cmbRegistroCarrera.getSelectedItem(),
                    (Integer) cmbRegistroAno.getSelectedItem(),
                    txtNuevaMateria.getText()
                );
            }
        });
        panelRegistro.add(btnRegistrarMateria, gbc);

        add(panelRegistro, BorderLayout.SOUTH);

        // Inicializar y agregar cmbMateria al panel si es necesario
        cmbMateria = new JComboBox<>();
        cmbMateria.setEnabled(false);
        add(cmbMateria, BorderLayout.EAST);
    }

    private void cargarMaterias() {
        String carrera = (String) cmbCarrera.getSelectedItem();
        if (carrera == null || carrera.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una carrera válida.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String[] materias = materiaService.obtenerMaterias(carrera);
            if (materias != null && materias.length > 0) {
                cmbMateria.setModel(new DefaultComboBoxModel<>(materias));
                cmbMateria.setEnabled(true);
            } else {
                cmbMateria.setModel(new DefaultComboBoxModel<>());
                cmbMateria.setEnabled(false);
                JOptionPane.showMessageDialog(this, "No se encontraron materias para la carrera seleccionada.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarMateria(String carrera, Integer ano, String materia) {
        if (carrera == null || ano == null || materia == null || materia.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos correctamente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            materiaService.registrarMateria(carrera, ano, materia);
            JOptionPane.showMessageDialog(this, "Materia registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            txtNuevaMateria.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar materia: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());

                MateriaService materiaService = new MateriaService();
                JFrame frame = new JFrame("Gestión de Materias");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 400);
                frame.add(new MateriasPanel(materiaService));
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No se pudo aplicar el tema: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
