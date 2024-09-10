package Materias;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableCellRenderer;

public class AsistenciaPanel2 extends JPanel {

    private JComboBox<String> cmbCarrera;
    private JComboBox<Integer> cmbAno;
    private JComboBox<String> cmbMateria;
    private JComboBox<String> cmbTurno;
    private JComboBox<String> cmbComision; // JComboBox para las comisiones
    private JTextField txtFecha;
    private JButton btnRegistrarAsistencia;
    private JTable tableAlumnos;
    private DefaultTableModel tableModel;
    private MateriaService materiaService;

    public AsistenciaPanel2(MateriaService materiaService) {
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

        addComponent(panelSeleccion, new JLabel("Comisión:"), 0, 5, gbc);
        cmbComision = new JComboBox<>();
        addComponent(panelSeleccion, cmbComision, 1, 5, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        btnRegistrarAsistencia = new JButton("Registrar Asistencia");
        btnRegistrarAsistencia.addActionListener(e -> confirmarRegistroAsistencia());
        panelSeleccion.add(btnRegistrarAsistencia, gbc);

        add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Apellido", "DNI", "Legajo", "Ano", "Presente"}, 0);
        tableAlumnos = new JTable(tableModel);
        tableAlumnos.setFillsViewportHeight(true);

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
        cmbCarrera.addActionListener(e -> {
            cargarMaterias();
        });
        cmbAno.addActionListener(e -> {
            try {
                cargarComisiones(); // Cargar comisiones cuando se selecciona un año
                cargarAlumnos(); // Cargar alumnos después de cargar las comisiones
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cmbMateria.addActionListener(e -> {
            try {
                cargarAlumnos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar alumnos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cmbComision.addActionListener(e -> {
            try {
                cargarAlumnos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar alumnos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

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
                cargarComisiones(); // Cargar comisiones después de cargar materias
                cargarAlumnos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar materias: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            cmbMateria.setModel(new DefaultComboBoxModel<>());
            cmbMateria.setEnabled(false);
        }
    }

  private void cargarComisiones() {
    String carrera = (String) cmbCarrera.getSelectedItem();
    Integer ano = (Integer) cmbAno.getSelectedItem();
    if (carrera != null && ano != null) {
        try {
            String[] comisiones = materiaService.obtenerComisiones(carrera, ano);
            cmbComision.setModel(new DefaultComboBoxModel<>(comisiones));
            cmbComision.setEnabled(true);
            cargarAlumnos(); // Cargar alumnos después de cargar las comisiones
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar comisiones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        cmbComision.setModel(new DefaultComboBoxModel<>());
        cmbComision.setEnabled(false);
    }
}

    private void cargarAlumnos() throws SQLException {
        String carrera = (String) cmbCarrera.getSelectedItem();
        Integer ano = (Integer) cmbAno.getSelectedItem();
        String materia = (String) cmbMateria.getSelectedItem();
        String comision = (String) cmbComision.getSelectedItem();

        if (carrera != null && ano != null && materia != null && comision != null) {
            String[] alumnos = materiaService.obtenerAlumnos(carrera, ano, materia, comision);
            updateAlumnosTable(alumnos);
        }
    }

    private void updateAlumnosTable(String[] alumnos) {
        tableModel.setRowCount(0);
        for (String alumno : alumnos) {
            String[] datos = alumno.split(",");
            if (datos.length == 7) {
                tableModel.addRow(new Object[]{
                        datos[0], // ID
                        datos[1], // Nombre
                        datos[2], // Apellido
                        datos[3], // DNI
                        datos[4], // Legajo
                        datos[5], // Año
                        false // Casilla de verificación
                });
            }
        }
    }

    private void confirmarRegistroAsistencia() {
        DefaultTableModel modelPresentes = new DefaultTableModel(new Object[]{"Legajo", "Nombre Completo"}, 0);
        DefaultTableModel modelAusentes = new DefaultTableModel(new Object[]{"Legajo", "Nombre Completo"}, 0);

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String legajo = (String) tableModel.getValueAt(i, 4);
            String nombre = (String) tableModel.getValueAt(i, 1);
            String apellido = (String) tableModel.getValueAt(i, 2);
            Boolean presente = (Boolean) tableModel.getValueAt(i, 6);

            if (presente != null && presente) {
                modelPresentes.addRow(new Object[]{legajo, nombre + " " + apellido});
            } else {
                modelAusentes.addRow(new Object[]{legajo, nombre + " " + apellido});
            }
        }

        JTable tablePresentes = new JTable(modelPresentes);
        JTable tableAusentes = new JTable(modelAusentes);

        JPanel panelResumen = new JPanel(new GridLayout(1, 2));
        panelResumen.add(new JScrollPane(tablePresentes));
        panelResumen.add(new JScrollPane(tableAusentes));

        JOptionPane.showMessageDialog(this, panelResumen, "Resumen de Asistencia", JOptionPane.INFORMATION_MESSAGE);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date fecha = new java.sql.Date(sdf.parse(txtFecha.getText()).getTime());

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String alumnoId = (String) tableModel.getValueAt(i, 0);
                String comision = (String) cmbComision.getSelectedItem();
                boolean presente = (Boolean) tableModel.getValueAt(i, 6);

                materiaService.registrarAsistencia(alumnoId, (String) cmbCarrera.getSelectedItem(), (String) cmbMateria.getSelectedItem(), (Integer) cmbAno.getSelectedItem(), (String) cmbTurno.getSelectedItem(), fecha, comision, presente);
            }

            JOptionPane.showMessageDialog(this, "Asistencia registrada con éxito.");
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Error en el formato de la fecha: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar asistencia: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
