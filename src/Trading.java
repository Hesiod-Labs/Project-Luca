public interface Trading
{
    public void buyOrder(double amount);
    
    public void sellOrder(double amount);
    
    public void shortOrder(double amount);
    
    public void coverOrder(double amount);

}
