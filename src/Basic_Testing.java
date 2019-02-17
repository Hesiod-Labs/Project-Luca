import ABP.*;
import BTA.*;
import LucaMember.User;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class Basic_Testing
{
    public static void main(String[] args) throws InterruptedException
    {
        
        /* Create an  account with a bank, portfolio, and storage for users*/
        new Account("Hesiod Account", "Hesiod Bank", "Hesiod Portfolio");
        User admin = new User("A", "D", "M", "password", "alpha", "beta", "chi",
                User.UserType.SYSTEM_ADMIN, 10);
        User reqUser = new User("A", "B", "C", "dog", "a", "b", "c", User.UserType.GENERAL_USER, 500);
        User resUser = new User("D", "E", "F", "cat", "d", "e", "f", User.UserType.OFFICER, 500);
    
        printAccountInfo();
        
        printUsersInfo();
        
        Transaction t1 = new Transaction("Deposit", 1000);
        printTransactionReceipt(t1);
        reqUser.requestTransaction(t1);
        printTransactionReceipt(t1);
        resUser.resolveTransaction(t1, "Deposited");
        printTransactionReceipt(t1);
        
        Transaction t2 = new Transaction("Withdraw", 200);
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
        
        fb_short.setEndPrice(fb_short.getStartPrice() + 10);
        Transaction t6 = new Transaction("Cover", fb_short);
        printTransactionReceipt(t6);
        printAssetInfo(t6);
        reqUser.requestTransaction(t6);
        printTransactionReceipt(t6);
        resUser.resolveTransaction(t6, "Covered");
        printTransactionReceipt(t6);
        printAssetInfo(t6);
        
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
