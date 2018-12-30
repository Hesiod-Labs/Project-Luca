import java.time.*;

public class Trading extends Transaction
{
    private double volume;
    private double pricePerAsset;
    private double totalCost;
    private String symbol;
    private double priceTrigger; // TODO
    private LocalDateTime dateTrigger; // TODO
    
    public Trading(String id, String symbol, double pricePerAsset, double volume, String type)
    {
        super(id, pricePerAsset, type);
        totalCost = pricePerAsset*volume;
        setAmount(totalCost);
        this.symbol = symbol;
        this.pricePerAsset = pricePerAsset;
        this.volume = volume;
    }
    
    public void approve()
    {
        switch(getType())
        {
            case "BUY" :
            {
                setExeDate(LocalDateTime.now(ZoneId.of("America/New_York")));
                setStatus("BOUGHT");
                updateWithdrawableFunds(getWithdrawableFunds() - getAmount());
                updatePortFolioValue(getPortFolioValue() + getAmount());
                break;
            }
            
            case "SELL" : // TODO Fix funds updating since price sold will be different than bought
            {
                setExeDate(LocalDateTime.now(ZoneId.of("America/New_York")));
                setStatus("SOLD");
                updateWithdrawableFunds(getWithdrawableFunds() + getAmount());
                updatePortFolioValue(getPortFolioValue() - getAmount());
                break;
            }
    
            case "SHORT":
            {
                setExeDate(LocalDateTime.now(ZoneId.of("America/New_York")));
                setStatus("SHORTED");
                updatePortFolioValue(getPortFolioValue() + getAmount());
                break;
            }
    
            case "COVER": // TODO Fix funds updating since price shorted will be different than covered
            {
                setExeDate(LocalDateTime.now(ZoneId.of("America/New_York")));
                setStatus("COVERED");
                updateWithdrawableFunds(getWithdrawableFunds() - getAmount());
                updatePortFolioValue(getPortFolioValue() + getAmount());
                break;
            }
        }
    }
    
    public void decline()
    {
        setExeDate(LocalDateTime.now(ZoneId.of("America/New_York")));
        setStatus("DECLINED");
    }
}
