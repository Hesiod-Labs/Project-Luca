package LASER;
import BTA.Transaction;

public class Block {
  
  private String currentHash;
  private String previousHash;
  private Transaction trx;
  private long timestamp;
  
  //Block Constructor, will take a specific kind of Transaction
  public Block(Transaction trx, String previousHash, long timestamp, String status) {
    this.previousHash = previousHash;
    this.trx = trx;
    this.timestamp = timestamp;
    this.currentHash = Encryption.applySHA256(status + trx.toString() + previousHash + timestamp);
  }

  public String getCurrentHash() { return this.currentHash; }

  public String getPreviousHash() {
    return this.previousHash;
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public Transaction getTransaction() {
    return this.trx;
  }
}