package LASER;
import BTA.*;
import LucaMember.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.File;


//L.A.S.E.R.: Luca Auditing Security Enterprise Repository
public class Laser {

  public static File transactionHistory = new File("TransactionHistory.txt");

  public static ArrayList<Block> blockchain = new ArrayList<>();

  public static String laserKey = "L.A.S.E.R.: Luca Auditing Security Enterprise Repository";


  public static boolean verifyRuntimeHash(User user) {
    Scanner scan = new Scanner(System.in);
    System.out.println("Confirm Identity (case sensitive, add a space between words): ");
    String w1 = scan.next();
    String w2 = scan.next();
    String w3 = scan.next();
    String threeWords = w1 + w2 + w3;
    
    return Encryption.applySHA256(threeWords).equals(user.getRunTimeHash());
  }

  public static void initializeFile(File file) throws IOException {
    PrintWriter pw = new PrintWriter(file);
    pw.close();
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

  public static boolean addBlock(Transaction trx, String status) throws IOException {
    Block block = new Block(trx, getBlockchain().get(getBlockchain().size() - 1).getCurrentHash(),
            System.currentTimeMillis(), status);
    blockchain.add(block);
    Encryption.logEncryptedTransaction(trx);
    return Laser.isChainValid();
    }

  //approve or deny the transaction based on a permissioned status
  public static boolean validateTransaction(Transaction trx) throws IOException {
    if (Laser.verifyRuntimeHash(trx.getRequestUser())) {
      if (trx.getRequestUser().getClearance() < 2) {
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
    }
    return false;
  }

  public static boolean createGenesisBlock() {
    Block block = new Block(new Transaction(Transaction.Type.BUY, 0), "Genesis Block", System.currentTimeMillis(), "");
    getBlockchain().add(block);
    return true;
  }
  
  public static ArrayList<LASER.Block> getBlockchain() {
    return blockchain;
  }

  public static ArrayList<String> getBlockchainToString() {
    ArrayList<String> output = new ArrayList<>(getBlockchain().size());
    for (Block block : getBlockchain()) {
      output.add(block.toString());
    }
    return output;
  }
}