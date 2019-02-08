import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;


//The Encryption Methods used throughout the LASER program
public class Encryption {

    //Blockchain Encryption & Verification Methods

    // utilizing secure hashing algorithm 256 bit for encryption
    public static String applySHA256(String trx) { //should be noted that this is a one way encryption
        try {
            MessageDigest encrypted = MessageDigest.getInstance("SHA-256");
            //literal hash code in UTF-8 basic text format for String - Hash transition
            byte[] hash = encrypted.digest(trx.getBytes("UTF-8"));
            StringBuffer hexHash = new StringBuffer();
            //encrypts and records the hash
            for (byte hash1 : hash) {
                //each character of the string corresponds to a part of the hash
                String hexChar = Integer.toHexString(0xff & (int) hash1);
                //base-case
                if (hexChar.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hexChar);
            }
            return hexHash.toString();
        }
        //for exceptions such as algorithm does not exist
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Signs the transaction with the private key to indentify the address
    public static byte[] applySignature(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] sig = new byte[0];
        try {
            //specific signature utilizing the eliptic curve
            dsa = Signature.getInstance("DSA", "SUN");
            //signs the transaction using the private key
            dsa.initSign(privateKey);
            //hash the input for the first time
            byte[] strByte = input.getBytes();
            //apply the signature of the address
            dsa.update(strByte);
            //resign with both encrypted information and an ecrypted signature
            byte[] realSig = dsa.sign();
            //so it can leave the Try/Catch
            sig = realSig;

        } catch (Exception e) {
            //for exceptions such as algorithm not found
            throw new RuntimeException(e);
        }
        return sig;
    }

    //Verifies a transaction came from a specific public key
    public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature) {
        try {
            //specific signature utilizing the eliptic curve
            Signature sig = Signature.getInstance("DSA", "SUN");
            //assumes the signature was made using the input public key
            sig.initVerify(publicKey);
            //updates the data to reflect a specifc public key
            sig.update(data.getBytes());
            //verifies if the signature actually did come from this private key
            return sig.verify(signature);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // Information Storage Encryption/Decryption

    private static SecretKeySpec sks;
    private static byte[] key;

    //input a string and that will be the information to be entered
    public static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            sks = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
    }

    //encrypt the information using the private key
    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //decrypts the hashes
    public static String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    //The keys are used to sign transactions made by an address
  public static KeyPair generateKeyPair() {
    //prevent runtime algorithm mishaps 
    try {
        //access keys created using the eliptic curve digital secure algorithm 
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        //cryptographically strong random number generator
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");   
        //generates a pair of keys from the key generator
        keyGen.initialize(1024, random);
        KeyPair keyPair = keyGen.generateKeyPair();
        //Set the public and private keys from the keyPair
        // String privateKey = keyPair.getPrivate().toString();
        // String publicKey = keyPair.getPublic().toString();
        return keyPair;
    }
    catch(Exception e) {
     //runtime exception such as algorithm not found
     throw new RuntimeException(e);
    }
  }
  
}