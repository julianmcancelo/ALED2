import beltran.Clases.CarreraDAO;
import beltran.Clases.MateriaDAO;
import beltran.Clases.Profesor;
import beltran.Clases.ProfesorDAO;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GestionProfesores extends JFrame {
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField emailField;
    private JTextField telefonoField;
    private JComboBox<String> carreraComboBox;
    private JComboBox<String> materiaComboBox;
    private JComboBox<String> turnoComboBox;
    private JTable tablaProfesores;
    private DefaultTableModel modeloTabla;
    private ProfesorDAO profesorDAO;
    private CarreraDAO carreraDAO;
    private MateriaDAO materiaDAO;

    public GestionProfesores() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        profesorDAO = new ProfesorDAO();
        carreraDAO = new CarreraDAO();
        materiaDAO = new MateriaDAO();

        setTitle("Gestión de Profesores");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes

        // Inicializar campos de entrada
        nombreField = new JTextField(10);
        apellidoField = new JTextField(10);
        emailField = new JTextField(10);
        telefonoField = new JTextField(10);
        carreraComboBox = new JComboBox<>();
        materiaComboBox = new JComboBox<>();
        turnoComboBox = new JComboBox<>(new String[]{"Mañana", "Tarde", "Noche"});
        
        // Configuración de la tabla
        modeloTabla = new DefaultTableModel(new String[]{"Nombre", "Apellido", "Email", "Materia", "Carrera", "Turno"}, 0);
        tablaProfesores = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProfesores);
        tablaProfesores.setFillsViewportHeight(true);
        
        JButton agregarButton = new JButton("Agregar Profesor");
        JButton listarButton = new JButton("Listar Profesores");

        // Agregar componentes al JFrame
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; add(nombreField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1; add(apellidoField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; add(telefonoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; add(new JLabel("Carrera:"), gbc);
        gbc.gridx = 1; add(carreraComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Materia:"), gbc);
        gbc.gridx = 1; add(materiaComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Turno:"), gbc);
        gbc.gridx = 1; add(turnoComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1; add(agregarButton, gbc);
        gbc.gridx = 1; add(listarButton, gbc);
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; add(scrollPane, gbc);

        // Cargar carreras y materias en los JComboBox
        cargarCarreras();
        cargarMaterias();

        // Acciones de los botones
        agregarButton.addActionListener(e -> agregarProfesor());
        listarButton.addActionListener(e -> listarProfesores());
        
        // Ajustar la ventana
        pack();
        setLocationRelativeTo(null); // Centrar la ventana
        setResizable(true);
    }

    private void cargarCarreras() {
        try {
            List<String> carreras = carreraDAO.listarNombresCarreras();
            for (String carrera : carreras) {
                carreraComboBox.addItem(carrera);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMaterias() {
        try {
            List<String> materias = materiaDAO.listarNombresMaterias();
            for (String materia : materias) {
                materiaComboBox.addItem(materia);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarProfesor() {
        try {
            String nombre = nombreField.getText().trim();
            String apellido = apellidoField.getText().trim();
            String email = emailField.getText().trim();
            String telefono = telefonoField.getText().trim();
            String carreraSeleccionada = (String) carreraComboBox.getSelectedItem();
            String materiaSeleccionada = (String) materiaComboBox.getSelectedItem();
            String turno = (String) turnoComboBox.getSelectedItem();

            // Validaciones
            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (carreraSeleccionada == null || materiaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una carrera y una materia.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int carreraId = carreraDAO.obtenerIdPorNombre(carreraSeleccionada);
            int materiaId = materiaDAO.obtenerIdPorNombre(materiaSeleccionada);

            Profesor profesor = new Profesor(nombre, apellido, email, telefono, materiaId);
            profesor.setCarreraId(carreraId);
            profesor.setTurno(turno);

            profesorDAO.agregarProfesor(profesor);
            JOptionPane.showMessageDialog(null, "Profesor agregado");
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar el profesor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

private void listarProfesores() {
    try {
        List<Profesor> profesores = profesorDAO.listarProfesores();
        modeloTabla.setRowCount(0); // Limpiar tabla
        for (Profesor p : profesores) {
            int carreraId = p.getCarreraId();
            String carreraNombre = (carreraId != 0) ? carreraDAO.obtenerNombrePorId(carreraId) : "Carrera no asignada";
            String materiaNombre = materiaDAO.obtenerNombrePorId(p.getMateriaId()); // Obtener el nombre de la materia

            modeloTabla.addRow(new Object[]{
                p.getNombre(),
                p.getApellido(),
                p.getEmail(),
                materiaNombre, // Cambiado a nombre de materia
                carreraNombre,
                p.getTurno()
            });
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al listar profesores: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void clearFields() {
        nombreField.setText("");
        apellidoField.setText("");
        emailField.setText("");
        telefonoField.setText("");
        carreraComboBox.setSelectedIndex(0);
        materiaComboBox.setSelectedIndex(0);
        turnoComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionProfesores frame = new GestionProfesores();
            frame.setVisible(true);
        });
    }
}
