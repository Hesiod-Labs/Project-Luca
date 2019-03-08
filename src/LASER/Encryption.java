package LASER;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.*;


//One and Two way encryption and signature functions for the LASER protocol
public class Encryption {
    // utilizing secure hashing algorithm 256 bit for ONE WAY encryption
    // algorithm instance is "SHA-256" and charsetName is "UTF-8"
    //TODO Handle exception errors
    public static String applySHA256(String trx)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
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

    // an Address signs a transaction with its private key
    // TODO Handle exception errors
    public static byte[] applySignature(PrivateKey privateKey, String input)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature dsa = Signature.getInstance("DSA", "SUN");
        dsa.initSign(privateKey);
        byte[] strByte = input.getBytes();
        dsa.update(strByte);
        return dsa.sign();
    }

    //verifies that a specific public key is tied to the private key that signed a transaction
    public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("DSA", "SUN");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        return sig.verify(signature);
    }

    // sets a secret key for encryption or decryption
    public static SecretKeySpec setKey(String myKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key = myKey.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        return sks;
    }

    //encrypt the information using the private key
    public static String encryptAES(String strToEncrypt, String runTimeHash)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec sks = Encryption.setKey(runTimeHash);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    //decrypts a string or hash using a given key
    public static String decryptAES(String strToDecrypt, String runTimeHash)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec sks = setKey(runTimeHash);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    // generates a key pair for a given address
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        return keyGen.generateKeyPair();
    }
}