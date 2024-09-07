package beltran.Clases;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportarReporteExcel {

    public static void exportarReporte(String materia, String turno, java.sql.Date fecha, String rutaArchivo) {
        try (Workbook libro = new XSSFWorkbook()) {
            Sheet hoja = libro.createSheet("Reporte de Asistencia");

            // Crear estilos de celda
            CellStyle estiloCabecera = libro.createCellStyle();
            Font fuenteCabecera = libro.createFont();
            fuenteCabecera.setBold(true);
            estiloCabecera.setFont(fuenteCabecera);

            CellStyle estiloCelda = libro.createCellStyle();

            // Crear la fila de cabeceras
            Row filaCabecera = hoja.createRow(0);
            String[] cabeceras = {"Legajo", "Nombre Completo", "Presente"};
            for (int i = 0; i < cabeceras.length; i++) {
                Cell celda = filaCabecera.createCell(i);
                celda.setCellValue(cabeceras[i]);
                celda.setCellStyle(estiloCabecera);
            }

            // Obtener los datos y añadir filas
            try (Connection conn = Conexion.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT legajo, nombre, apellido, presente " +
                        "FROM asistencia " +
                        "WHERE materia = ? AND turno = ? AND fecha = ?");
                ps.setString(1, materia);
                ps.setString(2, turno);
                ps.setDate(3, fecha);

                ResultSet rs = ps.executeQuery();
                int rowIndex = 1;
                while (rs.next()) {
                    Row fila = hoja.createRow(rowIndex++);
                    String legajo = rs.getString("legajo");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    Boolean presente = rs.getBoolean("presente");

                    Cell celdaLegajo = fila.createCell(0);
                    celdaLegajo.setCellValue(legajo);

                    Cell celdaNombre = fila.createCell(1);
                    celdaNombre.setCellValue(nombre + " " + apellido);

                    Cell celdaPresente = fila.createCell(2);
                    celdaPresente.setCellValue(presente ? "Sí" : "No");

                    // Aplicar estilo a las celdas
                    celdaLegajo.setCellStyle(estiloCelda);
                    celdaNombre.setCellStyle(estiloCelda);
                    celdaPresente.setCellStyle(estiloCelda);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Ajustar el tamaño de las columnas
            for (int i = 0; i < 3; i++) {
                hoja.autoSizeColumn(i);
            }

            // Guardar el archivo Excel
            try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                libro.write(fileOut);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al exportar el reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al crear el archivo Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
