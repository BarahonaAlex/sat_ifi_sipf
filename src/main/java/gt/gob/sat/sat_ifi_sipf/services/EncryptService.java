package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.arquitectura.microservices.exceptions.GeneralResponseException;
import gt.gob.sat.sat_ifi_sipf.Config;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ruarcuse
 */
@Service
@Slf4j
public class EncryptService {

    public static final String IMAGE = "IMAGE";
    public static final String APS = "APS";
    public static final String ACS = "ACS";

    private final PooledPBEStringEncryptor cipherImagesEncrypt;
    private final PooledPBEStringEncryptor cipherProcessInstancesEncrypt;
    private final PooledPBEStringEncryptor cipherAcs;

    @Autowired
    EncryptService(final Config config) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {

        this.cipherImagesEncrypt = new PooledPBEStringEncryptor();
        this.cipherImagesEncrypt.setPoolSize(4);          // This would be a good value for a 4-core system
        this.cipherImagesEncrypt.setPassword(config.getSecretKeyImages());
        this.cipherImagesEncrypt.setAlgorithm("PBEWithMD5AndTripleDES");

        this.cipherProcessInstancesEncrypt = new PooledPBEStringEncryptor();
        this.cipherProcessInstancesEncrypt.setPoolSize(4);          // This would be a good value for a 4-core system
        this.cipherProcessInstancesEncrypt.setPassword(config.getSecretKeyProcessInstances());
        this.cipherProcessInstancesEncrypt.setAlgorithm("PBEWithMD5AndTripleDES");

        this.cipherAcs = new PooledPBEStringEncryptor();
        this.cipherAcs.setPoolSize(4);
        this.cipherAcs.setPassword(config.getAcsSecretKey());
        this.cipherAcs.setAlgorithm("PBEWithMD5AndTripleDES");
    }

    public synchronized String encrypt(String encripter, String value) {
        String encryptedValue = "";
        switch (encripter.toUpperCase()) {
            case IMAGE:
                encryptedValue = this.cipherImagesEncrypt.encrypt(value);
                break;
            case APS:
                encryptedValue = this.cipherProcessInstancesEncrypt.encrypt(value);
                break;
            case ACS:
                encryptedValue = this.cipherAcs.encrypt(value);
                break;
        }
        return new String(Base64.getEncoder().encode(encryptedValue.getBytes(StandardCharsets.UTF_8)));
    }

    @SuppressWarnings("empty-statement")
    public synchronized String decrypt(String encripter, String value) {
        String decodedValue = "";
        decodedValue = new String(Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8)));
        try {
            switch (encripter.toUpperCase()) {
                case IMAGE:
                    return this.cipherImagesEncrypt.decrypt(decodedValue);
                case APS:
                    return this.cipherProcessInstancesEncrypt.decrypt(decodedValue);
                case ACS:
                    return this.cipherAcs.decrypt(decodedValue);
                default:
                    return decodedValue;
            }
        } catch (EncryptionOperationNotPossibleException e) {
            throw new GeneralResponseException("Error en desencriptacion del valor: " + value);
        }
    }
}
