package beltran.Clases;

import beltran.Clases.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MarcoReporte extends JFrame {

    private String materia;
    private String turno;
    private java.sql.Date fecha;

    public MarcoReporte(String materia, String turno, java.sql.Date fecha) {
        this.materia = materia;
        this.turno = turno;
        this.fecha = fecha;

        setTitle("Reporte de Asistencia");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarUI();
    }

    private void inicializarUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // Título del reporte
        panel.add(new JLabel("Reporte de Asistencia", SwingConstants.CENTER), BorderLayout.NORTH);

        // Crear el modelo de la tabla
        DefaultTableModel modeloReporte = new DefaultTableModel(new Object[]{"Legajo", "Nombre Completo", "Presente"}, 0);

        try (Connection conn = Conexion.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT legajo, nombre, apellido, presente " +
                "FROM asistencia " +
                "WHERE materia = ? AND turno = ? AND fecha = ?");
            ps.setString(1, materia);
            ps.setString(2, turno);
            ps.setDate(3, fecha);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String legajo = rs.getString("legajo");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                Boolean presente = rs.getBoolean("presente");

                modeloReporte.addRow(new Object[]{
                    legajo,
                    nombre + " " + apellido,
                    presente ? "Sí" : "No"
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Crear la tabla y agregarla al panel
        JTable tablaReporte = new JTable(modeloReporte);
        tablaReporte.setFillsViewportHeight(true);
        JScrollPane scrollPaneReporte = new JScrollPane(tablaReporte);
        panel.add(scrollPaneReporte, BorderLayout.CENTER);

        add(panel);
    }
}
