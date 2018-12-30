import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Asset
{
    private String name;
    private String symbol;
    private String sector;
    private int volume;
    private double orginalPrice;
    private double finalPrice;
    private double returns;
    private ZonedDateTime start;
    private ZonedDateTime finish;
    private ZonedDateTime timeHeld;
    private boolean own;
    private String orderType;
    
    /**
     * Represents the adding of an asset to the Portfolio via buying/shorting
     * @param nameOfAsset Full name of the stock
     * @param sym Stock symbol in accordance with NYSE
     * @param sect Sector to which the stock belongs
     * @param vol Number of shares being bought/shorted
     * @param orgPrice Price at which the asset is first bought/sold
     * @param type Type of order
     */
    public Asset(String nameOfAsset, String sym, String sect, int vol, double orgPrice, String type)
    {
        this.name = nameOfAsset;
        this.symbol = symbol;
        this.sector = sect;
        this.volume = vol;
        this.orginalPrice = orgPrice;
        this.start = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.own = true;
        this.orderType = type;
    }
    
    public enum Sector
    {
        FINANCIALS, UTILITIES, CONSUMERS, ENERGY, HEALTHCARE, INDUSTRIALS, TECHNOLOGY, TELECOM, MATERIALS, REAL_ESTATE
    }
    
    public enum OrderType
    {
        // https://scs.fidelity.com/webxpress/help/topics/learn_order_types_conditions.shtml
        MARKET, LIMIT, STOP_LOSS, STOP_LIMIT, TRAILING_STOP_LOSS, TRAILING_STOP_LIMIT
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getSymbol()
    {
        return symbol;
    }
    
    public String getSector()
    {
        return sector;
    }
    
    public int getVolume()
    {
        return volume;
    }
    
    public double getOrginalPrice()
    {
        return orginalPrice;
    }
    
    public double getFinalPrice()
    {
        return finalPrice;
    }
    
    public double getReturns()
    {
        return returns;
    }
    
    public ZonedDateTime getStart()
    {
        return start;
    }
    
    public ZonedDateTime getFinish()
    {
        return finish;
    }
    
    public ZonedDateTime getTimeHeld()
    {
        return timeHeld;
    }
    
    public String getOrderType()
    {
        return orderType;
    }
}
