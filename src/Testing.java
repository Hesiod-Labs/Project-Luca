import java.time.format.DateTimeFormatter;
import java.util.*;

public class Testing
{
    public static void main(String[] args) throws InterruptedException
    {
        long starTime = System.nanoTime();
        
        /* Create an  account with a bank, portfolio, and storage for users*/
        new Account("Hesiod Account");
        Bank bank = new Bank("Hesiod Bank");
        Portfolio portfolio = new Portfolio("Hesiod Portfolio");
        ArrayList<User> allUsers = new ArrayList<>();
        
        /* Assign the bank, portfolio, and users storage to the Account */
        Account.setAccountBank(bank);
        Account.setAccountPortfolio(portfolio);
        Account.setAccountUsers(allUsers);
        
        /* Create a user and add it to the Account */
        User reqUser = new User("R", "E", "Q", "dog", false, 500);
        User resUser = new User("R", "E", "S", "cat", true, 500);
        resUser.addUser(reqUser);
        resUser.addUser(resUser);
        
        /* Display Account information */
        System.out.println("--- ACCOUNT INFORMATION ---");
        System.out.println("Account: " + Account.getAccountName());
        System.out.println("\t" + "Current Balance: $" + Account.getAccountBalance().getCurrentValue() + "0");
        System.out.println("Bank Name: " + Bank.getBankName());
        System.out.println("\t" + "Current Balance: $" + Bank.getBankBalance().getCurrentValue() + "0");
        System.out.println("Portfolio Name: " + Portfolio.getNameOfPortFolio());
        System.out.println("\t" + "Current Balance: $" + Portfolio.getPortfolioBalance().getCurrentValue() + "0");
        System.out.println("\t" + "Time Created: " + Portfolio.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        System.out.println(" ");
        for(User user : Account.getAccountUsers())
        {
            System.out.println("------ USER INFORMATION ---");
            System.out.println("\t" + "Username: " + user.getUsername());
            System.out.println("\t" + "Password: " + user.getPassword());
            System.out.println("\t" + "Admin: " + user.getPermissionStatus());
            System.out.println("\t" + "Total contributions: $" + user.getUserContribution() + "0");
            System.out.println("\t" + "Time Created: " + user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println(" ");
        }
    
        Thread.sleep(1000);
        
        /* Testing transaction functionality. Request and resolve six different types of transactions */
        System.out.println("--- REQUEST DEPOSIT TRANSACTION ---");
        Transaction t1 = new Transaction(1000, "Deposit");
        reqUser.requestTransaction(t1);
        if(!t1.getTransactionStatus().name().equalsIgnoreCase("Cancelled"))
        {
            System.out.println("Transaction ID: " + t1.getTransactionID());
            System.out.println("Request Date: " + t1.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Transaction Status: " + t1.getTransactionStatus());
            System.out.println("Transaction Type: " + t1.getTransactionType());
            System.out.println("Transaction Amount: $" + t1.getTransactionAmount() + "0");
            System.out.println("Requested By: " + t1.getRequestUser().getUsername());
    
            System.out.println(" ");
    
            Thread.sleep(1000);
    
            System.out.println("--- RESOLVING DEPOSIT TRANSACTION ---");
            resUser.resolveTransaction(t1, "Deposited");
            System.out.println("Transaction Status: " + t1.getTransactionStatus());
            System.out.println("Resolve Date: " + t1.getResolveDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Resolved By: " + t1.getResolveUser().getUsername());
            System.out.println("Updated Bank Balance: $" + Bank.getBankBalance().getCurrentValue() + "0");
            System.out.println(" ");
        }
        else
            System.out.println("Transaction was cancelled from being requested.");
        Thread.sleep(1000);
        
        System.out.println(" --- EXAMPLE REQUEST WITHDRAW TRANSACTION ---");
        Transaction t2 = new Transaction(200, "Withdraw");
        reqUser.requestTransaction(t2);
        if(!t2.getTransactionStatus().name().equalsIgnoreCase("Cancelled"))
        {
            System.out.println("Transaction ID: " + t2.getTransactionID());
            System.out.println("Request Date: " + t2.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Transaction Status: " + t2.getTransactionStatus());
            System.out.println("Transaction Type: " + t2.getTransactionType());
            System.out.println("Transaction Amount: $" + t2.getTransactionAmount() + "0");
            System.out.println("Requested By: " + t2.getRequestUser().getUsername());
            System.out.println(" ");
    
            Thread.sleep(1000);
    
            System.out.println("--- EXAMPLE RESOLVING WITHDRAW TRANSACTION ---");
            resUser.resolveTransaction(t2, "Withdrawn");
            System.out.println("Transaction Status: " + t2.getTransactionStatus());
            System.out.println("Resolve Date: " + t2.getResolveDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Resolved By: " + t2.getResolveUser().getUsername());
            System.out.println("Updated Bank Balance: $" + Bank.getBankBalance().getCurrentValue() + "0");
            System.out.println("Updated User Balance: $" + reqUser.getUserBalance().getCurrentValue() + "0");
            System.out.println(" ");
        }
        else
            System.out.println("Transaction was cancelled from being requested.");
        
        Thread.sleep(1000);
        
        System.out.println("--- EXAMPLE REQUEST BUY TRANSACTION (by reqUser) ---");
        Asset cmg_buy = new Asset("Chipotle", "CMG", "Consumers", 3, 100, "Market");
        Transaction t3 = new Transaction("Buy", cmg_buy);
        reqUser.requestTransaction(t3);
        if(!t3.getTransactionStatus().name().equalsIgnoreCase("Cancelled"))
        {
            System.out.println("Transaction ID: " + t3.getTransactionID());
            System.out.println("Request Date: " + t3.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Transaction Status: " + t3.getTransactionStatus());
            System.out.println("Transaction Type: " + t3.getTransactionType());
            System.out.println("Transaction Amount: $" + t3.getTransactionAmount() + "0");
            System.out.println("Requested By: " + t3.getRequestUser().getUsername());
            System.out.println(" ");
    
            System.out.println("------ ASSET INFORMATION ---");
            Asset asset = t3.getTransactionAsset();
            System.out.println("Asset Name: " + asset.getAssetName() + " (" + asset.getSymbol() + ")");
            System.out.println("Start Price: $" + asset.getStartPrice() + "0");
            System.out.println("Number of Shares: " + asset.getVolume());
            System.out.println(" ");
    
            Thread.sleep(1000);
    
            System.out.println("--- EXAMPLE RESOLVING BUY TRANSACTION ---");
            resUser.resolveTransaction(t3, "Bought");
            //Asset dupA = new Asset("Chipotle", "CMG", "Consumers", 1, 105, "Market");
            //Transaction dupT = new Transaction("Buy", dupA);
            //resUser.requestTransaction(dupT);
            System.out.println("Transaction Status: " + t3.getTransactionStatus());
            System.out.println("Resolve Date: " + t3.getResolveDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Resolved By: " + t3.getResolveUser().getUsername());
            System.out.println("Updated Bank Balance: $" + Bank.getBankBalance().getCurrentValue() + "0");
            System.out.println("Updated Portfolio Balance: $" + Portfolio.getPortfolioBalance().getCurrentValue() + "0");
            System.out.println(" ");
        }
        else
            System.out.println("Transaction was cancelled from being requested.");
        Thread.sleep(1000);
    
        System.out.println("--- EXAMPLE REQUEST SELL TRANSACTION ---");
        Transaction t4 = new Transaction("Sell", cmg_buy);
        reqUser.requestTransaction(t4);
        if(!t4.getTransactionStatus().name().equalsIgnoreCase("Cancelled"))
        {
            System.out.println("Transaction ID: " + t4.getTransactionID());
            System.out.println("Request Date: " + t4.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Transaction Status: " + t4.getTransactionStatus());
            System.out.println("Transaction Type: " + t4.getTransactionType());
            System.out.println("Transaction Amount: $" + t4.getTransactionAmount() + "0");
            System.out.println("Requested By: " + t4.getRequestUser().getUsername());
            System.out.println(" ");
    
            System.out.println("------ ASSET INFORMATION ---");
            Asset asset_sell = t4.getTransactionAsset();
            System.out.println("Asset Name: " + asset_sell.getAssetName() + " (" + asset_sell.getSymbol() + ")");
            System.out.println("Start Price: $" + asset_sell.getStartPrice() + "0");
            System.out.println("Number of Shares: " + asset_sell.getVolume());
            System.out.println(" ");
    
            Thread.sleep(1000);
    
            System.out.println("--- EXAMPLE RESOLVING SELL TRANSACTION ---");
            asset_sell.setEndPrice(asset_sell.getStartPrice() + 10);
            resUser.resolveTransaction(t4, "Sold");
            System.out.println("Transaction Status: " + t4.getTransactionStatus());
            System.out.println("Resolve Date: " + t4.getResolveDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Resolved By: " + t4.getResolveUser().getUsername());
            System.out.println("Asset Initial Price: $" + asset_sell.getStartPrice());
            System.out.println("Asset Final Price: $" + asset_sell.getEndPrice());
            System.out.println("Updated Bank Balance: $" + Bank.getBankBalance().getCurrentValue() + "0");
            System.out.println("Updated Portfolio Balance: $" + Portfolio.getPortfolioBalance().getCurrentValue() + "0");
            System.out.println("Returns on Asset: $" + asset_sell.getReturns() + "0");
            System.out.println("Asset Held for: " + asset_sell.getTimeHeld() + " seconds");
            System.out.println(" ");
        }
        else
            System.out.println("Transaction was cancelled from being requested.");
    
        System.out.println("--- EXAMPLE REQUEST SHORT TRANSACTION ---");
        Asset fb_short = new Asset("Facebook", "FB", "Technology", 3, 100, "Market");
        Transaction t5 = new Transaction("Short", fb_short);
        reqUser.requestTransaction(t5);
        if(!t5.getTransactionStatus().name().equalsIgnoreCase("Cancelled"))
        {
            System.out.println("Transaction ID: " + t5.getTransactionID());
            System.out.println("Request Date: " + t5.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Transaction Status: " + t5.getTransactionStatus());
            System.out.println("Transaction Type: " + t5.getTransactionType());
            System.out.println("Transaction Amount: $" + t5.getTransactionAmount() + "0");
            System.out.println("Requested By: " + t5.getRequestUser().getUsername());
            System.out.println(" ");
        
            System.out.println("------ ASSET INFORMATION ---");
            Asset asset_short = t5.getTransactionAsset();
            System.out.println("Asset Name: " + asset_short.getAssetName() + " (" + asset_short.getSymbol() + ")");
            System.out.println("Start Price: $" + asset_short.getStartPrice() + "0");
            System.out.println("Number of Shares: " + asset_short.getVolume());
            System.out.println(" ");
        
            Thread.sleep(1000);
        
            System.out.println("--- EXAMPLE RESOLVING SHORT TRANSACTION ---");
            resUser.resolveTransaction(t5, "Shorted");
            System.out.println("Transaction Status: " + t5.getTransactionStatus());
            System.out.println("Resolve Date: " + t5.getResolveDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Resolved By: " + t5.getResolveUser().getUsername());
            System.out.println("Updated Bank Balance: $" + Bank.getBankBalance().getCurrentValue() + "0");
            System.out.println("Updated Portfolio Balance: $" + Portfolio.getPortfolioBalance().getCurrentValue() +
                    "0");
            System.out.println(" ");
        }
        else
            System.out.println("Transaction was cancelled from being requested.");
    
        System.out.println("--- EXAMPLE REQUEST COVER TRANSACTION ---");
        Transaction t6 = new Transaction("Cover", fb_short);
        reqUser.requestTransaction(t6);
        if(!t5.getTransactionStatus().name().equalsIgnoreCase("Cancelled"))
        {
            System.out.println("Transaction ID: " + t6.getTransactionID());
            System.out.println("Request Date: " + t6.getRequestDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Transaction Status: " + t6.getTransactionStatus());
            System.out.println("Transaction Type: " + t6.getTransactionType());
            System.out.println("Transaction Amount: $" + t6.getTransactionAmount() + "0");
            System.out.println("Requested By: " + t6.getRequestUser().getUsername());
            System.out.println(" ");
        
            System.out.println("------ ASSET INFORMATION ---");
            Asset asset_short = t6.getTransactionAsset();
            System.out.println("Asset Name: " + asset_short.getAssetName() + " (" + asset_short.getSymbol() + ")");
            System.out.println("Start Price: $" + asset_short.getStartPrice() + "0");
            System.out.println("Number of Shares: " + asset_short.getVolume());
            System.out.println(" ");
        
            Thread.sleep(1000);
        
            System.out.println("--- EXAMPLE RESOLVING COVER TRANSACTION ---");
            asset_short.setEndPrice(asset_short.getStartPrice() + 10);
            resUser.resolveTransaction(t6, "Covered");
            System.out.println("Transaction Status: " + t6.getTransactionStatus());
            System.out.println("Resolve Date: " + t6.getResolveDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            System.out.println("Resolved By: " + t6.getResolveUser().getUsername());
            System.out.println("Asset Initial Price: $" + asset_short.getStartPrice());
            System.out.println("Asset Final Price: $" + asset_short.getEndPrice());
            System.out.println("Updated Bank Balance: $" + Bank.getBankBalance().getCurrentValue() + "0");
            System.out.println("Updated Portfolio Balance: $" + Portfolio.getPortfolioBalance().getCurrentValue() +
                    "0");
            System.out.println("Returns on Asset: $" + asset_short.getReturns() + "0");
            System.out.println("Asset Held for: " + asset_short.getTimeHeld() + " seconds");
            System.out.println(" ");
        }
        else
            System.out.println("Transaction was cancelled from being requested.");
        
        System.out.println("--- TRANSACTION HISTORY ---");
        while(!Transaction.getTransactionHistory().isEmpty())
        {
            Transaction hist_trans = Transaction.getTransactionHistory().pop();
            System.out.println("Transaction ID: " + hist_trans.getTransactionID());
            System.out.println("Transaction Type: " + hist_trans.getTransactionType());
            System.out.println("Transaction Amount: " + hist_trans.getTransactionAmount());
            if(!(hist_trans.getTransactionAsset() == null))
                System.out.println("Associated Asset Symbol: " + hist_trans.getTransactionAsset().getSymbol());
            else
                System.out.println("Associated Asset Symbol: No associated asset with transaction.");
            System.out.println(" ");
        }
        
        System.out.println("--- BANK BALANCE HISTORY ---");
        while(! Bank.getBankBalance().getBalanceHistory().isEmpty())
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
                System.out.println("Associated Transaction ID: No associated transaction" );
            
            System.out.println(" ");
        }
    
        System.out.println("--- PORTFOLIO HISTORY ---");
        while(! Portfolio.getPortfolioBalance().getBalanceHistory().isEmpty())
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
        
        long endTime = System.nanoTime();
        long totalTime = endTime - starTime;
        System.out.println("Total Runtime: " + totalTime / 1000000000.0 + " sec"); // in seconds
    }
}