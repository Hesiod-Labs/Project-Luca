import java.security.*;

//L.A.S.E.R.: Luca Auditing Security Enterprise Repository
public class Laser {
  
  private boolean permissionGranted = false;
  
  public boolean checkPermission(Enterprise request, Address user) {
    
    //if the enterprise request is permissioned by the qualifications of the user then access is granted
    //if not access is denied 
    
    return false;
  }
  
  //Returns true if new transaction could be created. 
  public boolean processTransaction() {
    
    return true;
  }
  
  
  
  //Verifies who the original requesting user using the signature, data, and public key without translating the data itself
  public static boolean verifiySignature(Enterprise request) {
    
    return Encryption.verifyECDSASig(request.publicKey, request.enterpriseData, request.signature);
  }

  
 // public boolean verifyRuntimeHash();
  
  
  //needs a concatenate 3 words method or submethod
  public boolean compareHashes(String word1, String word2, String word3, Address address) {
    
    String accessHash = Encryption.applySHA256(word1 + word2 + word3);
    
    if (accessHash == address.runtimeHash) {
    }
    return false;
  }
  
  public void provideAdminAccess(Address address, PrivateKey adminPrKey, PublicKey adminPuKey) {
    
    address.adminPrivateKey = adminPrKey;
    address.adminPublicKey = adminPuKey;
    address.adminPermissioned = true;
  }
  
  public void provideSectorAccess(Address address, PrivateKey sectorPrKey, PublicKey sectorPuKey, PublicKey adminPuKey) {
    
    address.sectorPrivateKey = sectorPrKey;
    address.sectorPublicKey = sectorPuKey;
    address.sectorPermissioned = true;
    address.adminPublicKey = adminPuKey;
  }
  
}



