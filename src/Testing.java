import java.util.ArrayList;

public class Testing
{
    public static void main(String[] args) throws InterruptedException
    {
        long starTime = System.nanoTime();
        
        /* Create an  Account with a Bank, Portfolio, and storage for Users*/
        new Account("Hesiod Account");
        Bank bank = new Bank("Hesiod Bank");
        Portfolio portfolio = new Portfolio("Hesiod Portfolio");
        ArrayList<User> allUsers = new ArrayList<>();
        
        /* Assign the Bank, Portfolio, and Users storage to the Account */
        Account.setBank(bank);
        Account.setPortfolio(portfolio);
        Account.setAllUsers(allUsers);
        
        /* Create a user and add it to the Account */
        User reqUser = new User("John", "A", "Smith", "dog", true, 500);
        User resUser = new User("Joe", "B", "Doe", "cat", true, 500);
        Account.addUser(reqUser);
        Account.addUser(resUser);
        
        /* Display Account information */
        System.out.println("--- ACCOUNT INFORMATION ---");
        System.out.println("Account: " + Account.getAccountName());
        System.out.println("\t" + "Current Balance: " + Account.getAccountBalance().getCurrentBalance());
        System.out.println("Bank Name: " + Bank.getBankName());
        System.out.println("\t" + "Current Balance: " + Bank.getBankBalance().getCurrentBalance());
        System.out.println("Portfolio Name: " + Portfolio.getNameOfPortFolio());
        System.out.println("\t" + "Current Balance: " + Portfolio.getPortfolioBalance().getCurrentBalance());
        System.out.println("\t" + "Time Created: " + Portfolio.formatTime(Portfolio.getTimeCreated()));
    
        Thread.sleep(10000);
        
        /* Testing transaction functionality. Request and resolve four different types of transactions */
        System.out.println("--- EXAMPLE REQUEST DEPOSIT TRANSACTION ---");
        Transaction t1 = new Transaction(reqUser, 100, "Deposit");
        reqUser.requestTransaction(t1);
        System.out.println("Transaction ID: " + t1.getTransactionID());
        System.out.println("Request Date: " + t1.formatTime(t1.getRequestDate()));
        System.out.println("Transaction Status: " + t1.getTransactionStatus());
        System.out.println("Transaction Type: " + t1.getTransactionType());
        System.out.println("Transaction Amount: $" + t1.getTransactionAmount() + "0");
        System.out.println("Requested By: " + t1.getRequestUser().formatName());
        System.out.println(" ");
    
        Thread.sleep(10000);
        
        System.out.println("--- EXAMPLE RESOLVING DEPOSIT TRANSACTION ---");
        resUser.resolveTransaction(t1, "Deposited");
        System.out.println("Transaction Status: " + t1.getTransactionStatus());
        System.out.println("Resolve Date: " + t1.formatTime(t1.getResolveDate()));
        System.out.println("Resolved By: " + t1.getResolveUser().formatName());
        System.out.println("Updated Bank Balance: $" + Bank.getBankBalance().getCurrentBalance() + "0");
        System.out.println(" ");
    
        Thread.sleep(10000);
        
        System.out.println(" --- EXAMPLE REQUEST WITHDRAW TRANSACTION ---");
        Transaction t2 = new Transaction(reqUser, 200, "Withdraw");
        reqUser.requestTransaction(t2);
        System.out.println("Transaction ID: " + t2.getTransactionID());
        System.out.println("Request Date: " + t2.formatTime(t2.getRequestDate()));
        System.out.println("Transaction Status: " + t2.getTransactionStatus());
        System.out.println("Transaction Type: " + t2.getTransactionType());
        System.out.println("Transaction Amount: $" + t2.getTransactionAmount() + "0");
        System.out.println("Requested By: " + t2.getRequestUser().formatName());
        System.out.println(" ");
    
        Thread.sleep(10000);
        
        System.out.println("--- EXAMPLE RESOLVING WITHDRAW TRANSACTION ---");
        resUser.resolveTransaction(t2, "Withdrawn");
        System.out.println("Transaction Status: " + t2.getTransactionStatus());
        System.out.println("Resolve Date: " + t2.formatTime(t2.getResolveDate()));
        System.out.println("Resolved By: " + t2.getResolveUser().formatName());
        System.out.println("Updated Bank Balance: $" + Bank.getBankBalance().getCurrentBalance() + "0");
        System.out.println("Updated User Balance: $" + reqUser.getUserBalance().getCurrentBalance() + "0");
        System.out.println(" ");
        
        Thread.sleep(10000);
        
        System.out.println("--- EXAMPLE REQUEST BUY TRANSACTION ---");
        Asset cmg_buy = new Asset("Chipotle", "CMG", "Consumers", 3, 100, "Market");
        Transaction t3 = new Transaction(reqUser, "Buy", cmg_buy);
        System.out.println("Transaction ID: " + t3.getTransactionID());
        System.out.println("Request Date: " + t3.formatTime(t3.getRequestDate()));
        System.out.println("Transaction Status: " + t3.getTransactionStatus());
        System.out.println("Transaction Type: " + t3.getTransactionType());
        System.out.println("Transaction Amount: $" + t3.getTransactionAmount() + "0");
        System.out.println("Requested By: " + t3.getRequestUser().formatName());
        System.out.println(" ");
        System.out.println("------ ASSET INFORMATION ---");
        Asset asset = t3.getTransactionAsset();
        System.out.println("Asset Name: " + asset.getAssetName() + " (" + asset.getSymbol() + ")");
        System.out.println("Start Price: $" + asset.getOriginalPrice() + "0");
        System.out.println("Number of Shares: " + asset.getVolume());
        System.out.println(" ");
        
        Thread.sleep(10000);
    
        System.out.println("--- EXAMPLE RESOLVING BUY TRANSACTION ---");
        resUser.resolveTransaction(t3, "Bought");
        System.out.println("Transaction Status: " + t3.getTransactionStatus());
        System.out.println("Resolve Date: " + t3.formatTime(t3.getResolveDate()));
        System.out.println("Resolved By: " + t3.getResolveUser().formatName());
        System.out.println("Updated Bank Balance: $" + Bank.getBankBalance().getCurrentBalance() + "0");
        System.out.println("Updated Portfolio Balance: $" + Portfolio.getPortfolioBalance().getCurrentBalance() + "0");
        System.out.println(" ");
    
        Thread.sleep(10000);
    
        System.out.println("--- EXAMPLE REQUEST SELL TRANSACTION ---");
        Transaction t4 = new Transaction(reqUser, "Sell", cmg_buy);
        System.out.println("Transaction ID: " + t4.getTransactionID());
        System.out.println("Request Date: " + t4.formatTime(t4.getRequestDate()));
        System.out.println("Transaction Status: " + t4.getTransactionStatus());
        System.out.println("Transaction Type: " + t4.getTransactionType());
        System.out.println("Transaction Amount: $" + t4.getTransactionAmount() + "0");
        System.out.println("Requested By: " + t4.getRequestUser().formatName());
        System.out.println(" ");
        System.out.println("------ ASSET INFORMATION ---");
        Asset asset_sell = t4.getTransactionAsset();
        System.out.println("Asset Name: " + asset_sell.getAssetName() + " (" + asset_sell.getSymbol() + ")");
        System.out.println("Start Price: $" + asset_sell.getOriginalPrice() + "0");
        System.out.println("Number of Shares: " + asset_sell.getVolume());
        System.out.println(" ");
    
        Thread.sleep(10000);
    
        System.out.println("--- EXAMPLE RESOLVING SELL TRANSACTION ---");
        resUser.resolveTransaction(t4, "Sold");
        asset_sell.setFinalPrice(asset_sell.getOriginalPrice() + 10); //TODO Check final price
        System.out.println("Transaction Status: " + t4.getTransactionStatus());
        System.out.println("Resolve Date: " + t4.formatTime(t4.getResolveDate()));
        System.out.println("Resolved By: " + t4.getResolveUser().formatName());
        System.out.println("Updated Bank Balance: $" + Bank.getBankBalance().getCurrentBalance() + "0"); //TODO Should be $-100
        System.out.println("Updated Portfolio Balance: $" + Portfolio.getPortfolioBalance().getCurrentBalance() + "0");
        System.out.println("Returns on Asset: $" + asset_sell.getReturns() + "0"); //TODO Should be +$30
        //System.out.println("Asset Held for: " + asset_sell.getTimeHeld());
        System.out.println(" ");
        
        /*
        while(!Bank.getBankBalance().getBalanceHistory().isEmpty())
        {
            Balance historical = Bank.getBankBalance().getBalanceHistory().pop();
            double amount = historical.getBalanceAmount();
            System.out.println("Bank balance: " + amount);
        }
        System.out.println("Portfolio: " + Portfolio.getPortfolioBalance().getCurrentBalance());
        */

    /*
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
    */
    /*
        System.out.println("--- BANK BALANCE HISTORY ---");
        while(! Bank.getBankBalance().getBalanceHistory().isEmpty()) {
            Balance hist_bal = Bank.getBankBalance().getBalanceHistory().pop();
            if(!(hist_bal.getAssociatedTransaction() == null))
                System.out.println("Associated Transaction ID: " + hist_bal.getAssociatedTransaction().getTransactionID());
            else
                System.out.println("Associated Transaction ID: No associated transaction" );
            System.out.println("Balance: " + hist_bal.getBalanceAmount());
            System.out.println(" ");
        }
    
        System.out.println("--- PORTFOLIO HISTORY ---");
        while(! Portfolio.getPortfolioBalance().getBalanceHistory().isEmpty()) {
            Balance port_hist = Portfolio.getPortfolioBalance().getBalanceHistory().pop();
            System.out.println("Associated Transaction: " + port_hist.getAssociatedTransaction().getTransactionID());
            System.out.println("Balance: " + port_hist.getBalanceAmount());
            System.out.println(" ");
        }
        */
        long endTime = System.nanoTime();
        long totalTime = endTime - starTime;
        //System.out.println("Runtime: " + totalTime / 1000000000.0); // in seconds
    }
}
