package beltran.Clases;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JFrame;

public class VentanaUtils {

    /**
     * Centra una ventana en la pantalla.
     * 
     * @param ventana La ventana que se debe centrar.
     */
    public static void centrarVentana(JFrame ventana) {
        // Obtén el tamaño de la pantalla
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gd.getDefaultConfiguration().getBounds();

        // Obtén el tamaño de la ventana
        int width = ventana.getWidth();
        int height = ventana.getHeight();

        // Calcula la posición central
        int x = (screenBounds.width - width) / 2;
        int y = (screenBounds.height - height) / 2;

        // Ajusta la ubicación de la ventana
        ventana.setLocation(x, y);
    }
}
