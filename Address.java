//import java.security.*;
import java.security.*;
import java.security.spec.ECGenParameterSpec;


//address for HD Wallets to be assigned to each user
public class Address {
  //track user login period activity 
  String username; 
  String password; 
  //individual access and keys
  public PrivateKey privateKey;
  public PublicKey publicKey;
  public String runtimeHash;
  //sectorhead access and keys
  public boolean sectorPermissioned = false;
  public PrivateKey sectorPrivateKey;
  public PublicKey sectorPublicKey;
  //admin access and keys 
  public boolean adminPermissioned = false; 
  public PrivateKey adminPrivateKey;
  public PublicKey adminPublicKey;
  
  //An instance of an address is created using the username, password, and key words
  public Address(String un, String pw, String word1, String word2, String word3) {
    //field intiializing 
    this.username = un;
    this.password = pw;
    //two factor authentication using the three word principle
    this.runtimeHash = Encryption.applySHA256(word1 + word2 + word3);
    //key generation method for access and permissions 
    generateKeyPair(); 
  }
  
  //The keys are used to sign transactions made by an address
  public void generateKeyPair() {
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
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }
    catch(Exception e) {
     //runtime exception such as algorithm not found
     throw new RuntimeException(e);
    }
  }
  
  
  public void getInformation() {
    String[] info = {this.username, this.password, this.runtimeHash};
    for(int i = 0; i < info.length; i++) {
      System.out.println(info[i]);
    }
  }
  
  public void getKeys() {
    System.out.println(this.privateKey);
    System.out.println(this.publicKey);
  }
  
}