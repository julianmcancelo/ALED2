package Materias;

import Materias.MateriaService;
import javax.swing.*;
import java.sql.SQLException;

public class CarreraComboBox extends JComboBox<String> {

    public CarreraComboBox(MateriaService materiaService) {
        super();
        cargarCarreras(materiaService);
    }

    private void cargarCarreras(MateriaService materiaService) {
        removeAllItems();
        try {
            String[] carreras = materiaService.obtenerCarreras();
            for (String carrera : carreras) {
                addItem(carrera);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
