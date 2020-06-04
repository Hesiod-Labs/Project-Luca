import BTA.Transaction;
import LASER.Encryption;
import LASER.Laser;
import LucaMember.User;

public class TestLaser {

    public TestLaser() {

        String genBlock = Laser.createGenesisBlock();
        System.out.print(Laser.getBlockchainToString());
        User testUser = new User("c", "s", "m", "password", "wordone", "wordtwo",
                                 "wordthree", User.UserType.SYSTEM_ADMIN, 500);
        System.out.println("Created User:" + testUser.getUsername());
        System.out.println("Keys:" + '\n' + testUser.getUserPublicKey() + '\n' + testUser.getUserPrivateKey());
        Transaction trx = new Transaction(Transaction.Type.BUY, 500);
        System.out.println("Created Transaction:" + trx);
        String hash = Encryption.applySHA256("Transaction Data");
        System.out.print("Encrypted Hash:" + hash);
        byte[] sig = Encryption.applySignature(testUser.getUserPrivateKey(), "Transaction Data");
        System.out.println('\n');
        System.out.println("User Signature:" + sig);
        System.out.println('\n');
        boolean verify = Encryption.verifySignature(testUser.getUserPublicKey(), "Transaction Data", sig);
        System.out.println("Verify Signature:" + verify);
        Laser.addBlock(trx);
        System.out.println("New Block has been added");
        System.out.println(Laser.getBlockchainToString());
        }

    public static void main(String[] args) {
        new TestLaser();
    }
}
