package it.sander.aml.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

@Component
public class JwtProvider {

    private static final Logger log = LogManager.getLogger(JwtProvider.class);

    public static String issuer;
    public static String secret;
    public static String prefix;
    public static String headerParam;
    public static int expireMinutes;

    @Autowired
    public JwtProvider(Environment env) {
    	JwtProvider.issuer = env.getProperty("security.issuer");
        JwtProvider.secret = env.getProperty("security.secret");
        JwtProvider.prefix = env.getProperty("security.prefix");
        JwtProvider.headerParam = env.getProperty("security.param");
        JwtProvider.expireMinutes = env.getProperty("security.expireMinutes", Integer.class);
        if (JwtProvider.issuer == null || JwtProvider.secret == null || JwtProvider.prefix == null || JwtProvider.headerParam == null) {
            throw new BeanInitializationException("Cannot assign security properties. Check application.yml file.");
        }
    }

    public static String createJwt(String subject, Map<String, String> payloadClaims) {

        final Date now = new Date();
        final Calendar expiration = new GregorianCalendar();
        expiration.setTime(now);
        expiration.add(Calendar.MINUTE, expireMinutes);
    	
    	JWTCreator.Builder builder = 
        		JWT.create()
        		.withSubject(subject)
        		.withIssuer(issuer)
        		.withIssuedAt(now)
        		.withExpiresAt(expiration.getTime());

        if (payloadClaims.isEmpty()) {
            log.warn("You are building a JWT without header claims");
        }
        for (Map.Entry<String, String> entry : payloadClaims.entrySet()) {
            builder.withClaim(entry.getKey(), entry.getValue());
        }
        return builder.sign(Algorithm.HMAC256(JwtProvider.secret));
    }

    public static DecodedJWT verifyJwt(String jwt) {
        return JWT.require(Algorithm.HMAC256(JwtProvider.secret)).build().verify(jwt);
    }
}
