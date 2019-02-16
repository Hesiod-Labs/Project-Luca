import java.util.Date;
import java.util.ArrayList;

public class Block {
  
  private String currentHash;
  private String previousHash;
  private Transaction trx;
  private long timestamp;
  
  //Block Constructor, will take a specific kind of Transaction
  public Block(Transaction trx, String previousHash, long timestamp) {
    this.previousHash = previousHash;
    this.trx = trx;
    this.timestamp = timestamp;
    this.currentHash = Encryption.applySHA256(previousHash + " " + String.valueOf(timestamp) + " " + trx.toString());
  }
  
  //approve or deny the transaction based on a permissioned status
  public boolean validateTransaction(Transaction trx) {
    //if certain characterisitcs are possessed or criteria is retained
    //then begin the creation of the block for the blockchain
    //else return an error statement
    return true;
  }

  public String getCurrentHash() {
    return this.currentHash;
  }

  public String getPreviousHash() {
    return this.previousHash;
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public String getTransaction() {
    return this.trx.toString();
  }
}