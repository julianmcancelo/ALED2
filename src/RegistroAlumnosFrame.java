import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class RegistroAlumnosFrame extends JFrame {
    private JTextField txtNombres, txtApellido, txtDNI, txtLegajo, txtDomicilio, txtAltura, txtLocalidad, txtProvincia, txtFechaNacimiento, txtAnoIngreso, txtEmail;
    private JComboBox<String> cmbCarrera, cmbEstado, cmbTurno;
    private JLabel lblFoto;
    private JButton btnRegistrar, btnCancelar, btnSeleccionarFoto;
    private File fotoArchivo;
    private Map<String, Integer> carreraMap; // Mapa para relacionar nombres de carrera con ID
    private int idCarreraSeleccionada;

    public RegistroAlumnosFrame() {
        initUI();
        cargarCarreras();
    }

    private void initUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("Registro de Alumnos");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicialización de campos de texto
        txtNombres = new JTextField(20);
        txtApellido = new JTextField(20);
        txtDNI = new JTextField(20);
        txtLegajo = new JTextField(20);
        txtDomicilio = new JTextField(20);
        txtAltura = new JTextField(20);
        txtLocalidad = new JTextField(20);
        txtProvincia = new JTextField(20);
        txtFechaNacimiento = new JTextField(20);
        txtAnoIngreso = new JTextField(20);
        txtEmail = new JTextField(20);

        // Etiquetas y campos de texto
        String[] labels = {"Nombres:", "Apellido:", "DNI:", "Legajo:", "Domicilio:", "Altura:", "Localidad:", "Provincia:", "Fecha Nacimiento:", "Año Ingreso:", "Email:", "Carrera:", "Estado:", "Turno:"};
        JTextField[] textFields = {txtNombres, txtApellido, txtDNI, txtLegajo, txtDomicilio, txtAltura, txtLocalidad, txtProvincia, txtFechaNacimiento, txtAnoIngreso, txtEmail};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            if (i < 11) {
                formPanel.add(textFields[i], gbc);
            } else if (i == 11) {
                cmbCarrera = new JComboBox<>();
                formPanel.add(cmbCarrera, gbc);
            } else if (i == 12) {
                cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
                formPanel.add(cmbEstado, gbc);
            } else if (i == 13) {
                cmbTurno = new JComboBox<>(new String[]{"Mañana", "Tarde", "Noche"});
                formPanel.add(cmbTurno, gbc);
            }
        }

        // Panel de foto
        JPanel fotoPanel = new JPanel();
        fotoPanel.setBorder(BorderFactory.createTitledBorder("Foto"));
        lblFoto = new JLabel("Foto Aquí");
        lblFoto.setPreferredSize(new Dimension(150, 150));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        fotoPanel.add(lblFoto);

        btnSeleccionarFoto = new JButton("Seleccionar Foto");
        fotoPanel.add(btnSeleccionarFoto);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(Box.createHorizontalGlue());

        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnRegistrar);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(btnCancelar);

        // Agregar paneles al panel principal
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(fotoPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Acción del botón de seleccionar foto
        btnSeleccionarFoto.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes JPG", "jpg", "jpeg", "png"));
            if (fileChooser.showOpenDialog(RegistroAlumnosFrame.this) == JFileChooser.APPROVE_OPTION) {
                fotoArchivo = fileChooser.getSelectedFile();
                updateFotoPreview();
            }
        });

        // Acción del botón de registrar
        btnRegistrar.addActionListener(e -> {
            if (validateFields()) {
                int confirmation = JOptionPane.showConfirmDialog(
                        RegistroAlumnosFrame.this,
                        "¿Está seguro de que desea registrar al alumno?",
                        "Confirmación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (confirmation == JOptionPane.YES_OPTION) {
                    registrarAlumno();
                }
            }
        });

        // Acción del botón de cancelar
        btnCancelar.addActionListener(e -> dispose());
    }

    private void updateFotoPreview() {
        if (fotoArchivo != null) {
            try {
                BufferedImage img = ImageIO.read(fotoArchivo);
                ImageIcon icon = new ImageIcon(img.getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH));
                lblFoto.setIcon(icon);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

 private void registrarAlumno() {
    if (validateFields()) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO alumnos (nombres, apellido, dni, legajo, domicilio, altura, localidad, provincia, fecha_nacimiento, ano_ingreso, email, carrera, estado, turno, ruta_foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtNombres.getText());
                ps.setString(2, txtApellido.getText());
                ps.setString(3, txtDNI.getText());
                ps.setString(4, txtLegajo.getText());
                ps.setString(5, txtDomicilio.getText());
                ps.setString(6, txtAltura.getText());
                ps.setString(7, txtLocalidad.getText());
                ps.setString(8, txtProvincia.getText());
                ps.setDate(9, new java.sql.Date(parseDate(txtFechaNacimiento.getText()).getTime()));
                ps.setInt(10, Integer.parseInt(txtAnoIngreso.getText()));
                ps.setString(11, txtEmail.getText());
                ps.setString(12, (String) cmbCarrera.getSelectedItem()); // Guardar el nombre de la carrera
                ps.setString(13, (String) cmbEstado.getSelectedItem());
                ps.setString(14, (String) cmbTurno.getSelectedItem());
                ps.setString(15, saveFoto());

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Alumno registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el alumno: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    private boolean validateFields() {
        // Verificar que todos los campos estén completos y no sean nulos
        if (txtNombres == null || txtApellido == null || txtDNI == null || txtLegajo == null || txtDomicilio == null || txtAltura == null || txtLocalidad == null || txtProvincia == null || txtFechaNacimiento == null || txtAnoIngreso == null || txtEmail == null || cmbCarrera == null || cmbEstado == null || cmbTurno == null) {
            JOptionPane.showMessageDialog(this, "Error: Algunos campos no están inicializados.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtNombres.getText().isEmpty() || txtApellido.getText().isEmpty() || txtDNI.getText().isEmpty() || txtLegajo.getText().isEmpty() || txtDomicilio.getText().isEmpty() || txtAltura.getText().isEmpty() || txtLocalidad.getText().isEmpty() || txtProvincia.getText().isEmpty() || txtFechaNacimiento.getText().isEmpty() || txtAnoIngreso.getText().isEmpty() || txtEmail.getText().isEmpty() || cmbCarrera.getSelectedItem() == null || cmbEstado.getSelectedItem() == null || cmbTurno.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Verificar formato de fecha
        try {
            parseDate(txtFechaNacimiento.getText());
        } catch (Exception e) {
            return false;
        }

        // Verificar formato de año
        try {
            Integer.parseInt(txtAnoIngreso.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El año de ingreso debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://190.106.131.13:3306/beltran?useSSL=false";
        String user = " ";
        String password = "feelthesky1";
        return DriverManager.getConnection(url, user, password);
    }

    private String saveFoto() {
        if (fotoArchivo != null) {
            File destino = new File("fotos/" + fotoArchivo.getName());
            try {
                BufferedImage img = ImageIO.read(fotoArchivo);
                ImageIO.write(img, "jpg", destino);
                return destino.getAbsolutePath();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    private java.util.Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void cargarCarreras() {
        carreraMap = new HashMap<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nombre FROM carreras")) {

            cmbCarrera.removeAllItems();
            while (rs.next()) {
                String nombreCarrera = rs.getString("nombre");
                int idCarrera = rs.getInt("id");
                cmbCarrera.addItem(nombreCarrera);
                carreraMap.put(nombreCarrera, idCarrera); // Guardar la relación nombre-ID
            }

                                    cmbCarrera.addActionListener(e -> {
    String carreraSeleccionada = (String) cmbCarrera.getSelectedItem();
    idCarreraSeleccionada = carreraMap.getOrDefault(carreraSeleccionada, -1); // Obtener el ID de la carrera seleccionada
});
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las carreras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistroAlumnosFrame().setVisible(true));
    }
}
