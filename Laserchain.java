import java.security.MessageDigest;
import java.security.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Date;

public class Laserchain {
  
  public static ArrayList<Block> blockchain = new ArrayList<Block>(); //the blockchain itself
 
  //checks the validity of the blockchian after every block add
  // TODO
  public static boolean isChainValid() {
    
    Block currentBlock; 
    Block previousBlock;
    
    //loop through blockchain to check hashes:
    for(int index = 1; index < blockchain.size(); index++) {
      
      currentBlock = blockchain.get(index);
      previousBlock = blockchain.get(index - 1);
      //compare registered hash and calculated hash:
      if(!currentBlock.hash.equals(currentBlock.blockHash()) ){
        return false;
      }
      //compare previous hash and registered previous hash
      if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
        return false;
      }
    }
    return true;
  }
  
  //adds a new block to the blockchain 
  public static void addBlock(Block newBlock) {
    blockchain.add(newBlock);
  }
  
  //main method for the functionally running blockchain 
  public static void main(String[] args) { 
    
    isChainValid();
  }
  
}