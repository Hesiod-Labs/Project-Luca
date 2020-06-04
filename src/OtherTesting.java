import ABP.*;
import BTA.*;


public class OtherTesting
{
    public static void main(String[] args) throws InterruptedException
    {
        new Account("Hesiod Financial", "Hesiod Bank", "Hesiod Portfolio");
        Bank.deposit(new Transaction(Transaction.Type.DEPOSIT, 1000));
        System.out.println(Portfolio.getPortfolioReturns().getCurrentValue());
        System.out.println(Portfolio.getNameOfPortFolio());
        
        //Asset a = new Asset("apple", "AAPL", Asset.Sector.CONSUMER_HEALTHCARE, 3, 100, Asset.OrderType.MARKET);
        //Transaction buy = new Transaction(Transaction.Type.BUY, a);
    
        //Portfolio.buyOrder(buy);
        
        //Thread.sleep(10000);
        
        //a.setEndPrice(110);
        //Portfolio.sellOrder(buy);
    
        //System.out.println(a.getReturns());
        //System.out.println(a.getTimeHeld());
        
        Asset s = new Asset("short", "SHORT", Asset.Sector.FOREX, 2, 200, Asset.OrderType.MARKET);
        Transaction st = new Transaction(Transaction.Type.SHORT, s);
        Portfolio.shortOrder(st);
    }
}
