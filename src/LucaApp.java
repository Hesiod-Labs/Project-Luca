import ABP.*;
import BTA.*;
import LASER.*;
import LucaMember.User;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.System.*;

public class LucaApp
{
    private static Scanner commandLine = new Scanner(in);
    private static ArrayList<String> userInfo = new ArrayList<>();
    private static ArrayList<String> transInfo = new ArrayList<>();
    private static ArrayList<String> assetInfo = new ArrayList<>();
    private static User loggedIn;
    private static boolean run = true;
    
    public static void main(String[] args)
    {
        long startTime = nanoTime();
        /* Create an  account with a bank, portfolio, and storage for users*/
        new Account("Hesiod Account", "Hesiod Bank", "Hesiod Portfolio");
        new User("A", "D", "M", "password", "alpha", "beta", "chi", User.UserType.SYSTEM_ADMIN, 1000);
        new User("O", "F", "F", "password", "the", "best", "officer", User.UserType.OFFICER, 500);
        new User("S", "E", "C", "password", "the", "best", "head", User.UserType.SECTOR_HEAD, 500);
        try
        {
            login();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            runLuca();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        long endTime = nanoTime();
        long totalTime = endTime - startTime;
        out.println("Total Runtime: " + totalTime / 1000000000.0 + " sec");
        exit(0);
    }
    
    private static void login() throws IOException
    {
        boolean run = true;
        while(run)
        {
            out.print("'Existing' or 'New' user: ");
            String userExistence = commandLine.next();
            if(!(userExistence.equalsIgnoreCase("Existing")) &&
                    !(userExistence.equalsIgnoreCase("New")) &&
                    !(userExistence.equalsIgnoreCase("Shutdown")))
            {
                out.println("Please enter one of the following valid commands: "
                        + "\n\t" + "EXISTING"
                        + "\n\t" + "NEW"
                        + "\n\t" + "SHUTDOWN" + "\n");
            }
            
            if(userExistence.equalsIgnoreCase("New"))
            {
                out.print("Enter your first, middle, and last name initials: ");
                String initials = commandLine.next();
                while(initials.toCharArray().length != 3)
                {
                    out.println("You must enter exactly 3 letters");
                    initials = commandLine.next();
                }
                userInfo.add(initials.trim());
                
                out.print("Enter your password: ");
                String pw = commandLine.next();
                userInfo.add(pw.trim());
                out.println("Provide 3 word to be used for your encryption hash (case sensitive):");
                
                out.print("1st word: ");
                addWordToUserHash();
                
                out.print("2nd word: ");
                addWordToUserHash();
                
                out.print("3rd word: ");
                addWordToUserHash();
                
                out.println("You will be set to a General User. An existing admin will update your status " +
                        "if necessary.");
                out.print("Enter contribution to the Account (U.S. dollar): ");
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
                out.print("Username: ");
                String username = commandLine.next().toUpperCase();
                userInfo.add(username.trim());
                
                out.print("Password: ");
                String password = commandLine.next().toUpperCase();
                userInfo.add(password.trim());
                
                for(User someone : Account.getAccountUsers())
                {
                    if(someone.getUsername().equalsIgnoreCase(userInfo.get(0))
                            && someone.getPassword().equalsIgnoreCase(userInfo.get(1)))
                    {
                        loggedIn = someone;
                        run = false;
                        runLuca();
                    }
                }
                out.println("User credentials do not exist within the account.");
            }
            if(userExistence.equalsIgnoreCase("Shutdown"))
            {
                shutdown();
            }
        }
    }
    
    private static void shutdown()
    {
        run = false;
        commandLine.reset();
        exit(0);
    }
    
    private static void logout()
    {
        userInfo.clear();
        try
        {
            login();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void runLuca() throws IOException
    {
        while(run)
        {
            out.println("Enter a command: ");
            
            String userResponse = commandLine.next();
            
            if(isValidCommand(userResponse))
            {
                if(userResponse.equalsIgnoreCase(Command.SHUTDOWN.toString()))
                {
                    shutdown();
                }
                if(userResponse.equalsIgnoreCase(Command.LOGOUT.toString()))
                {
                    logout();
                }
                if(userResponse.equalsIgnoreCase(Command.REQUEST.toString()))
                {
                    if(loggedIn.getClearance() >= 1)
                    {
                        printValidTransactions();
                        
                        out.print("Specify the type of transaction: ");
                        String transType = commandLine.next();
                        
                        for(Transaction.Type t : Transaction.Type.values())
                        {
                            if(transType.equalsIgnoreCase(t.toString()))
                                transInfo.add(transType);
                            else
                            {
                                printValidTransactions();
                            }
                        }
                        if(transType.equalsIgnoreCase("Deposit") || transType.equalsIgnoreCase("Withdraw"))
                        {
                            out.print("Enter the amount associated with the transaction (U.S. dollar): ");
                            transInfo.add(isValidNumber());
                            
                            Transaction.Type requestType = Transaction.Type.valueOf(transInfo.get(0));
                            double amount = Double.parseDouble(transInfo.get(1));
                            
                            Transaction req = new Transaction(requestType, amount);
                            loggedIn.requestTransaction(req);
                            out.println("--- TRANSACTION INFO ---");
                            printTransactionReceipt(req);
                        }
                        
                        if(transType.equalsIgnoreCase("Buy") || transType.equalsIgnoreCase("Short"))
                        {
                            out.println("--- ENTER ASSET INFORMATION --- ");
                            
                            double doublePrice;
                            double doubleVolume;
                            double totalPrice;
                            
                            boolean sufficientFunds = false;
                            while(!sufficientFunds)
                            {
                                out.print("Company Name: ");
                                assetInfo.add(commandLine.next().toUpperCase());
                                
                                out.print("Stock Symbol: ");
                                assetInfo.add(commandLine.next().toUpperCase());
                                
                                out.println("Valid Sectors: ");
                                out.println(Arrays.toString(Asset.Sector.values()));
                                out.println("\t" + "Sector: ");
                                assetInfo.add(commandLine.next().toUpperCase());
                                
                                out.println("Volume: ");
                                String inputVolume = commandLine.next();
                                assetInfo.add(inputVolume);
                                doubleVolume = Double.parseDouble(inputVolume);
                                
                                out.println("Start Price (U.S dollar): ");
                                String inputPrice = commandLine.next();
                                assetInfo.add(inputPrice);
                                doublePrice = Double.parseDouble(inputPrice);
                                
                                totalPrice = doublePrice * doubleVolume;
                                
                                if(totalPrice > Bank.getBankBalance().getCurrentValue())
                                {
                                    if(doublePrice > Bank.getBankBalance().getCurrentValue())
                                    {
                                        out.println("Unit stock price exceeds bank funds. Acquire a different asset.");
                                        assetInfo.clear();
                                    }
                                    out.println("Total price exceeds bank funds. Acquire either a different asset or fewer shares.");
                                    assetInfo.clear();
                                }
                                sufficientFunds = true;
                                
                                printValidOrderTypes();
                                
                                out.println("Order Type: ");
                                String orderType = commandLine.next();
                                assetInfo.add(orderType.toUpperCase());
                            }
                            String name = assetInfo.get(0);
                            String symbol = assetInfo.get(1);
                            Asset.Sector sector = Asset.Sector.valueOf(assetInfo.get(2));
                            int volume = Integer.parseInt(assetInfo.get(3));
                            double startPrice = Double.parseDouble(assetInfo.get(4));
                            Asset.OrderType orderType = Asset.OrderType.valueOf(assetInfo.get(5));
                            Asset requestedAsset = new Asset(name, symbol, sector, volume, startPrice, orderType);
                            
                            printTransactionAndAsset(Transaction.Type.valueOf(transInfo.get(0)), requestedAsset);
                            assetInfo.clear();
                        }
                        
                        if(transType.equalsIgnoreCase("Sell") || transType.equalsIgnoreCase("Cover"))
                        {
                            if(!Portfolio.getPortfolio().isEmpty())
                            {
                                out.println("--- SELECT ASSET ---");
                                int i = 0;
                                for(Asset a : Portfolio.getPortfolio())
                                {
                                    out.println("Name: " + a.getAssetName() + " (" + a.getSymbol() + ")");
                                    out.println("Enter 'yes' if to liquidate this holding and 'no' to move on to the next asset: ");
                                    if(commandLine.next().equalsIgnoreCase("yes"))
                                    {
                                        i++;
                                        String type;
                                        out.println("Final price: ");
                                        double finalPrice = Double.parseDouble(commandLine.next());
                                        a.setEndPrice(finalPrice);
                                        if(a.getAcquisitionTransaction().getTransactionType().name().equalsIgnoreCase("Buy"))
                                            type = "Sell";
                                        else
                                            type = "Cover";
                                        printTransactionAndAsset(Transaction.Type.valueOf(type), a);
                                    }
                                }
                                if(i == 0)
                                {
                                    out.println("No asset was selected to be liquidated.");
                                }
                            }
                            else
                            {
                                out.println("Empty portfolio.");
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
                            Transaction.Type transType = transaction.getTransactionType();
                            Asset transAsset = transaction.getTransactionAsset();
                            printTransactionAndAsset(transType, transAsset);
                            
                            out.println("'Deny' or 'Allow' the requested transaction: ");
                            boolean completed = false;
                            while(!completed)
                            {
                                String response = commandLine.next();
                                if(response.equalsIgnoreCase("Deny"))
                                {
                                    loggedIn.resolveTransaction(transaction, "DENIED");
                                    out.println("--- UPDATED ---");
                                    printTransactionAndAsset(transType, transAsset);
                                    completed = true;
                                }
                                if(response.equalsIgnoreCase("Allow"))
                                {
                                    loggedIn.resolveTransaction(transaction, transaction.getMatchingStatus().toString());
                                    out.println("--- UPDATED ---");
                                    printTransactionAndAsset(transType, transAsset);
                                    completed = true;
                                }
                                if(!response.equalsIgnoreCase("Deny") && !response.equalsIgnoreCase("Allow"))
                                {
                                    out.println("Enter either 'Deny' or 'Allow' to resolve the transaction.");
                                }
                            }
                        }
                    }
                    else
                    {
                        out.println("You must have at least SECTOR HEAD clearance to resolve transactions.");
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
                //TODO Fix calculation
                if(userResponse.equalsIgnoreCase(Command.VIEW_PORTFOLIO_HISTORY.toString()))
                {
                    double netReturns = 0;
                    for(Transaction t : Account.getTransactionHistory())
                    {
                        if(t.getTransactionAsset() != null)
                        {
                            netReturns = (netReturns + t.getTransactionAsset().getReturns())/2;
                        }
                    }
                    printPortfolioHistory();
                    out.println("Net Returns: $" + netReturns + "0");
                }
                if(userResponse.equalsIgnoreCase(Command.VIEW_LOGGED_IN_USER.toString()))
                {
                    out.println("--- LOGGED-IN USER INFO ---");
                    out.println("Username: " + loggedIn.getUsername());
                    out.println("Password: " + loggedIn.getPassword());
                    out.println("Public Key: " + loggedIn.getUserPublicKey());
                    out.println("Private Key: " + loggedIn.getUserPrivateKey());
                    out.println("Clearance: " + loggedIn.getUserType());
                    out.println("Time Created: " + loggedIn.getTimeCreated());
                    out.println("Runtime Hash : " + loggedIn.getRunTimeHash());
                    out.println("Percent Holdings: " + loggedIn.calculatePctHoldings() + "%");
                }
                if(userResponse.equalsIgnoreCase(Command.VIEW_ALL_USERS.toString()))
                {
                    printUsersInfo();
                }
                if(userResponse.equalsIgnoreCase(Command.VIEW_ACCOUNT_INFO.toString()))
                {
                    printAccountInfo();
                }
                if(userResponse.equalsIgnoreCase(Command.VIEW_PORTFOLIO.toString()))
                {
                    out.println("--- PORTFOLIO HOLDINGS ---");
                    for(Asset a : Portfolio.getPortfolio())
                    {
                        out.println("Name (symbol) [sector]: " + a.getAssetName() + " (" + a.getSymbol() + ")" + " [" + a.getSector() + "]");
                    }
                }
                if(userResponse.equalsIgnoreCase(Command.VIEW_BLOCKCHAIN.toString()))
                {
                    out.println("--- BLOCK CHAIN ---");
                    out.println(Arrays.toString(Laser.getBlockchain().toArray()));
                }
            }
            else
            {
                out.println("Please enter any of the following valid commands: ");
                for(Command c : Command.values())
                {
                    out.println("\t" + c.toString());
                }
            }
        }
        commandLine.close();
    }
    
    private static void addWordToUserHash()
    {
        String word = commandLine.next();
        while(word.toCharArray().length == 0)
        {
            out.println("You must enter at least 1 character.");
        }
        userInfo.add(word.trim());
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
                    out.println("Entered amount must be non-negative.");
                else
                {
                    negNum = false;
                }
            }
            catch(NumberFormatException e)
            {
                out.println("Only enter numeric values.");
            }
        }
        return Double.toString(amount);
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
        VIEW_ALL_USERS,
        VIEW_ACCOUNT_INFO,
        VIEW_LOGGED_IN_USER,
        VIEW_PORTFOLIO,
        VIEW_BLOCKCHAIN
    }
    
    private static void printValidTransactions()
    {
        out.println("Valid forms of transaction: ");
        out.println(Arrays.toString(Transaction.Type.values()));
    }
    
    private static void printValidOrderTypes()
    {
        out.println("Valid order types: ");
        out.println(Arrays.toString(Asset.OrderType.values()));
    }
    
    private static boolean isValidCommand(String input)
    {
        Command[] allCommands = Command.values();
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
    
    private static void printAccountInfo()
    {
        out.println("--- ACCOUNT INFORMATION ---");
        out.println("Account: " + Account.getAccountName());
        out.println("\t" + "Current Balance: $" + Account.getAccountBalance().getCurrentValue() + "0");
        out.println("Bank Name: " + Bank.getBankName());
        out.println("\t" + "Current Balance: $" + Bank.getBankBalance().getCurrentValue() + "0");
        out.println("Portfolio Name: " + Portfolio.getNameOfPortFolio());
        out.println("\t" + "Current Balance: $" + Portfolio.getPortfolioBalance().getCurrentValue() + "0");
        out.println("Number of Users: " + Account.getAccountUsers().size());
        out.println(" ");
    }
    
    private static void printUsersInfo()
    {
        for(User user : Account.getAccountUsers())
        {
            out.println("--- USER INFORMATION ---");
            out.println("Username: " + user.getUsername());
            out.println("Password: " + user.getPassword());
            out.println("User Public Key: " + user.getUserPublicKey());
            out.println("User Private Key: " + user.getUserPrivateKey());
            out.println("Admin Status: " + user.getUserType());
            out.println("USER CONTRIBUTION: " + user.getUserContribution().getCurrentValue());
            out.println("Total contributions: $" + user.getUserContribution().getCurrentValue() + "0");
            out.println("% Holdings: " + user.calculatePctHoldings() + "%");
            out.println("Holdings Value: $" + user.calculateHoldingsValue() + "0");
            out.println("Time Created: " + user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            out.println(" ");
        }
    }
    
    private static void printTransactionRequests()
    {
        out.println("--- TRANSACTION REQUESTS ---");
        for(Transaction request : Account.getTransactionRequests())
        {
            out.println("Timestamp: " + request.getTimestamp());
            out.println("Date Requested: " + request.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            out.println("Request User: " + request.getRequestUser().getUsername());
            out.println("Transaction Type: " + request.getTransactionType());
            out.println("Transaction Amount: " + request.getTransactionAmount());
            out.println("Transaction Signature: " + Arrays.toString(request.getSignature()));
            if(request.getTransactionAsset() != null)
            {
                out.println("Associated Asset Symbol: " + request.getTransactionAsset().getSymbol() + "(" + request.getTransactionAsset().getSector() + ")");
                out.println("Currently Owned: " + request.getTransactionAsset().isOwned());
            }
            out.println(" ");
        }
    }
    
    private static void printTransactionAndAsset(Transaction.Type transType, Asset a)
    {
        Transaction req = new Transaction(transType, a);
    
        try
        {
            loggedIn.requestTransaction(req);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        out.println("--- TRANSACTION INFO ---");
        printTransactionReceipt(req);
        if(req.getTransactionAsset() != null)
        {
            out.println("--- ASSOCIATED ASSET INFO ---");
            printAssetInfo(req);
        }
    }
    
    private static void printTransactionReceipt(Transaction transaction)
    {
        if(!transaction.getTransactionStatus().name().equalsIgnoreCase("Cancelled"))
        {
            out.println("--- " + transaction.getTransactionType() + " ---");
            out.println("Transaction Data: " + transaction.getTransactionData());
            out.println("Transaction Signature: " + Arrays.toString(transaction.getSignature()));
            if(transaction.getRequestUser() != null)
            {
                out.println("Requested By: " + transaction.getRequestUser().getUsername());
                out.println("Request Date: " + transaction.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            }
            else
            {
                out.println("Requested By: Not yet requested");
                out.println("Request Date: Not yet requested");
            }
            if(transaction.getResolveUser() != null)
            {
                out.println("Resolved By: " + transaction.getResolveUser().getUsername());
                out.println("Resolve Date: " + transaction.getResolveDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            }
            else
            {
                out.println("Resolved By: Not yet resolved");
                out.println("Resolve Date: Not yet resolved");
            }
            out.println("Transaction Status: " + transaction.getTransactionStatus());
            out.println("Transaction Type: " + transaction.getTransactionType());
            out.println("Transaction Amount: $" + transaction.getTransactionAmount() + "0");
        }
        else
            out.println("Transaction was cancelled.");
        out.println(" ");
    }
    
    private static void printAssetInfo(Transaction transaction)
    {
        out.println("------ ASSET INFORMATION ---");
        Asset asset = transaction.getTransactionAsset();
        out.println("Asset Name: " + asset.getAssetName() + " (" + asset.getSymbol() + ")" + "[" + asset.getSector() + "]");
        out.println("Currently Owned: " + asset.isOwned());
        out.println("Number of Shares: " + asset.getVolume());
        out.println("Start Price: $" + asset.getStartPrice() + "0");
        if(asset.getEndPrice() != 0)
        {
            out.println("Asset Final Price: $" + asset.getEndPrice());
            out.println("Returns on Asset: $" + asset.getReturns() + "0");
            out.println("Asset Held for: " + asset.getTimeHeld() + " seconds");
        }
        else
        {
            out.println("Asset Final Price: Asset has not been liquidated");
            out.println("Returns on Asset: Asset has not been liquidated");
            out.println("Asset Held for: Asset has not been liquidated");
        }
        out.println(" ");
    }
    
    private static void printTransactionHistory()
    {
        out.println("--- TRANSACTION HISTORY ---");
        while(!Account.getTransactionHistory().isEmpty())
        {
            Transaction hist_trans = Account.getTransactionHistory().pop();
            out.println("Timestamp: " + hist_trans.getTimestamp());
            out.println("Date Requested: " + hist_trans.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            out.println("Request User: " + hist_trans.getRequestUser().getUsername());
            out.println("Date Resolved: " + hist_trans.getResolveDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            out.println("Resolve User: " + hist_trans.getResolveUser());
            out.println("Transaction Type: " + hist_trans.getTransactionType());
            out.println("Transaction Amount: " + hist_trans.getTransactionAmount());
            if(!(hist_trans.getTransactionAsset() == null))
            {
                out.println("Associated Asset Symbol: " + hist_trans.getTransactionAsset().getSymbol() + "(" + hist_trans.getTransactionAsset().getSector() + ")");
            }
            else
                out.println("Associated Asset Symbol: No associated asset with transaction.");
            out.println(" ");
        }
        out.println("No transactions have been requested/resolved.");
    }
    
    private static void printBalanceHistory(Stack<Balance> accountHistory)
    {
        for(Balance b : accountHistory)
        {
            out.println("Current Value: $" + b.getBalanceAmount() + "0");
            out.println("Timestamp: " + b.getBalanceTimeStamp());
        }
        out.println(" ");
    }
    
    private static void printAccountBalanceHistory()
    {
        out.println("--- ACCOUNT BALANCE HISTORY ---");
        Stack<Balance> accountHistory = Account.getAccountBalance().getBalanceHistory();
        printBalanceHistory(accountHistory);
    }
    
    
    
    private static void printBankBalanceHistory()
    {
        out.println("--- BANK BALANCE HISTORY ---");
        Stack<Balance> bankHistory = Bank.getBankBalance().getBalanceHistory();
        printBalanceHistory(bankHistory);
    }
    
    private static void printPortfolioHistory()
    {
        out.println("--- PORTFOLIO BALANCE HISTORY ---");
        Stack<Balance> portfolioHistory = Portfolio.getPortfolioBalance().getBalanceHistory();
        printBalanceHistory(portfolioHistory);
    }
}