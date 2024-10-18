package beltran;

import beltran.Clases.Conexion;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SeleccionarAlumno extends JFrame {

    private JComboBox<String> cmbAlumnos; // ComboBox para mostrar la información de los alumnos que coinciden con la búsqueda
    private JButton btnEditar; // Botón para editar el alumno seleccionado
    private JTextField txtBuscar; // Campo de texto para buscar alumnos

    public SeleccionarAlumno() {
        // Configuración del marco
        setTitle("Seleccionar Alumno");
        setSize(800, 300); // Tamaño de la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana
        setLayout(new GridBagLayout()); // Usa GridBagLayout para la disposición de los componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los componentes se expanden horizontalmente

        // Inicialización de los componentes
        cmbAlumnos = new JComboBox<>();
        btnEditar = new JButton("Editar");
        txtBuscar = new JTextField(25);

        // Estilo del botón
        btnEditar.setBackground(new Color(0, 123, 255)); // Color de fondo del botón
        btnEditar.setForeground(Color.WHITE); // Color del texto del botón
        btnEditar.setFocusPainted(false); // Quita el borde de enfoque del botón

        // Agregar componentes al marco
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(new JLabel("Ingrese DNI, Apellido o Carrera:"), gbc); // Etiqueta para el campo de búsqueda

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(txtBuscar, gbc); // Campo de texto para buscar alumnos

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(new JLabel("Seleccione un Alumno:"), gbc); // Etiqueta para el ComboBox de alumnos

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(cmbAlumnos, gbc); // ComboBox para mostrar la información de los alumnos

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(btnEditar, gbc); // Botón para editar el alumno seleccionado

        // Cargar alumnos en el JComboBox
        loadAlumnos();

        // Agregar ActionListener al botón Editar
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String alumnoSeleccionado = (String) cmbAlumnos.getSelectedItem();
                if (alumnoSeleccionado != null) {
                    // Extraer el DNI del texto seleccionado
                    String dni = alumnoSeleccionado.split(" - ")[0];
                    // Mostrar ventana de edición con el DNI del alumno seleccionado
                    new EditarAlumno(dni).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(SeleccionarAlumno.this, "Seleccione un alumno para editar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Agregar ActionListener al campo de búsqueda
        txtBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarAlumnos();
            }
        });
    }

    /**
     * Carga todos los alumnos en el JComboBox.
     */
    private void loadAlumnos() {
        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT dni, apellido, carrera FROM alumnos";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                cmbAlumnos.removeAllItems(); // Limpia el JComboBox antes de añadir nuevos elementos
                while (rs.next()) {
                    String alumnoInfo = rs.getString("dni") + " - " + rs.getString("apellido") + " - " + rs.getString("carrera");
                    cmbAlumnos.addItem(alumnoInfo);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los alumnos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filtra los alumnos en el JComboBox basado en el texto de búsqueda.
     */
    private void filtrarAlumnos() {
        String textoBusqueda = txtBuscar.getText().trim(); // Obtiene el texto de búsqueda

        if (textoBusqueda.isEmpty()) {
            // Si el campo de búsqueda está vacío, recargar todos los alumnos
            loadAlumnos();
            return;
        }

        String sql = "SELECT dni, apellido, carrera FROM alumnos WHERE dni LIKE ? OR apellido LIKE ? OR carrera LIKE ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String busquedaParam = "%" + textoBusqueda + "%";
            stmt.setString(1, busquedaParam);
            stmt.setString(2, busquedaParam);
            stmt.setString(3, busquedaParam);
            try (ResultSet rs = stmt.executeQuery()) {
                cmbAlumnos.removeAllItems(); // Limpia el JComboBox antes de añadir nuevos elementos
                if (!rs.isBeforeFirst()) { // Verifica si hay resultados
                    JOptionPane.showMessageDialog(this, "No se encontraron resultados para la búsqueda.", "Información", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    while (rs.next()) {
                        String alumnoInfo = rs.getString("dni") + " - " + rs.getString("apellido") + " - " + rs.getString("carrera");
                        cmbAlumnos.addItem(alumnoInfo);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar los alumnos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Configuración del Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Usa SwingUtilities para asegurar que el código de Swing se ejecute en el hilo de eventos
        SwingUtilities.invokeLater(() -> new SeleccionarAlumno().setVisible(true));
    }
}
