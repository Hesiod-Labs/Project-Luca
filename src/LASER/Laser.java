package LASER;
import BTA.*;
import LucaMember.User;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;


//L.A.S.E.R.: Luca Auditing Security Enterprise Repository
public class Laser {

  public static File transactionHistory = new File("Transaction History");

  public static ArrayList<Block> blockchain = new ArrayList<>();

  public static String laserKey = "L.A.S.E.R.: Luca Auditing Security Enterprise Repository";

  public static File initializeFile(File transactionHistory) throws IOException {
    FileWriter fw = new FileWriter(transactionHistory);
    fw.write("Request User\tResolve User\tTransaction\n");
    return transactionHistory;
  }

  public static boolean verifyRuntimeHash(User user) {
    Scanner scan = new Scanner(System.in);
    System.out.println("Confirm Identity (case sensitive, add a space between words): ");
    String threeWords = scan.next();
    return Encryption.applySHA256(threeWords).equals(user.getRunTimeHash());
  }

  //checks the validity of the blockchain after every block add
  public static boolean isChainValid() {
    Block currentBlock;
    Block previousBlock;
    //loop through blockchain to check hashes:
    for (int index = 1; index < getBlockchain().size(); index++) {
      currentBlock = getBlockchain().get(index);
      previousBlock = getBlockchain().get(index - 1);
      //compare registered hash and calculated hash:
      if (!(currentBlock.getCurrentHash()).equals(
              Encryption.applySHA256(currentBlock.getPreviousHash() +
              currentBlock.getTimestamp() + currentBlock.getTransaction()))) {
        return false;
      }
      //compare previous hash and registered previous hash
      if (!(previousBlock.getCurrentHash().equals(currentBlock.getPreviousHash()))) {
        return false;
      }
    }
    return true;
  }

  public static boolean addBlock(Transaction trx, String status) {
    Block block = new Block(trx, getBlockchain().get(getBlockchain().size() - 1).getCurrentHash(),
            System.currentTimeMillis(), status);
    getBlockchain().add(block);
    
    return Laser.isChainValid();
  }

  //approve or deny the transaction based on a permissioned status
  public static boolean validateTransaction(Transaction trx) {
    if (trx.getResolveUser() == null && trx.getRequestUser().getClearance() < 2) {
      if (Encryption.verifySignature(trx.getUserPublicKey(), trx.getTransactionData(), trx.getSignature())) {
        addBlock(trx, "Request: ");
        return true;
      }
    }
    if (trx.getResolveUser().getClearance() >= 2) {
      if (Encryption.verifySignature(trx.getUserPublicKey(), trx.getTransactionData(), trx.getSignature())) {
        Laser.addBlock(trx, "Resolved: ");
        return true;
      }
    }
    return false;
  }

  public static boolean createGenesisBlock() {
    Block block = new Block(new Transaction("BUY", 0), "Genesis Block", System.currentTimeMillis(), "");
    getBlockchain().add(block);
    return true;
  }
  
  public static ArrayList<LASER.Block> getBlockchain() {
    return blockchain;
  }
}


