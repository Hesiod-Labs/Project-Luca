import java.util.Date;
import java.util.ArrayList;

public class Block {
  
  public String hash;
  public String previousHash;
  private Enterprise transaction;
  private long timestamp; //as number of milliseconds since 1/1/1970.
  
  //Block Constructor, will take a specific kind of Transaction
  public Block(Transaction trx, String previousHash, long mineTime) {
    this.transaction = instance;
    this.previousHash = previousHash;
    this.timestamp = mineTime;
    this.currentHash = Encryption.applySHA256(previousHash + Long.toString(timestamp) + transaction.toString());
  }
  
  //approve or deny the transaction based on a permissioned status
  public boolean validateTransaction(Enterprise instance) {
    //if certain characterisitcs are possessed or criteria is retained
    //then begin the creation of the block for the blockchain
    //else return an error statement
    return true;
  }
  
  //Add transactions to this block if its valid
  public boolean addTransaction(Transaction trx) {
    //add if its the second block or if the transaction was properly processed
    return true;
  }
}