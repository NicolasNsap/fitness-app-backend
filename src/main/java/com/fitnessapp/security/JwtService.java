package com.fitnessapp.security;
//DEPENDENCIAS JWT
import io.jsonwebtoken.Claims;//los datos del token (payload)
import io.jsonwebtoken.ExpiredJwtException;//Excepción: token expirado
import io.jsonwebtoken.Jwts;//clase principal para crear/leer JWT
import io.jsonwebtoken.MalformedJwtException;//Excepción: token mal formado
import io.jsonwebtoken.UnsupportedJwtException;//Excepción: token no soportado
import io.jsonwebtoken.io.Decoders;//para codificar Base64
import io.jsonwebtoken.security.Keys;//para crear la clave de firma
import io.jsonwebtoken.security.SecurityException;//Excepcion: firma invalida

import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;//para logging
import org.springframework.beans.factory.annotation.Value;//para leer application.yml
import org.springframework.stereotype.Service;//marca esta clase como Service

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT service es la fabrica de pasaportes digitales
 * se encarga de:
 * - generar tokens
 * - validar tokens
 * - entraer informacion de tokens
 *
 * @author Nicolas Abarca
 */

@Service
@Slf4j
public class JwtService {
    //variables de configuracion(se leen de application.yml)

    /**
     * La clave secreta para firmar los tokens
     * @Value le dice a sring lee los valores de application.yml
     */
    @Value("${jwt.secret-key}")//Se lee desde application.yml: jwt.secret-key
    private String secretKey;

    /**
     * Tiempo de expiracion de token en milisegundos
     * fecha de vencimiento del token
     */
    @Value("${jwt.expiration}")//Se lee desde application.yml: jwt.secret-key
    private long jwtExpiration;

    /**
     * METODOS DE EXTRACCION
     */

    /**
     * extrae el username (subject) del token
     *
     * @param token el JWT del cual exrear el username
     * @return el username contenido en el token
     */
    public String extractUsername(String token){
        Claims claims = extractAllClaims(token);//obtener todos los datos
        return claims.getSubject();//retornar el "subject"(username
    }

    /**
     * Extrae el rol del token
     *
     * @param token El JWT
     * @return El rol del usuario (USER, ADMIN)
     */
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    /**
     * Extrae la fecha de expiración del token
     *
     * @param token El JWT
     * @return La fecha de expiración
     */
    public Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    /**
     * Extrae todos los claims del token
     *
     * Analogía: Leer TODA la información del pasaporte
     *
     * Este metodo también VÁLIDA la firma del token
     * Si la firma no es válida, lanza una excepción
     *
     * @param token El JWT a parsear
     * @return Todos los claims contenidos en el token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Usa nuestra clave para verificar
                .build()
                .parseSignedClaims(token)// Parsea y valida la firma
                .getPayload();// Retorna el payload (claims)
    }

    /**
     * METODOS DE GENERACION
     */

    /**
     * Genera un token JWT para un usuario
     *
     * Analogía: Imprimir un pasaporte nuevo para alguien
     * - Ponemos su nombre (subject)
     * - Ponemos sus permisos (claims extra como rol)
     * - Ponemos fecha de creación y expiración
     * - Firmamos con nuestro sello oficial
     *
     * @param username El username del usuario
     * @param role El rol del usuario (USER, ADMIN)
     * @return El token JWT generado
     */
    public String generateToken(String username, String role) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role);  // Agregamos el rol como claim extra

        return generateToken(extraClaims, username);
    }

    /**
     * Genera un token con claims personalizados
     *
     * @param extraClaims Claims adicionales a incluir (ej: rol, email)
     * @param username El subject del token
     * @return El token JWT generado
     */
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return buildToken(extraClaims, username, jwtExpiration);
    }

    /**
     * Construye el token JWT
     *
     * Este es el método que realmente CREA el token
     *
     * @param extraClaims Claims adicionales
     * @param subject El subject (normalmente username)
     * @param expiration Tiempo de expiración en milisegundos
     * @return El token JWT firmado
     */
    private String buildToken(Map<String, Object> extraClaims, String subject, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)                                          // Claims extras (rol, etc.)
                .subject(subject)                                             // Subject (username)
                .issuedAt(new Date(System.currentTimeMillis()))               // Fecha de creación
                .expiration(new Date(System.currentTimeMillis() + expiration)) // Fecha de expiración
                .signWith(getSigningKey())                                    // Firma con nuestra clave
                .compact();                                                   // Construye el string JWT
    }


    /**
     * MÉTODOS DE VALIDACIÓN - Verificar si el pasaporte es auténtico
     */

    /**
     * Válida si un token es válido para un username dado
     *
     * Analogía: Verificar un pasaporte
     * 1. ¿El nombre coincide con la persona que lo presenta?
     * 2. ¿El pasaporte no ha expirado?
     * 3. ¿El sello oficial es auténtico? (esto se verifica en extractAllClaims)
     *
     * @param token El JWT a validar
     * @param username El username esperado
     * @return true si el token es válido, false si no
     */
    public boolean isTokenValid(String token, String username) {
        try {
            final String tokenUsername = extractUsername(token);
            return (tokenUsername.equals(username)) && !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Valida un token sin necesidad de username
     * Útil para validación inicial en el filtro
     *
     * @param token El JWT a validar
     * @return true si el token tiene formato válido y no ha expirado
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);  // Si esto no lanza excepción, el token es válido
            return !isTokenExpired(token);
        } catch (SignatureException e) {
            log.error("❌ Firma JWT inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("❌ Token JWT malformado: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("❌ Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("❌ Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("❌ JWT claims vacío: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Verifica si el token ha expirado
     *
     * Analogía: Verificar la fecha de vencimiento del pasaporte
     *
     * @param token El JWT a verificar
     * @return true si ha expirado, false si aún es válido
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * METODOS AUXILIARES
     */

    /**
     * Obtiene la clave de firma
     *
     * Convierte nuestra clave secreta (String) en una SecretKey
     * que puede usar el algoritmo HMAC-SHA256
     *
     * Analogía: Preparar el sello oficial para estampar
     *
     * @return La SecretKey para firmar/verificar tokens
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
