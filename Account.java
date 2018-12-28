public class Account
{
    private double totalFunds;
    private double portFolioValue;
    private double withdrawableFunds;
    
    public Account()
    {
        this.totalFunds = portFolioValue + withdrawableFunds;
    }
    
    public void transferToPortfolio(double amount)
    {
        updatePortFolioValue(getPortFolioValue() + amount);
        updateWithdrawableFunds(getWithdrawableFunds() - amount);
    }
    
    public void transferToWithDrawable(double amount)
    {
        updateWithdrawableFunds(getWithdrawableFunds() + amount);
        updatePortFolioValue(getPortFolioValue() - amount);
    }
    
    public double getPortFolioValue()
    {
        return portFolioValue;
    }
    
    public double getTotalFunds()
    {
        return totalFunds;
    }
    
    public double getWithdrawableFunds()
    {
        return withdrawableFunds;
    }
    
    public void updatePortFolioValue(double portFolioValue)
    {
        this.portFolioValue = portFolioValue;
    }
    
    public void updateTotalFunds(double totalFunds)
    {
        this.totalFunds = getWithdrawableFunds() + getPortFolioValue();
    }
    
    public void updateWithdrawableFunds(double withdrawableFunds)
    {
        this.withdrawableFunds = withdrawableFunds;
    }
}
