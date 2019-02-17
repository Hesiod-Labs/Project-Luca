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
        VIEW_TRANSACTION_HISTORY,
        VIEW_TRANSACTION_REQUESTS,
        VIEW_ACCOUNT_HISTORY,
        VIEW_BANK_HISTORY,
        VIEW_PORTFOLIO_HISTORY,
    
        REMOVE_USER,
        VIEW_ALL_USERS,
        VIEW_LOGGED_IN_USER,
        VIEW_PORTFOLIO;
    }
    private static void login()
    {
        boolean run = true;
        while(run)
        {
            System.out.print("'Existing' or 'New' user: ");
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
                System.out.print("Enter your first, middle, and last name initials: ");
                String initials = commandLine.next();
                while(initials.toCharArray().length != 3)
                {
                    System.out.println("You must enter exactly 3 letters");
                    initials = commandLine.next();
                }
                userInfo.add(initials.trim());
                
                System.out.print("Enter your password: ");
                String pw = commandLine.next();
                userInfo.add(pw.trim());
                
                System.out.println("Provide 3 word to be used for your encryption hash (case sensitive):");
                
                System.out.print("1st word: ");
                addWordToUserHash();
                
                System.out.print("2nd word: ");
                addWordToUserHash();
                
                System.out.print("3rd word: ");
                addWordToUserHash();
                
                System.out.println("You will be set to a General User. An existing admin will update your status " +
                        "if necessary.");
                System.out.print("Enter contribution to the Account (U.S. dollar): ");
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
                System.out.print("Username:");
                String username = commandLine.next().toUpperCase();
                userInfo.add(username.trim());
                
                System.out.print("Password: ");
                String password = commandLine.next().toUpperCase();
                userInfo.add(password.trim());
                
                Iterator<User> findPerson = Account.getAccountUsers().iterator();
                
                while(findPerson.hasNext())
                {
                    User someone = findPerson.next();
                    if(someone.getUsername().equalsIgnoreCase(userInfo.get(0))
                            && someone.getPassword().equalsIgnoreCase(userInfo.get(1)))
                    {
                        loggedIn = someone;
                        runLuca();
                    }
                }
                System.out.println("User credentials do not exist within the account.");
            }
            if(userExistence.equalsIgnoreCase("Shutdown"))
            {
                commandLine.close();
                System.exit(0);
            }
            printUsersInfo();
            printAccountInfo();
        }
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
                {
                    userInfo.clear();
                    login();
                }
                
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
                        System.out.print("Specify the type of transaction: ");
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
                        ArrayList<String> assetInfo = new ArrayList<>();
                        if(transType.equalsIgnoreCase("Deposit") ||
                                transType.equalsIgnoreCase("Withdraw"))
                        {
                            System.out.print("Enter the amount associated with the transaction (U.S. dollar): ");
                            transInfo.add(isValidNumber());
                            
                            String requestType = transInfo.get(0);
                            double amount = Double.parseDouble(transInfo.get(1));
                            Transaction req = new Transaction(requestType, amount);
                            loggedIn.requestTransaction(req);
                            System.out.println("--- TRANSACTION INFO ---");
                            printTransactionReceipt(req);
                            if(req.getTransactionAsset() != null)
                            {
                                System.out.println("--- ASSOCIATED ASSET INFO ---");
                                printAssetInfo(req);
                            }
                        }
                        if(transType.equalsIgnoreCase("Buy") ||
                                transType.equalsIgnoreCase("Short"))
                        {
                            System.out.println("--- ENTER ASSET INFORMATION --- ");
                            
                            double doublePrice = 0;
                            double doubleVolume = 0;
                            double totalPrice = doublePrice * doubleVolume;
                            
                            boolean suffientFunds = false;
                            while(!suffientFunds)
                            {
                                System.out.print("Company Name: ");
                                assetInfo.add(commandLine.next().toUpperCase());
                                
                                System.out.print("Stock Symbol: ");
                                assetInfo.add(commandLine.next().toUpperCase());
                                
                                System.out.println("Valid Sectors: ");
                                System.out.println(Arrays.toString(Asset.Sector.values()));
                                System.out.println("\t" + "Sector: ");
                                assetInfo.add(commandLine.next().toUpperCase());
                                
                                System.out.println("Volume: ");
                                String inputVolume = commandLine.next();
                                assetInfo.add(inputVolume);
                                doubleVolume = Double.parseDouble(inputVolume);
                                
                                System.out.println("Start Price (U.S dollar): ");
                                String inputPrice = commandLine.next();
                                assetInfo.add(inputPrice);
                                doublePrice = Double.parseDouble(inputPrice);
                                
                                totalPrice = doublePrice * doubleVolume;
                                
                                if(totalPrice > Bank.getBankBalance().getCurrentValue())
                                {
                                    if(doublePrice > Bank.getBankBalance().getCurrentValue())
                                    {
                                        System.out.println("Unit stock price exceeds bank funds. Acquire a different asset.");
                                        assetInfo.clear();
                                    }
                                    System.out.println("Total price exceeds bank funds. Acquire either a different asset or fewer shares.");
                                    assetInfo.clear();
                                }
                                suffientFunds = true;
                                
                                System.out.println("Valid forms of transaction: " +
                                        "\n\t" + "LIMIT" +
                                        "\n\t" + "MARKET" +
                                        "\n\t" + "STOP_LIMIT" +
                                        "\n\t" + "STOP_LOSS" +
                                        "\n\t" + "TRAILING_STOP_LIMIT" +
                                        "\n\t" + "TRAILING_STOP_LOSS");
                                
                                System.out.println("Order Type: ");
                                String orderType = commandLine.next();
                                assetInfo.add(orderType.toUpperCase());
                            }
                            String name = assetInfo.get(0);
                            String symbol = assetInfo.get(1);
                            String sector = assetInfo.get(2);
                            int volume = Integer.parseInt(assetInfo.get(3));
                            double startPrice = Double.parseDouble(assetInfo.get(4));
                            String orderType = assetInfo.get(5);
                            Asset requestedAsset = new Asset(name, symbol, sector, volume, startPrice, orderType);
                            
                            Transaction newTrans = new Transaction(transInfo.get(0), requestedAsset);
                            loggedIn.requestTransaction(newTrans);
                            System.out.println("--- TRANSACTION INFO ---");
                            printTransactionReceipt(newTrans);
                            if(newTrans.getTransactionAsset() != null)
                            {
                                System.out.println("--- ASSOCIATED ASSET INFO ---");
                                printAssetInfo(newTrans);
                            }
                            assetInfo.clear();
                        }
                        
                        if(transType.equalsIgnoreCase("Sell") ||
                                transType.equalsIgnoreCase("Cover"))
                        {
                            Asset assetOfInterest = null;
                            if(!Portfolio.getPortfolio().isEmpty())
                            {
                                System.out.println("--- SEARCH FOR ASSET ---");
                                boolean found = false;
                                while(!found)
                                {
                                    System.out.println("Enter stock symbol to liquidate: ");
                                    String symbol = commandLine.next();
                                    Asset[] assets = new Asset[Portfolio.getPortfolio().size()];
                                    for(int i = 0; i < assets.length - 1; i++)
                                    {
                                        if(assets[i].getSymbol().equalsIgnoreCase(symbol))
                                        {
                                            assetOfInterest = assets[i];
                                            found = true;
                                        }
                                        else
                                        {
                                            System.out.println("Asset of interest does not exist in the portfolio.");
                                        }
                                    }
                                }
                                String type;
                                
                                System.out.println("Final price: ");
                                double finalPrice = Double.parseDouble(commandLine.next());
                                assetOfInterest.setEndPrice(finalPrice);
                                if(assetOfInterest.getAcquisitionTransaction().getTransactionType().name().equalsIgnoreCase("Buy"))
                                    type = "Sell";
                                else
                                    type = "Cover";
                                Transaction requested = new Transaction(type, assetOfInterest);
                                loggedIn.requestTransaction(requested);
                                System.out.println("--- TRANSACTION INFO ---");
                                printTransactionReceipt(requested);
                                if(requested.getTransactionAsset() != null)
                                {
                                    System.out.println("--- ASSOCIATED ASSET INFO ---");
                                    printAssetInfo(requested);
                                }
                            }
                            else
                            {
                                System.out.println("Portfolio is empty.");
                            }
                        }
                    }
                }
                if(userResponse.equalsIgnoreCase(Command.RESOLVE.toString()))
                {
                    if(loggedIn.getClearance() >= 2)
                    {
                        for(Transaction transaction : Account.getTransactionRequests())
                        {
                            System.out.println("--- TRANSACTION INFO ---");
                            printTransactionReceipt(transaction);
                            if(transaction.getTransactionAsset() != null)
                            {
                                System.out.println("--- ASSOCIATED ASSET INFO ---");
                                printAssetInfo(transaction);
                            }
                            System.out.println("'Deny' or 'Allow' the requested transaction: ");
                            boolean completed = false;
                            while(!completed)
                            {
                                String response = commandLine.next();
                                if(response.equalsIgnoreCase("Deny"))
                                {
                                    loggedIn.resolveTransaction(transaction, "DENIED");
                                    System.out.println("--- UPDATED TRANSACTION INFO ---");
                                    printTransactionReceipt(transaction);
                                    if(transaction.getTransactionAsset() != null)
                                    {
                                        System.out.println("--- UPDATED ASSOCIATED ASSET INFO ---");
                                        printAssetInfo(transaction);
                                    }
                                    completed = true;
                                }
                                if(response.equalsIgnoreCase("Allow"))
                                {
                                    loggedIn.resolveTransaction(transaction, transaction.getMatchingStatus().toString());
                                    System.out.println("--- UPDATED TRANSACTION INFO ---");
                                    printTransactionReceipt(transaction);
                                    if(transaction.getTransactionAsset() != null)
                                    {
                                        System.out.println("--- UPDATED ASSOCIATED ASSET INFO ---");
                                        printAssetInfo(transaction);
                                    }
                                    completed = true;
                                }
                                    if(!response.equalsIgnoreCase("Deny") && !response.equalsIgnoreCase("Allow"))
                                    {
                                        System.out.println("Enter either 'Deny' or 'Allow' to resolve the transaction.");
                                    }
                                }
                            }
                        }
                    else
                    {
                        System.out.println("You must have at least SECTOR HEAD clearance to resolve transactions.");
                    }
                }
                if(userResponse.equalsIgnoreCase(Command.VIEW_TRANSACTION_HISTORY.toString()))
                {
                    printTransactionHistory();
                }
                if(userResponse.equalsIgnoreCase(Command.VIEW_TRANSACTION_REQUESTS.toString()))
                {
                    printTransactionRequests();
                }
                if(userResponse.equalsIgnoreCase(Command.VIEW_ACCOUNT_HISTORY.toString()))
                {
                    printAccountBalanceHistory();
                }
                if(userResponse.equalsIgnoreCase(Command.VIEW_BANK_HISTORY.toString()))
                {
                    printBankBalanceHistory();
                }
                if(userResponse.equalsIgnoreCase(Command.VIEW_PORTFOLIO_HISTORY.toString()))
                {
                    printPortfolioHistory();
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
        User testadmin = new User("A", "D", "M", "password", "alpha", "beta", "chi",
                User.UserType.SYSTEM_ADMIN, 1000);
        User testOfficer = new User("O", "F", "F", "password", "the", "best", "officer", User.UserType.OFFICER, 500);
        User testSectorHead = new User("S", "E", "C", "password", "the", "best", "head", User.UserType.SECTOR_HEAD, 500);
        login();
        runLuca();
    }
    
    private static void printAccountInfo()
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
    
    private static void printUsersInfo()
    {
        for(User user : Account.getAccountUsers())
        {
            System.out.println("--- USER INFORMATION ---");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + user.getPassword());
            System.out.println("User Public Key: " + user.getUserPublicKey());
            System.out.println("User Private Key: " + user.getUserPrivateKey());
            System.out.println("Admin Status: " + user.getUserType());
            System.out.println("USER CONTRIBUTION: " + user.getUserContribution().getCurrentValue());
            System.out.println("Total contributions: $" + user.getUserContribution().getCurrentValue() + "0");
            System.out.println("% Holdings: " + user.calculatePctHoldings() + "%");
            System.out.println("Holdings Value: $" + user.calculateHoldingsValue() + "0");
            System.out.println("Time Created: " + user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println(" ");
        }
    }
    
    public static void printTransactionRequests()
    {
        System.out.println("--- TRANSACTION REQUESTS ---");
        for(Transaction request : Account.getTransactionRequests())
        {
            System.out.println("Timestamp: " + request.getTimestamp());
            System.out.println("Date Requested: " + request.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Request User: " + request.getRequestUser());
            System.out.println("Transaction Type: " + request.getTransactionType());
            System.out.println("Transaction Amount: " + request.getTransactionAmount());
            System.out.println("Transaction: " + request.getSignature());
            if(request.getTransactionAsset() != null)
            {
                System.out.println("Associated Asset Symbol: " + request.getTransactionAsset().getSymbol());
                System.out.println("Currently Owned: " + request.getTransactionAsset().isOwned());
            }
        }
        System.out.println(" ");
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
        System.out.println("Currently Owned: " + asset.isOwned());
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
        System.out.println("No transactions have been requested/resolved.");
    }
    
    public static void printAccountBalanceHistory()
    {
        System.out.println("--- ACCOUNT BALANCE HISTORY ---");
        Stack<Balance> accountHistory = Account.getAccountBalance().getBalanceHistory();
        while(!accountHistory.isEmpty())
        {
            for(Balance b : accountHistory)
            {
                System.out.println("Current Value: $" + b.getBalanceAmount() + "0");
                System.out.println("Timestamp: " + b.getBalanceTimeStamp());
            }
            System.out.println(" ");
        }
    }
    
    public static void printBankBalanceHistory()
    {
        System.out.println("--- BANK BALANCE HISTORY ---");
        Stack<Balance> bankHistory = Bank.getBankBalance().getBalanceHistory();
        while(!bankHistory.isEmpty())
        {
            for(Balance b : bankHistory)
            {
                System.out.println("Current Value: $" + b.getBalanceAmount() + "0");
                System.out.println("Timestamp: " + b.getBalanceTimeStamp());
            }
            System.out.println(" ");
        }
    }
    
    public static void printPortfolioHistory()
    {
        System.out.println("--- PORTFOLIO BALANCE HISTORY ---");
        Stack<Balance> portfolioHistory = Portfolio.getPortfolioBalance().getBalanceHistory();
        while(!portfolioHistory.isEmpty())
        {
            for(Balance b : portfolioHistory)
            {
                System.out.println("Current Value: $" + b.getBalanceAmount() + "0");
                System.out.println("Timestamp: " + b.getBalanceTimeStamp());
            }
            System.out.println(" ");
        }
    }
}