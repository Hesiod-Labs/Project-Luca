import ABP.*;
import BTA.*;
import LucaMember.User;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class Testing
{
    
    Scanner commandLine = new Scanner(System.in);
    
    private static boolean isValidCommand(String input)
    {
        Command allCommands[] = Command.values();
        int i = 0;
        while(i < allCommands.length)
        {
            if(allCommands[i].name().equalsIgnoreCase(input))
                return true;
            else
                i++;
        }
        return false;
    }
    
    public enum Command
    {
        SHUTDOWN,
        LOGOUT,
        REQUEST,
        RESOLVE,
        ADD_USER,
        REMOVE_USER,
        VIEW_ALL_USERS,
        VIEW_LOGGED_IN_USER,
        VIEW_TRANSACTION_HISTORY,
        VIEW_TRANSACTION_REQUESTS,
        VIEW_ACCOUNT_BALANCE,
        VIEW_BANK_BALANCE,
        VIEW_PORTFOLIO_BALANCE,
        VIEW_PORTFOLIO;
       /*
        private Method methodToRun;
        
        static {
            try
            {
                REQUEST.methodToRun = User.class.getMethod("requestTransaction", Transaction.class);
                RESOLVE.methodToRun = User.class.getMethod("resolveTransaction", Transaction.class, String.class);
                ADD_USER.methodToRun = User.class.getMethod("addUser", User.class);
                REMOVE_USER.methodToRun = User.class.getMethod("removeUser", User.class);
                VIEW_ALL_USERS.methodToRun = Testing.class.getMethod("printUsersInfo");
            }
            catch(NoSuchMethodException nsm)
            {
                nsm.printStackTrace();
            }
        }
        */
    }
    
    private static void login()
    {
        ArrayList<String> userInfo = new ArrayList<>();
        boolean run = true;
        Scanner commandLine = new Scanner(System.in);
        while(run)
        {
            System.out.println("'Existing' or 'New' user: ");
            String userExistence = commandLine.next();
            if(!(userExistence.equalsIgnoreCase("Existing")) &&
                    !(userExistence.equalsIgnoreCase("New")) &&
                    !(userExistence.equalsIgnoreCase("Shutdown")))
            {
                System.out.println("Please enter one of the following valid commands: "
                        + "\n\t" + "EXISTING"
                        + "\n\t" + "NEW"
                        + "\n\t" + "SHUTDOWN" + "\n");
            }
    
            if(userExistence.equalsIgnoreCase("New"))
            {
                System.out.println("What are your first, middle, and last name initials?");
                String initials = commandLine.next();
                userInfo.add(initials.trim());
        
                System.out.println("Enter your password: ");
                String pw = commandLine.next();
                userInfo.add(pw.trim());
        
                System.out.println("Provide 3 word to be used for your encryption hash (case sensitive):");
                System.out.println("1st word: ");
                String first = commandLine.next();
                userInfo.add(first.trim());
        
                System.out.println("2nd word: ");
                String second = commandLine.next();
                userInfo.add(second.trim());
        
                System.out.println("3rd word: ");
                String third = commandLine.next();
                userInfo.add(third.trim());
        
                System.out.println("You will be set to a General User. An existing admin will update your status " +
                        "if necessary.");
                System.out.println("Enter contribution to the Account: ");
                String contribution = commandLine.next();
                userInfo.add(contribution.trim());
        
                new User(
                        userInfo.get(0).substring(0, 1),
                        userInfo.get(0).substring(1, 2),
                        userInfo.get(0).substring(2, 3),
                        userInfo.get(1),
                        userInfo.get(2),
                        userInfo.get(3),
                        userInfo.get(4),
                        User.UserType.GENERAL_USER,
                        Double.parseDouble(userInfo.get(5)));
                run = false;
            }
            
            if(userExistence.equalsIgnoreCase("Existing"))
            {
                userInfo.clear();
                
                System.out.println("Username:" );
                String username = commandLine.next();
                userInfo.add(username.trim());
    
                System.out.println("Password: ");
                String password = commandLine.next();
                userInfo.add(password.trim());
                
               User[] allUsers =  Account.getAccountUsers().toArray(new User[] {});
               
               for(User u : allUsers)
               {
                   if(u.getUsername().equalsIgnoreCase(userInfo.get(0)) &&
                           u.getPassword().equalsIgnoreCase(userInfo.get(1)))
                   {
                       runLuca();
                   }
                   else
                   {
                       System.out.println("User credentials do not exist within the account.");
                   }
               }
            }
        }
        printUsersInfo();
    }
    
    public static void runLuca()
    {
        boolean run = true;
        Scanner commandLine = new Scanner(System.in);
        while(run)
        {
            System.out.println("Enter a command: ");
            
            String userResponse = commandLine.next();
            
            if(isValidCommand(userResponse))
            {
                if(userResponse.equalsIgnoreCase(Command.SHUTDOWN.toString()))
                {
                    System.exit(0);
                    commandLine.reset();
                }
                
                if(userResponse.equalsIgnoreCase(Command.LOGOUT.toString()))
                    login();
            }
            else
            {
                System.out.println("Please enter any of the following valid commands: ");
                for(Command c : Command.values())
                {
                    System.out.println("\t" + c.toString());
                }
            }
        }
        commandLine.close();
        System.exit(0);
        }
    
    public static void main(String[] args) throws InterruptedException
    {
        long starTime = System.nanoTime();
    
        /* Create an  account with a bank, portfolio, and storage for users*/
        new Account("Hesiod Account", "Hesiod Bank", "Hesiod Portfolio");
        
        login();
        runLuca();
    
        long endTime = System.nanoTime();
        long totalTime = endTime - starTime;
        System.out.println("Total Runtime: " + totalTime / 1000000000.0 + " sec");
        
        /* Create a user and add it to the ABP */
        //User reqUser = new User("R", "E", "Q", "dog", "alpha", "beta", "chi", User.UserType.GENERAL_USER, 500);
        //User resUser = new User("R", "E", "S", "cat", "delta", "epsilon", "gamma", User.UserType.OFFICER, 500);
        
        /* Display general ABP information */
        /*
        printAccountInfo();
        
        printUsersInfo();
    
        Transaction t1 = new Transaction(1000, "Deposit");
        printTransactionReceipt(t1);
        reqUser.requestTransaction(t1);
        printTransactionReceipt(t1);
        resUser.resolveTransaction(t1, "Deposited");
        printTransactionReceipt(t1);
    
        Transaction t2 = new Transaction(200, "Withdraw");
        printTransactionReceipt(t2);
        reqUser.requestTransaction(t2);
        printTransactionReceipt(t2);
        resUser.resolveTransaction(t2, "Withdrawn");
        printTransactionReceipt(t2);
    
        Asset cmg_buy = new Asset("Chipotle", "CMG", "Consumers", 3, 100, "Market");
        Transaction t3 = new Transaction("Buy", cmg_buy);
        printTransactionReceipt(t3);
        printAssetInfo(t3);
        reqUser.requestTransaction(t3);
        printTransactionReceipt(t3);
        resUser.resolveTransaction(t3, "Bought");
        printTransactionReceipt(t3);
        printAssetInfo(t3);
    
        cmg_buy.setEndPrice(cmg_buy.getStartPrice() + 10);
        Transaction t4 = new Transaction("Sell", cmg_buy);
        printTransactionReceipt(t4);
        printAssetInfo(t4);
        reqUser.requestTransaction(t4);
        printTransactionReceipt(t4);
        resUser.resolveTransaction(t4, "Sold");
        printTransactionReceipt(t4);
        printAssetInfo(t4);
        
        Asset fb_short = new Asset("Facebook", "FB", "Technology", 3, 100, "Market");
        Transaction t5 = new Transaction("Short", fb_short);
        printTransactionReceipt(t5);
        printAssetInfo(t5);
        reqUser.requestTransaction(t5);
        printTransactionReceipt(t5);
        resUser.resolveTransaction(t5, "Shorted");
        printTransactionReceipt(t5);
        printAssetInfo(t5);
    
        Transaction t6 = new Transaction("Cover", fb_short);
        printTransactionReceipt(t6);
        printAssetInfo(t6);
        reqUser.requestTransaction(t6);
        printTransactionReceipt(t6);
        resUser.resolveTransaction(t6, "Covered");
        printTransactionReceipt(t6);
        printAssetInfo(t6);
        
        */
        printTransactionHistory();
        printBankBalanceHistory();
        printPortfolioHistory();
    }
   
    
    public static void printAccountInfo()
    {
        System.out.println("--- ACCOUNT INFORMATION ---");
        System.out.println("Account: " + Account.getAccountName());
        System.out.println("\t" + "Current Balance: $" + Account.getAccountBalance().getCurrentValue() + "0");
        System.out.println("Bank Name: " + Bank.getBankName());
        System.out.println("\t" + "Current Balance: $" + Bank.getBankBalance().getCurrentValue() + "0");
        System.out.println("Portfolio Name: " + Portfolio.getNameOfPortFolio());
        System.out.println("\t" + "Current Balance: $" + Portfolio.getPortfolioBalance().getCurrentValue() + "0");
        System.out.println("Number of Users: " + Account.getAccountUsers().size());
        System.out.println(" ");
    }
    
    public static void printUsersInfo()
    {
        for(User user : Account.getAccountUsers())
        {
            System.out.println("--- USER INFORMATION ---");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + user.getPassword());
            System.out.println("");
            System.out.println("Admin Status: " + user.getUserType());
            System.out.println("Total contributions: $" + user.getUserContribution().getCurrentValue() + "0");
            //System.out.println("% Holdings: " + user.roundToThree(user.calculatePctHoldings()) + "%");
            System.out.println("% Holdings: " + user.calculatePctHoldings() + "%");
            System.out.println("Holdings Value: $" + user.calculateHoldingsValue() + "0");
            System.out.println("Time Created: " + user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println(" ");
        }
    }
    
    public static void printTransactionReceipt(Transaction transaction)
    {
        if(!transaction.getTransactionStatus().name().equalsIgnoreCase("Cancelled"))
        {
            System.out.println("--- " + transaction.getTransactionType() + " ---");
            System.out.println("Transaction ID: " + transaction.getTransactionID());
            if(transaction.getRequestUser() != null)
            {
                System.out.println("Requested By: " + transaction.getRequestUser().getUsername());
                System.out.println("Request Date: " + transaction.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            }
            else
            {
                System.out.println("Requested By: Not yet requested");
                System.out.println("Request Date: Not yet requested");
            }
            if(transaction.getResolveUser() != null)
            {
                System.out.println("Resolved By: " + transaction.getResolveUser().getUsername());
                System.out.println("Resolve Date: " + transaction.getResolveDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            }
            else
            {
                System.out.println("Resolved By: Not yet resolved");
                System.out.println("Resolve Date: Not yet resolved");
            }
            System.out.println("Transaction Status: " + transaction.getTransactionStatus());
            System.out.println("Transaction Type: " + transaction.getTransactionType());
            System.out.println("Transaction Amount: $" + transaction.getTransactionAmount() + "0");
        }
        else
            System.out.println("Transaction was cancelled.");
        System.out.println(" ");
    }
    
    public static void printAssetInfo(Transaction transaction)
    {
        System.out.println("------ ASSET INFORMATION ---");
        Asset asset = transaction.getTransactionAsset();
        System.out.println("Asset Name: " + asset.getAssetName() + " (" + asset.getSymbol() + ")");
        System.out.println("Number of Shares: " + asset.getVolume());
        System.out.println("Start Price: $" + asset.getStartPrice() + "0");
        if(asset.getEndPrice() != 0)
        {
            System.out.println("Asset Final Price: $" + asset.getEndPrice());
            System.out.println("Returns on Asset: $" + asset.getReturns() + "0");
            System.out.println("Asset Held for: " + asset.getTimeHeld() + " seconds");
        }
        else
        {
            System.out.println("Asset Final Price: Asset has not been liquidated");
            System.out.println("Returns on Asset: Asset has not been liquidated");
            System.out.println("Asset Held for: Asset has not been liquidated");
        }
        System.out.println(" ");
    }
    
    public static void printTransactionHistory()
    {
        System.out.println("--- TRANSACTION HISTORY ---");
        while(!Account.getTransactionHistory().isEmpty())
        {
            Transaction hist_trans = Account.getTransactionHistory().pop();
            System.out.println("Transaction ID: " + hist_trans.getTransactionID());
            System.out.println("Transaction Type: " + hist_trans.getTransactionType());
            System.out.println("Transaction Amount: " + hist_trans.getTransactionAmount());
            if(!(hist_trans.getTransactionAsset() == null))
                System.out.println("Associated Asset Symbol: " + hist_trans.getTransactionAsset().getSymbol());
            else
                System.out.println("Associated Asset Symbol: No associated asset with transaction.");
            System.out.println(" ");
        }
    }
    
    public static void printBankBalanceHistory()
    {
        System.out.println("--- BANK BALANCE HISTORY ---");
        while(!Bank.getBankBalance().getBalanceHistory().isEmpty())
        {
            Balance hist_bal = Bank.getBankBalance().getBalanceHistory().pop();
            System.out.println("Balance: $" + hist_bal.getBalanceAmount() + "0");
            
            if(!(hist_bal.getAssociatedTransaction() == null))
            {
                System.out.println("Associated Transaction ID: " + hist_bal.getAssociatedTransaction().getTransactionID());
                
                if(hist_bal.getBalanceTimeStamp().isEqual(hist_bal.getAssociatedTransaction().getResolveDate()))
                    System.out.println("Balance Timestamp: " + hist_bal.getBalanceTimeStamp().format(DateTimeFormatter.RFC_1123_DATE_TIME) +
                            " [MATCHES RESOLVED TRANSACTION TIMESTAMP]");
                else
                    System.out.println("Balance Timestamp: " + hist_bal.getBalanceTimeStamp().format(DateTimeFormatter.RFC_1123_DATE_TIME) +
                            " [DOES NOT MATCH RESOLVED TRANSACTION TIMESTAMP]");
            }
            else
                System.out.println("Associated Transaction ID: No associated transaction");
            
            System.out.println(" ");
        }
    }
    
    public static void printPortfolioHistory()
    {
        System.out.println("--- PORTFOLIO HISTORY ---");
        while(!Portfolio.getPortfolioBalance().getBalanceHistory().isEmpty())
        {
            Balance port_hist = Portfolio.getPortfolioBalance().getBalanceHistory().pop();
            System.out.println("Associated Transaction: " + port_hist.getAssociatedTransaction().getTransactionID());
            System.out.println("Balance: $" + port_hist.getBalanceAmount() + "0");
            
            if(port_hist.getBalanceTimeStamp().isEqual(port_hist.getAssociatedTransaction().getResolveDate()))
                System.out.println("Balance Timestamp: " + port_hist.getBalanceTimeStamp().format(DateTimeFormatter.RFC_1123_DATE_TIME) +
                        " [MATCHES RESOLVED TRANSACTION TIMESTAMP]");
            else
                System.out.println("Balance Timestamp: " + port_hist.getBalanceTimeStamp().format(DateTimeFormatter.RFC_1123_DATE_TIME) +
                        " [DOES NOT MATCH RESOLVED TRANSACTION TIMESTAMP]");
            System.out.println(" ");
        }
    }
}
