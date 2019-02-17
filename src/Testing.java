import ABP.*;
import BTA.*;
import LucaMember.User;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class Testing
{
    
    private static Scanner commandLine = new Scanner(System.in);
    private static ArrayList<String> userInfo = new ArrayList<>();
    private static User loggedIn;
    
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
    }
        private static void login()
        {
            boolean run = true;
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
                    while(initials.toCharArray().length != 3)
                    {
                        System.out.println("You must enter exactly 3 letters");
                        initials = commandLine.next();
                    }
                    userInfo.add(initials.trim());
                
                    System.out.println("Enter your password: ");
                    String pw = commandLine.next();
                    userInfo.add(pw.trim());
                
                    System.out.println("Provide 3 word to be used for your encryption hash (case sensitive):");
                
                    System.out.println("1st word: ");
                    addWordToUserHash();
                
                    System.out.println("2nd word: ");
                    addWordToUserHash();
                
                    System.out.println("3rd word: ");
                    addWordToUserHash();
                
                    System.out.println("You will be set to a General User. An existing admin will update your status " +
                            "if necessary.");
                    System.out.println("Enter contribution to the Account (U.S. dollar): ");
                    userInfo.add(isValidNumber());
                
                    loggedIn = new User(
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
                
                    System.out.println("Username:");
                    String username = commandLine.next();
                    userInfo.add(username.trim());
                
                    System.out.println("Password: ");
                    String password = commandLine.next();
                    userInfo.add(password.trim());
                
                    User[] allUsers = Account.getAccountUsers().toArray(new User[]{});
                
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
                
                if(userExistence.equalsIgnoreCase("Shutdown"))
                {
                    commandLine.close();
                    System.exit(0);
                }
            }
            printUsersInfo();
        }
    
        private static void addWordToUserHash()
        {
            String word = commandLine.next();
            while(word.toCharArray().length == 0)
            {
                System.out.println("You must enter at least 1 character.");
            }
            userInfo.add(word.trim());
        }
    
        private static void runLuca()
        {
            long startTime = System.nanoTime();
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
                        run = false;
                        System.exit(0);
                        commandLine.reset();
                    }
                
                    if(userResponse.equalsIgnoreCase(Command.LOGOUT.toString()))
                        login();
                
                    if(userResponse.equalsIgnoreCase(Command.REQUEST.toString()))
                    {
                        ArrayList<String> transInfo = new ArrayList<>();
                        if(loggedIn.getClearance() >= 1)
                        {
                            System.out.println("Valid forms of transaction: " +
                                    "\n\t" + "DEPOSIT" +
                                    "\n\t" + "WITHDRAW" +
                                    "\n\t" + "BUY" +
                                    "\n\t" + "SELL" +
                                    "\n\t" + "SHORT" +
                                    "\n\t" + "COVER");
                            System.out.println("Specify the type of transaction: ");
                            String transType = commandLine.next();
                            if(!(transType.equalsIgnoreCase("Deposit")) &&
                                    !(transType.equalsIgnoreCase("Withdraw")) &&
                                    !(transType.equalsIgnoreCase("Buy")) &&
                                    !(transType.equalsIgnoreCase("Sell")) &&
                                    !(transType.equalsIgnoreCase("Short")) &&
                                    !(transType.equalsIgnoreCase("Cover")))
                            {
                                System.out.println("Please enter one of the following valid forms of transaction: " +
                                        "\n\t" + "DEPOSIT" +
                                        "\n\t" + "WITHDRAW" +
                                        "\n\t" + "BUY" +
                                        "\n\t" + "SELL" +
                                        "\n\t" + "SHORT" +
                                        "\n\t" + "COVER");
                            }
                            else
                                transInfo.add(transType);
                        
                            if(transType.equalsIgnoreCase("Deposit") ||
                                    transType.equalsIgnoreCase("Withdraw"))
                            {
                                System.out.println("Enter the amount associated with the transaction (U.S. dollar): ");
                                transInfo.add(isValidNumber());
                            }
                        }
                    }
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
            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            System.out.println("Total Runtime: " + totalTime / 1000000000.0 + " sec");
            System.exit(0);
        }
    
        private static String isValidNumber()
        {
            boolean numError = true;
            boolean negNum = true;
            double amount = 0;
            while(numError || negNum)
            {
                try
                {
                    amount = Double.parseDouble(commandLine.next());
                    numError = false;
                    if(amount < 0)
                        System.out.println("Entered amount must be non-negative.");
                    else
                    {
                        negNum = false;
                    }
                }
                catch(NumberFormatException e)
                {
                    System.out.println("Only enter numeric values.");
                }
            }
            return Double.toString(amount);
        }
    
        public static void main(String[] args) throws InterruptedException
        {
            /* Create an  account with a bank, portfolio, and storage for users*/
            new Account("Hesiod Account", "Hesiod Bank", "Hesiod Portfolio");
            User admin = new User("A", "D", "M", "password", "alpha", "beta", "chi",
                    User.UserType.SYSTEM_ADMIN, 10);
        
            login();
            runLuca();
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
                System.out.println("User Public Key: " + user.getUserPublicKey());
                System.out.println("User Private Key: " + user.getUserPrivateKey());
                System.out.println("Admin Status: " + user.getUserType());
                System.out.println("USER CONTRIBUTION: " + user.getUserContribution());
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
                System.out.println("Transaction Data: " + transaction.getTransactionData());
                System.out.println("Transaction Signature: " + Arrays.toString(transaction.getSignature()));
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
                System.out.println("Timestamp: " + hist_trans.getTimestamp());
                System.out.println("Date Requested: " + hist_trans.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
                System.out.println("Request User: " + hist_trans.getRequestUser());
                System.out.println("Date Resolved: " + hist_trans.getResolveDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
                System.out.println("Resolve User: " + hist_trans.getResolveUser());
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

