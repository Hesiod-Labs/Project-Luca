public class Testing
{
    public static void main(String[] args)
    {
        long starTime = System.nanoTime();
        
        
        Account.setPortfolio(new Portfolio("Hesiod Financial"));
        User user = new User("John", "A", "Smith", "dog", true, 0);
        
        //Transaction trans1 = new Transaction(user, 500, "Deposit");
        user.requestTransaction(user, 500, "Deposit");
        user.resolveTransaction(user, Transaction.getTransactionRequests().getFirst(), "Deposited");
        
        user.requestTransaction(user, 300, "Deposit");
        //Transaction trans2 = new Transaction(user, 200, "Deposit");
        user.resolveTransaction(user, Transaction.getTransactionRequests().getFirst(), "Deposited");
        
        
        long endTime = System.nanoTime();
    
        long totalTime = endTime - starTime;
    
        System.out.println("Runtime: " + totalTime / 1000000000.0); // in seconds
        
        System.out.println(Portfolio.getNameOfPortFolio());
        
        System.out.println("Bank balance: " + Bank.getBankBalance().getBalanceHistory().pop().getBalanceAmount());
        System.out.println("User balance: " + user.getUserBalance().getBalanceHistory().pop().getBalanceAmount());
        
        /*
        // Print all user information
        System.out.println(user.formatName());
        System.out.println("Username (Password): " + user.getUsername() + " (" + user.getPassword() + ")");
        System.out.println("Has Admin Permissions: " + user.hasPermission());
        System.out.println("User Contribution: $" + user.getContribution() + "0");
        System.out.println("Time of User Creation: " + user.formatTime(user.getTimeCreated()));
        */
        /*
        // Print all transaction information
        System.out.println(trans1.getReqUser().formatName());
        System.out.println(trans1.getOrderID());
        System.out.println(trans1.getStatus());
        System.out.println(trans1.getRequestDate());
        //System.out.println(trans1.getAdminUser().formatName());
        System.out.println(trans1.getTransactionAmount());
        System.out.println(trans1.getType());
        System.out.println(trans1.getResolveDate());
         */
    }
}
