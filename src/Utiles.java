import java.util.UUID;
import java.security.SecureRandom;
import java.math.BigInteger;

public class Utiles {

    /**
     * Método para generar un token único de validación.
     * @return Un token aleatorio único en formato String.
     */
    public static String generarTokenValidacion() {
        // Usamos UUID para generar un identificador único
        UUID uuid = UUID.randomUUID();
        return uuid.toString(); // Retorna el UUID como un string
    }
    
    /**
     * Método alternativo que genera un token más complejo y aleatorio.
     * @return Un token único aleatorio.
     */
    public static String generarTokenSeguro() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32); // Genera un token largo y aleatorio
    }
}
