package net.yc.race.track.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import jakarta.annotation.PostConstruct;
@Data
@Component
@ConfigurationProperties(prefix = "rsa")
public class RsaKeyProperties {

    private String privateKey;
    private String publicKey;

    private RSAPrivateKey rsaPrivateKey;
    private RSAPublicKey rsaPublicKey;

    @PostConstruct
    public void init() throws Exception {
        this.rsaPrivateKey = loadPrivateKey(privateKey);
        this.rsaPublicKey = loadPublicKey(publicKey);
    }

    public RSAPrivateKey getPrivateKey() {
        return rsaPrivateKey;
    }

    public RSAPublicKey getPublicKey() {
        return rsaPublicKey;
    }

    private RSAPrivateKey loadPrivateKey(String key) throws Exception {
        String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    private RSAPublicKey loadPublicKey(String key) throws Exception {
        String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}
