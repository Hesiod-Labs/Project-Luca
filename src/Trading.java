public interface Trading
{
    public void buyOrder(Transaction amount);
    
    public void sellOrder(Transaction amount);
    
    public void shortOrder(Transaction amount);
    
    public void coverOrder(Transaction amount);

}
