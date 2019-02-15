import java.security.*;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Date;

//L.A.S.E.R.: Luca Auditing Security Enterprise Repository
public class Laser {

  public boolean checkPermission(Transaction request, Address user) {
    // if the correct boolean is met then allow the access to specifc keys
    return false;
  }

  //Verifies who the original requesting user using the signature, data, and public key without translating the data itself
  public static boolean verifiySignature(Transaction trx) {
    return Encryption.verifyECDSASig(trx.publicKey, trx.transactionData, trx.getSignature());
  }
  
  public boolean verifyRuntimeHash() {
    // how to request three words input from the user
    //verify that the three words are in deed hashed to be the runtime hash
    // SHA 256 hash the username, pw, and three prompted words
    return true
  }

  public static ArrayList<Block> blockchain = new ArrayList<Block>();

  //checks the validity of the blockchian after every block add
  public static boolean isChainValid() {
    Block currentBlock;
    Block previousBlock;
    //loop through blockchain to check hashes:
    for (int index = 1; index < blockchain.size(); index++) {
      currentBlock = blockchain.get(index);
      previousBlock = blockchain.get(index - 1);
      //compare registered hash and calculated hash:
      if (!currentBlock.currentHash.equals(Encryption.applySHA256(currentBlock.previousHash
              + Long.toString(currentBlock.timestamp) + currentBlock.transaction.toString())) {
        return false;
      }
      //compare previous hash and registered previous hash
      if (!previousBlock.currentHash.equals(currentBlock.previousHash)) {
        return false;
      }
    }
    return true;
  }

    //adds a new block to the blockchain
    public static void addBlock(Block newBlock) {
      blockchain.add(newBlock);
  }
}


