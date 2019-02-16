package LASER;
import BTA.Transaction;
import LucaMember.User;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

//One and Two way encryption and signature functions for the LASER protocol
public class Encryption {
    // utilizing secure hashing algorithm 256 bit for ONE WAY encryption
    public static String applySHA256(String trx) {
        try { // try catch should not be used as version control
            MessageDigest encrypted = MessageDigest.getInstance("SHA-256");
            byte[] hash = encrypted.digest(trx.getBytes("UTF-8"));
            StringBuilder hexHash = new StringBuilder();
            for (byte hash1 : hash) {
                String hexChar = Integer.toHexString(0xff & (int) hash1);
                if (hexChar.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hexChar);
            }
            return hexHash.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // an Address signs a transaction with its private key
    public static byte[] applySignature(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] sig;
        try { // try catch should not be used as version control
            dsa = Signature.getInstance("DSA", "SUN");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            sig = realSig;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sig;
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
            throw new RuntimeException(e);
        }
    }


    // fields used in both the encryption and decryption functionality
    private static SecretKeySpec sks;
    private static byte[] key;

    // sets a secret key for encryption or decryption
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
    public static String encryptAES(String strToEncrypt, String runTimeHash) {
        try {
            setKey(runTimeHash);
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
            setKey(runTimeHash);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
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
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean logEncryptedTransaction(Transaction trx) throws IOException {
        FileWriter fw = new FileWriter(Laser.transactionHistory);
        StringBuilder sb = new StringBuilder();
        String resolveUser;
        if (trx.getResolveUser() != null) {
            resolveUser = Encryption.encryptAES(trx.getResolveUser().toString(), Laser.laserKey);
        }
        else resolveUser = "null";
        sb.append(Encryption.encryptAES(trx.getRequestUser().toString(), Laser.laserKey)
        + "\t" + resolveUser + "\t" + Encryption.encryptAES(trx.getTransactionData(), Laser.laserKey));
        fw.write(sb.toString());
        return true;
    }

    public static File decryptTransactionLog(File trxLog, User user) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(trxLog));
        File outputfile = new File("Your Transactions");
        FileWriter fw = new FileWriter(outputfile);
        String line;
        String [] split;
        while ((line = br.readLine()) != null) {
            split = line.split("\t");
            if (user.getClearance() >= 2) {
                String requestUser = Encryption.decryptAES(split[0], Laser.laserKey);
                String resolveUser = Encryption.decryptAES(split[1], Laser.laserKey);
                String data = Encryption.decryptAES(split[2], Laser.laserKey);
                fw.write(requestUser + "\t" + resolveUser + "\t" + data);
            }
            else {
                String requestUser = Encryption.decryptAES(split[0], Laser.laserKey);
                fw.write(requestUser + "\t" + split[1] + "\t" + split[2]);
            }
        }
        return outputfile;
    }
}