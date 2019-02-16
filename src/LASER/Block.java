import java.util.Date;
import java.util.ArrayList;
package LASER;
import BTA.Transaction;

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
    this.currentHash = Encryption.applySHA256(previousHash + " " + timestamp + " " + trx.toString());
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