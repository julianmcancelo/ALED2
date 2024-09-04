import Materias.MateriaService;
import javax.swing.*;
import java.sql.SQLException;

public class CarreraComboBox extends JComboBox<String> {
    private MateriaService materiaService;

    public CarreraComboBox(MateriaService materiaService) {
        this.materiaService = materiaService;
        cargarCarreras();
    }

    private void cargarCarreras() {
        try {
            String[] carreras = materiaService.obtenerCarreras();
            if (carreras != null) {
                setModel(new DefaultComboBoxModel<>(carreras));
            } else {
                JOptionPane.showMessageDialog(this, "No se pudieron cargar las carreras.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
