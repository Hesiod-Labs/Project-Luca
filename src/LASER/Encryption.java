package LASER;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.*;


//One and Two way encryption and signature functions for the LASER protocol
public class Encryption {
    // utilizing secure hashing algorithm 256 bit for ONE WAY encryption
    // algorithm instance is "SHA-256" and charsetName is "UTF-8"
    public static String applySHA256(String trx) {
        try {
            MessageDigest encrypted = MessageDigest.getInstance("SHA-256");
            byte[] hash = encrypted.digest(trx.getBytes("UTF-8"));
            StringBuilder hexHash = new StringBuilder();
            for (byte hashi : hash) {
                String hexChar = Integer.toHexString(0xff & (int) hashi);
                if (hexChar.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hexChar);
            }
            return hexHash.toString();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException  e) {
            e.printStackTrace();
            e.getMessage();
        }
        return null;
    }

    // an Address signs a transaction with its private key
    public static byte[] applySignature(PrivateKey privateKey, String input) {
        try {
            Signature dsa = Signature.getInstance("DSA", "SUN");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            return dsa.sign();
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    //verifies that a specific public key is tied to the private key that signed a transaction
    public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature sig = Signature.getInstance("DSA", "SUN");
            sig.initVerify(publicKey);
            sig.update(data.getBytes());
            return sig.verify(signature);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // sets a secret key for encryption or decryption
    private static SecretKeySpec setKey(String myKey) {
        try {
            byte[] key = myKey.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec sks = new SecretKeySpec(key, "AES");
            return sks;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //encrypt the information using the private key
    public static String encryptAES(String strToEncrypt, String runTimeHash) {
        try {
            SecretKeySpec sks = Encryption.setKey(runTimeHash);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //decrypts a string or hash using a given key
    public static String decryptAES(String strToDecrypt, String runTimeHash) {
        try {
            SecretKeySpec sks = setKey(runTimeHash);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // generates a key pair for a given address
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            return keyGen.generateKeyPair();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}