import java.util.ArrayList;
import java.util.Scanner;


//L.A.S.E.R.: Luca Auditing Security Enterprise Repository
public class Laser {

  public static ArrayList<Block> blockchain = new ArrayList<>();

  // in response to the system admin confirming the ability to make a certain transactions
  public static boolean checkPermission(User user, int clearance) {
    return user.getClearance() > clearance;
  }

  //Verifies who the original requesting user using the signature, data, and public key without translating the data itself
  public static boolean verifiySignature(Transaction trx) {
    return Encryption.verifySignature(trx.getUserPublicKey(), trx.getTransactionData(), trx.getSignature());
  }

  public static boolean verifyRuntimeHash(User user) {
    Scanner scan = new Scanner(System.in);
    System.out.println("Confirm Identity: ");
    String threeWords = scan.next();
    if (Encryption.applySHA256(threeWords).equals(user.getRunTimeHash())) {
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
              + Long.toString(currentBlock.getTimestamp()) + currentBlock.getTransaction()))) {
        return false;
      }
      //compare previous hash and registered previous hash
      if (!(previousBlock.getCurrentHash().equals(currentBlock.getPreviousHash()))) {
        return false;
      }
    }
    return true;
  }

  public static boolean createGenesisBlock() {
    Block block = new Block(new Transaction(0, "BUY"), "Genesis Block", System.currentTimeMillis());
    blockchain.add(block);
    return true;
  }
}


