package Materias;

import beltran.Clases.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;

public class AsistenciaPanel extends JPanel {

    private JComboBox<String> cmbCarrera;
    private JComboBox<Integer> cmbAno;
    private JComboBox<String> cmbMateria;
    private JComboBox<String> cmbTurno;
    private JTextField txtFecha;
    private JButton btnRegistrarAsistencia;
    private JTable tableAlumnos;
    private DefaultTableModel tableModel;
    private MateriaService materiaService;

    public AsistenciaPanel(MateriaService materiaService) {
        this.materiaService = materiaService;
        initializeUI();
    }

    private void initializeUI() {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JPanel panelSeleccion = new JPanel(new GridBagLayout());
    panelSeleccion.setBorder(BorderFactory.createTitledBorder("Seleccionar Información"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    addComponent(panelSeleccion, new JLabel("Carrera:"), 0, 0, gbc);
    cmbCarrera = new JComboBox<>();
    addComponent(panelSeleccion, cmbCarrera, 1, 0, gbc);

    addComponent(panelSeleccion, new JLabel("Año:"), 0, 1, gbc);
    cmbAno = new JComboBox<>(new Integer[]{1, 2, 3});
    addComponent(panelSeleccion, cmbAno, 1, 1, gbc);

    addComponent(panelSeleccion, new JLabel("Materia:"), 0, 2, gbc);
    cmbMateria = new JComboBox<>();
    cmbMateria.setEnabled(false);
    addComponent(panelSeleccion, cmbMateria, 1, 2, gbc);

    addComponent(panelSeleccion, new JLabel("Turno:"), 0, 3, gbc);
    cmbTurno = new JComboBox<>(new String[]{"Mañana", "Noche"});
    addComponent(panelSeleccion, cmbTurno, 1, 3, gbc);

    addComponent(panelSeleccion, new JLabel("Fecha:"), 0, 4, gbc);
    txtFecha = new JTextField(10);
    addComponent(panelSeleccion, txtFecha, 1, 4, gbc);

    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    btnRegistrarAsistencia = new JButton("Registrar Asistencia");
    btnRegistrarAsistencia.addActionListener(e -> confirmarRegistroAsistencia());
    panelSeleccion.add(btnRegistrarAsistencia, gbc);

    add(panelSeleccion, BorderLayout.NORTH);

    JPanel panelTabla = new JPanel(new BorderLayout());
    tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Apellido", "DNI", "Legajo", "Ano", "Presente"}, 0);
    tableAlumnos = new JTable(tableModel);
    tableAlumnos.setFillsViewportHeight(true);

    // Configura el renderizador y editor para la columna de presencia
    tableAlumnos.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JCheckBox checkBox = new JCheckBox();
            if (value instanceof Boolean) {
                checkBox.setSelected((Boolean) value);
            }
            return checkBox;
        }
    });
    tableAlumnos.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JCheckBox()));

    JScrollPane scrollPane = new JScrollPane(tableAlumnos);
    panelTabla.add(scrollPane, BorderLayout.CENTER);

    add(panelTabla, BorderLayout.CENTER);

    // Añadir listeners para actualizar los datos al seleccionar opciones
    cmbCarrera.addActionListener(e -> cargarMaterias());
    cmbAno.addActionListener(e -> cargarAlumnos());
    cmbMateria.addActionListener(e -> cargarAlumnos());

    // Cargar carreras al inicializar el panel
    cargarCarreras();
}


    private void addComponent(JPanel panel, Component component, int gridx, int gridy, GridBagConstraints gbc) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(component, gbc);
    }

    private void cargarCarreras() {
        try {
            String[] carreras = materiaService.obtenerCarreras();
            cmbCarrera.setModel(new DefaultComboBoxModel<>(carreras));
            // Si hay una carrera seleccionada, carga las materias correspondientes
            if (cmbCarrera.getItemCount() > 0) {
                cargarMaterias();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMaterias() {
        String carrera = (String) cmbCarrera.getSelectedItem();
        if (carrera != null) {
            try {
                String[] materias = materiaService.obtenerMaterias(carrera);
                cmbMateria.setModel(new DefaultComboBoxModel<>(materias));
                cmbMateria.setEnabled(true);
                cargarAlumnos(); // Cargar alumnos al seleccionar una materia
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            cmbMateria.setModel(new DefaultComboBoxModel<>());
            cmbMateria.setEnabled(false);
        }
    }

    private void cargarAlumnos() {
        String carrera = (String) cmbCarrera.getSelectedItem();
        Integer ano = (Integer) cmbAno.getSelectedItem();

        if (carrera != null && ano != null) {
            try {
                String[] alumnos = materiaService.obtenerAlumnos(carrera, ano);
                updateAlumnosTable(alumnos);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar alumnos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateAlumnosTable(String[] alumnos) {
        tableModel.setRowCount(0); // Limpiar tabla
        for (String alumno : alumnos) {
            String[] datos = alumno.split(","); // Asumiendo que los datos vienen separados por coma
            if (datos.length == 6) { // Ajustar el tamaño del array para incluir 'ano'
                tableModel.addRow(new Object[]{
                    datos[0], // ID
                    datos[1], // Nombre
                    datos[2], // Apellido
                    datos[3], // DNI
                    datos[4], // Legajo
                    datos[5], // Ano
                    false // Casilla de verificación para marcar la asistencia
                });
            }
        }
    }
private void confirmarRegistroAsistencia() {
    // Recopila los datos de los alumnos para mostrar en el diálogo
    DefaultTableModel modelPresentes = new DefaultTableModel(new Object[]{"Legajo", "Nombre Completo"}, 0);
    DefaultTableModel modelAusentes = new DefaultTableModel(new Object[]{"Legajo", "Nombre Completo"}, 0);

    for (int i = 0; i < tableModel.getRowCount(); i++) {
        String legajo = (String) tableModel.getValueAt(i, 4); // Columna de legajo
        String nombre = (String) tableModel.getValueAt(i, 1);
        String apellido = (String) tableModel.getValueAt(i, 2);
        Boolean presente = (Boolean) tableModel.getValueAt(i, 6); // Columna de presencia

        if (presente != null && presente) {
            modelPresentes.addRow(new Object[]{legajo, nombre + " " + apellido});
        } else {
            modelAusentes.addRow(new Object[]{legajo, nombre + " " + apellido});
        }
    }

    // Crear tablas para los presentes y ausentes
    JTable tablePresentes = new JTable(modelPresentes);
    JTable tableAusentes = new JTable(modelAusentes);

    tablePresentes.setFillsViewportHeight(true);
    tableAusentes.setFillsViewportHeight(true);

    JScrollPane scrollPanePresentes = new JScrollPane(tablePresentes);
    JScrollPane scrollPaneAusentes = new JScrollPane(tableAusentes);

    // Ajustar el tamaño de las tablas
    tablePresentes.setPreferredScrollableViewportSize(new Dimension(300, 80));
    tableAusentes.setPreferredScrollableViewportSize(new Dimension(300, 80));

    // Crear panel con un diseño más compacto
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2); // Ajustar el espaciado
    gbc.fill = GridBagConstraints.HORIZONTAL;
    
    // Leyenda de Presentes
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    JLabel lblPresentes = new JLabel("Presentes:");
    lblPresentes.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(lblPresentes, gbc);

    gbc.gridy = 1;
    gbc.gridwidth = 2;
    panel.add(scrollPanePresentes, gbc);

    // Leyenda de Ausentes
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    JLabel lblAusentes = new JLabel("Ausentes:");
    lblAusentes.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(lblAusentes, gbc);

    gbc.gridy = 3;
    gbc.gridwidth = 2;
    panel.add(scrollPaneAusentes, gbc);

    int opcion = JOptionPane.showConfirmDialog(this, panel, "Confirmar Asistencia", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

    if (opcion == JOptionPane.OK_OPTION) {
        try {
            registrarAsistencia();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar asistencia: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



    private void registrarAsistencia() throws SQLException {
        String turno = (String) cmbTurno.getSelectedItem();
        String fechaTexto = txtFecha.getText();

        if (turno != null && !fechaTexto.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date fechaUtil = sdf.parse(fechaTexto);
                java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());

                String materia = (String) cmbMateria.getSelectedItem();

                try (Connection conn = Conexion.getConnection()) {
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        Boolean presente = (Boolean) tableModel.getValueAt(i, 6); // Columna de presencia
                        if (presente != null && presente) {
                            String alumnoId = (String) tableModel.getValueAt(i, 0);
                            materiaService.registrarAsistencia(alumnoId, (String) cmbCarrera.getSelectedItem(), materia, (Integer) cmbAno.getSelectedItem(), turno, fechaSql, presente);
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Asistencia registrada exitosamente.");
                }
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Error en el formato de la fecha: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}
