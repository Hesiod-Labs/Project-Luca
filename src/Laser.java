import java.util.ArrayList;
import java.util.Scanner;
import LucaMember.User;
import BTA.Transaction;

//L.A.S.E.R.: Luca Auditing Security Enterprise Repository
public class Laser {

  private static ArrayList<Block> blockchain = new ArrayList<>();

  // in response to the system admin confirming the ability to make a certain transactions
  public static boolean checkPermission(User user, int clearance) {
    return user.getClearance() > clearance;
  }

  //Verifies who the original requesting user using the signature, data, and public key without translating the data itself
  public static boolean confirmSignature(Transaction trx) {
    return Encryption.verifySignature(trx.getUserPublicKey(), trx.getTransactionData(), trx.getSignature());
  }

  public static boolean verifyRuntimeHash(User user) {
    Scanner scan = new Scanner(System.in);
    System.out.println("Confirm Identity: ");
    String threeWords = scan.next();
    if (Encryption.applySHA256(threeWords).equals(user.getRunTimeHash())) {
      System.out.println("Access Granted");
      return true;
    }
    else return false;
  }

  //checks the validity of the blockchian after every block add
  public static boolean isChainValid() {
    Block currentBlock;
    Block previousBlock;
    //loop through blockchain to check hashes:
    for (int index = 1; index < blockchain.size(); index++) {
      currentBlock = blockchain.get(index);
      previousBlock = blockchain.get(index - 1);
      //compare registered hash and calculated hash:
      if (!(currentBlock.getCurrentHash()).equals(Encryption.applySHA256(currentBlock.getPreviousHash()
              + currentBlock.getTimestamp() + currentBlock.getTransaction()))) {
        return false;
      }
      if (!(previousBlock.getCurrentHash().equals(currentBlock.getPreviousHash()))) {
        return false;
      }
    }
    return true;
  }

  //approve or deny the transaction based on a permissioned status
  public static boolean validateTransaction(Transaction trx) {

    //if certain characterisitcs are possessed or criteria is retained
    //then begin the creation of the block for the blockchain
    //else return an error statement
    return true;
  }

  public static boolean addBlock() {

  }

  public static boolean createGenesisBlock() {
    Block block = new Block(new BTA.Transaction(0, "BUY"), Encryption.applySHA256("Genesis Block"),
                            System.currentTimeMillis());
    blockchain.add(block);
    return true;
  }
}


