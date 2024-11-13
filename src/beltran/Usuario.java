package beltran;

public class Usuario {
    private int id;
    private String usuario;
    private String nombreCompleto;
    private String permisos;
    private String email;
    private boolean validado;
    private String tokenValidacion;  // Agregar este campo para el token de validación

    // Constructor con todos los campos, incluyendo tokenValidacion
    public Usuario(int id, String usuario, String nombreCompleto, String permisos, String email) {
        this.id = id;
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.permisos = permisos;
        this.email = email;
        this.tokenValidacion = tokenValidacion;
    }

    // Getters y setters para los campos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPermisos() {
        return permisos;
    }

    public void setPermisos(String permisos) {
        this.permisos = permisos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
    }

    // Getter para el tokenValidacion
    public String getTokenValidacion() {
        return tokenValidacion;
    }

    // Setter para el tokenValidacion
    public void setTokenValidacion(String tokenValidacion) {
        this.tokenValidacion = tokenValidacion;
    }
}
