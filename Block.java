import java.util.Date;
import java.util.ArrayList;

public class Block {
  
  public String hash;
  public String previousHash;
  private Enterprise transaction;
  private long timestamp; //as number of milliseconds since 1/1/1970.
  
  //Block Constructor, will take a specific kind of Transaction
  //this will either be a single Transaction per small, quick block, or array of Transactions depending on block size
  public Block(Enterprise instance, String previousHash, long mineTime) {
    this.transaction = instance;
    this.previousHash = previousHash;
    this.timestamp = mineTime;
    this.hash = blockHash();
  }
  
  
  //pretty arbitrary 
  public String blockHash() {
    
    return Encryption.applySHA256(previousHash + Long.toString(timestamp) + transaction.toString()); 
  }
  
  //approve or deny the transaction based on a permissioned status
  public boolean validateTransaction(Enterprise instance) {
    //if certain characterisitcs are possessed or criteria is retained
    //then approve
    //else
    //deny access and register attempt as a transaction on the blockchain 
    return true;
  }
  
  
  //Add transactions to this block if its valid
  public boolean addTransaction(Enterprise instance) {
    
    if (instance == null) { //preventative check for a null transaction
      return false;  
    }
    
    if ((previousHash != "0")) { //if the previous hash is not the genesis block
    }
    
    //add if its the second block or if the transaction was properly processed
    return true;
  }
}