package beltran;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
/**
 *
 * @author julia
 */
public class RegistroALumnos extends javax.swing.JFrame { 
     private Administracion administracion;

    // Constructor que acepta una instancia de Administracion
    public RegistroALumnos(Administracion admin) {
        this.administracion = admin;
        initComponents();
    }
    public RegistroALumnos() {
        initComponents();
                setUpDNIField();

    }
 private Connection getConnection() throws SQLException {
        //String url = "jdbc:mysql://localhost:3306/beltran"; // Cambia el nombre de la base de datos según tu configuración
        //String user = "root"; // Cambia el usuario según tu configuración
        //String password = ""; // Cambia la contraseña según tu configuración
        
                String URL = "jdbc:mysql://190.106.131.13:3306/beltran?useSSL=false";


            String USER = "beltran2024";
            String PASSWORD = "feelthesky1";
          
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
      private void setUpDNIField() {
        ((AbstractDocument) txtDNI.getDocument()).setDocumentFilter(new DNIFormatter());
    }
    
    private void cargarCarreras() {
        String sql = "SELECT carrera FROM carreras"; // Suponiendo que la columna se llama 'carrera'
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String carrera = rs.getString("carrera");
                cmbCarrera.addItem(carrera);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    public void generarQRCode(String data, String filePath) {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    try {
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        File file = new File(filePath);
        ImageIO.write(bufferedImage, "png", file);
    } catch (WriterException | IOException e) {
        JOptionPane.showMessageDialog(this, "Error al generar el código QR.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
     private void registrarAlumno() {
    if (validateFields()) {
        File fotosDir = new File("fotos");
        if (!fotosDir.exists()) {
            fotosDir.mkdirs();
        }

        String fotoRuta = saveFoto();
        if (fotoRuta == null) return; // Salir si hubo un error al guardar la foto

        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO alumnos (nombres, apellido, dni, legajo, ruta_foto, carrera, domicilio, altura, localidad, provincia, fecha_nacimiento, ano_ingreso, email, estado, turno) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtNombres.getText());
                ps.setString(2, txtApellido.getText());
                ps.setString(3, txtDNI.getText());
                ps.setString(4, txtLegajo.getText());
                ps.setString(5, fotoRuta);
                ps.setString(6, (String) cmbCarrera.getSelectedItem());
                ps.setString(7, txtDomicilio.getText() + " " + txtAltura.getText());
                ps.setString(8, txtAltura.getText());
                ps.setString(9, txtLocalidad.getText());
                ps.setString(10, txtProvincia.getText());
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                sdf.setLenient(false);
                Date fechaNacimiento = sdf.parse(txtFechaNacimiento.getText());
                java.sql.Date sqlDate = new java.sql.Date(fechaNacimiento.getTime());
                ps.setDate(11, sqlDate);
                
                ps.setInt(12, Integer.parseInt(txtAnoIngreso.getText()));
                ps.setString(13, txtCorreo.getText());
                ps.setString(14, (String) cmbEstado.getSelectedItem());
                ps.setString(15, (String) cmbTurno.getSelectedItem()); // Agregado el turno
                
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Alumno registrado exitosamente.");

                generarQRCodeConInformacion();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al registrar el alumno: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la conexión a la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos correctamente.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}



    private String saveFoto() {
        if (lblFoto.getIcon() == null) return null;

        File fotosDir = new File("fotos");
        String fotoRuta = null;
        try {
            String fotoNombre = txtDNI.getText() + ".jpg";
            File fotoArchivo = new File(fotosDir, fotoNombre);
            BufferedImage imagen = new BufferedImage(lblFoto.getIcon().getIconWidth(), lblFoto.getIcon().getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = imagen.createGraphics();
            lblFoto.getIcon().paintIcon(this, g2d, 0, 0);
            g2d.dispose();
            ImageIO.write(imagen, "jpg", fotoArchivo);
            fotoRuta = fotoArchivo.getAbsolutePath();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la foto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return fotoRuta;
    }
    
    
    private void generarQRCodeConInformacion() {
    String qrData = txtAnoIngreso.getText() + "Nombres: " + txtNombres.getText() + "\n" +
            "Apellido: " + txtApellido.getText() + "\n" +
            "DNI: " + txtDNI.getText() + "\n" +
            "Legajo: " + txtLegajo.getText() + "\n" +
            "Carrera: " + cmbCarrera.getSelectedItem() + "\n" +
            "Domicilio: " + txtDomicilio.getText() + "\n" +
            "Altura: " + txtAltura.getText() + "\n" +
            "Localidad: " + txtLocalidad.getText() + "\n" +
            "Provincia: " + txtProvincia.getText() + "\n" +
            "Fecha de Nacimiento: " + txtFechaNacimiento.getText() + "\n" +
            "Año de Ingreso: " + txtAnoIngreso.getText() + "\n" +
            "Correo Institucional: " + txtCorreo.getText() + "\n" +
            "Estado: " + cmbEstado.getSelectedItem() + "\n" +
            "Turno: " + cmbTurno.getSelectedItem(); // Agregado el turno

    String qrFilePath = "fotos/" + txtDNI.getText() + "_QR.png";
    generarQRCode(qrData, qrFilePath);
    JOptionPane.showMessageDialog(this, "Código QR generado exitosamente.");
}

   private boolean validateFields() {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        sdf.setLenient(false);
        sdf.parse(txtFechaNacimiento.getText());

        Integer.valueOf(txtAnoIngreso.getText());

        // Validar formato de DNI
        String dni = txtDNI.getText();
        if (!dni.matches("\\d{2}\\.\\d{3}\\.\\d{3}")) {
            return false;
        }

        // Validar formato de email (simplificado)
        String email = txtCorreo.getText();
        if (!email.contains("@") || !email.contains(".")) {
            return false;
        }

    } catch (ParseException | NumberFormatException e) {
        return false;
    }
    return !txtNombres.getText().isEmpty() &&
           !txtApellido.getText().isEmpty() &&
           !txtDNI.getText().isEmpty() &&
           !txtLegajo.getText().isEmpty() &&
           cmbCarrera.getSelectedItem() != null &&
           !txtDomicilio.getText().isEmpty() &&
           !txtAltura.getText().isEmpty() &&
           !txtLocalidad.getText().isEmpty() &&
           !txtProvincia.getText().isEmpty() &&
           !txtFechaNacimiento.getText().isEmpty() &&
           !txtAnoIngreso.getText().isEmpty() &&
           !txtCorreo.getText().isEmpty() &&
           cmbTurno.getSelectedItem() != null; // Verificar que el turno esté seleccionado
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        cmbCarrera = new javax.swing.JComboBox<>();
        lblFoto = new javax.swing.JLabel();
        Carrera = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtNombres = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Carrera6 = new javax.swing.JLabel();
        txtDNI = new javax.swing.JTextField();
        txtAnoIngreso = new javax.swing.JTextField();
        Carrera7 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        Legajo = new javax.swing.JLabel();
        txtFechaNacimiento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Carrera5 = new javax.swing.JLabel();
        txtLegajo = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel2 = new javax.swing.JPanel();
        Carrera1 = new javax.swing.JLabel();
        Carrera2 = new javax.swing.JLabel();
        txtDomicilio = new javax.swing.JTextField();
        txtAltura = new javax.swing.JTextField();
        Carrera8 = new javax.swing.JLabel();
        txtLocalidad = new javax.swing.JTextField();
        Carrera4 = new javax.swing.JLabel();
        txtProvincia = new javax.swing.JTextField();
        cmbEstado = new javax.swing.JComboBox<>();
        Carrera3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
        btnFoto = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        cmbTurno = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        abrirVentanaPrincipal = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cmbCarrera.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Analista de Sistemas", "Seguridad Higiene", "Comunicacion Multimedial", "Administracion de Empresas y Pymes" }));

        Carrera.setText("Carrera");

        jLabel1.setText("Nombres");

        jLabel3.setText("N° DNI");

        Carrera6.setText("Año Ingreso");

        Carrera7.setText("Email");

        Legajo.setText("N° Legajo");

        jLabel2.setText("Apellido");

        Carrera5.setText("Fecha Nac.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Carrera7)
                        .addGap(257, 257, 257))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(Carrera5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Legajo, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtLegajo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Carrera6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAnoIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Carrera6)
                            .addComponent(txtAnoIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Legajo)
                            .addComponent(txtLegajo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Carrera5)
                            .addComponent(txtFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Carrera7)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jToolBar1.setRollover(true);

        Carrera1.setText("Domicilio");

        Carrera2.setText("Altura");

        Carrera8.setText("Localidad");

        Carrera4.setText("Provincia");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo", "Pendiente" }));

        Carrera3.setText("Estado");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Carrera3)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(Carrera1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDomicilio, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Carrera2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAltura, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(Carrera8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Carrera4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Carrera1)
                    .addComponent(txtDomicilio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Carrera2)
                    .addComponent(txtAltura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Carrera8)
                    .addComponent(txtLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Carrera4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Carrera3)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnFoto.setText("Cargar Foto");
        btnFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFoto)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegistrar)
                    .addComponent(btnFoto)
                    .addComponent(btnCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cmbTurno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mañana", "Noche" }));

        jMenu1.setText("Opciones");

        abrirVentanaPrincipal.setText("Cancelar");
        jMenu1.add(abrirVentanaPrincipal);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(Carrera, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(cmbCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Carrera)
                            .addComponent(cmbTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoActionPerformed
// Crear un JFileChooser para seleccionar la foto
    JFileChooser fileChooser = new JFileChooser();
    
    // Establecer un filtro para solo mostrar imágenes JPG
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagenes JPG", "jpg");
    fileChooser.setFileFilter(filter);

    // Mostrar el diálogo de apertura de archivo
    int returnValue = fileChooser.showOpenDialog(this);

    // Si se selecciona un archivo, cargar la imagen
    if (returnValue == JFileChooser.APPROVE_OPTION) {
        File archivoFoto = fileChooser.getSelectedFile();

        try {
            // Leer la imagen desde el archivo
            BufferedImage imagen = ImageIO.read(archivoFoto);
            
            // Crear un ImageIcon a partir de la imagen leída
            ImageIcon fotoIcon = new ImageIcon(imagen);
            
            // Establecer el icono del JLabel con la imagen
            lblFoto.setIcon(fotoIcon);
            
            // Ajustar el tamaño del JLabel a la imagen
            lblFoto.setText("");  // Limpiar el texto en caso de que haya texto
            lblFoto.setIcon(new ImageIcon(imagen.getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH)));
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la foto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }    }//GEN-LAST:event_btnFotoActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
         registrarAlumno();

    
       }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
  // Mostrar la ventana de Administracion
        if (administracion != null) {
            administracion.setVisible(true);
        }
        // Cerrar la ventana actual (RegistroALumnos)
        this.dispose();
    
    }//GEN-LAST:event_btnCancelarActionPerformed
    /**
     * @param args the command line arguments
     */     public static void main(String args[]) {
        /* Set the FlatLaf look and feel */
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistroALumnos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegistroALumnos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Carrera;
    private javax.swing.JLabel Carrera1;
    private javax.swing.JLabel Carrera2;
    private javax.swing.JLabel Carrera3;
    private javax.swing.JLabel Carrera4;
    private javax.swing.JLabel Carrera5;
    private javax.swing.JLabel Carrera6;
    private javax.swing.JLabel Carrera7;
    private javax.swing.JLabel Carrera8;
    private javax.swing.JLabel Legajo;
    private javax.swing.JMenuItem abrirVentanaPrincipal;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFoto;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cmbCarrera;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbTurno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JTextField txtAltura;
    private javax.swing.JTextField txtAnoIngreso;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtDomicilio;
    private javax.swing.JTextField txtFechaNacimiento;
    private javax.swing.JTextField txtLegajo;
    private javax.swing.JTextField txtLocalidad;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtProvincia;
    // End of variables declaration//GEN-END:variables
}
