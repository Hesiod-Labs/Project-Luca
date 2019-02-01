import java.security.*;
import java.util.ArrayList;
import java.time.ZonedDateTime;
import java.time.*;

public class Enterprise { 
  
  public PublicKey publicKey;        //signed by a public key 
  public PrivateKey privateKey;      //signed by a private key 
  public String transactionID;       //Hashed transaction ID to add to Block 
  public byte[] signature;           //Address signature to a transaction ID 
  private long timestamp;            //ZonedDateTime converted to Unix time
  private boolean isAdmin = false;   //Does the user have admin access
  private boolean isSector = false;  //Does the user have sectorhead access
  public String enterpriseData;      //the data to then be hashed and secured 
  
  //Creating a new Enterprise requires an Address, type of transaction, and time it occurred 
  public Enterprise(Address user, Transaction transactionType, ZonedDateTime time) {
    
    this.privateKey = user.privateKey;
    this.publicKey = user.publicKey;
    this.timestamp = time.toInstant().toEpochMilli();
    this.transactionID = Encryption.applySHA256(Encryption.getStringFromKey(privateKey) + Encryption.getStringFromKey(publicKey) + Long.toString(timestamp) + String.valueOf(transactionType));
    this.enterpriseData = Encryption.getStringFromKey(privateKey) + Encryption.getStringFromKey(publicKey) + transactionID;
    this.signature = Encryption.applyECDSASig(privateKey,enterpriseData); //creates the signature using an eliptic curve digital signature algorithm
    
    //if enterprise type requries admin access, then make isAdmin true
    //if enterprise type requires sector access, then make isSector true
    //certain transactionTypes are generally requests or actions 
  }
}