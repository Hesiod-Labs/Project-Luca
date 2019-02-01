import java.security.*;


//The Encryption Methods used throughout the LASER program 
public class Encryption { //complete & tested
  // utilizing secure hashing algorithm 256 bit for encryption
  public static String applySHA256(String trx) { //should be noted that this is a one way encryption 
    try {
      MessageDigest encrypted = MessageDigest.getInstance("SHA-256");
      //literal hash code in UTF-8 basic text format for String - Hash transition
      byte[] hash = encrypted.digest(trx.getBytes("UTF-8"));
      StringBuffer hexHash = new StringBuffer(); 
      //encrypts and records the hash
      for (int i = 0; i < hash.length; i++) {
        //each character of the string corresponds to a part of the hash 
        String hexChar = Integer.toHexString(0xff & (int)hash[i]);
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
 
 //need to make a new get the text from the encrypted hash 
 public static String decrypt(String hash) {
   return "hi";
 }
 
 //if we want to be able to mine the data from the hashes then we cant use SHA256 
 //working on alternative such as AES or a different way, need one that uses hexadecimal hashes for the encyrption/decryption
 //if you need to use any of these methods, can assume you will get a string returned with the hash and byte[] for signature, boolean for verification
 
}