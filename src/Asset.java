import java.time.*;
import java.time.temporal.ChronoUnit;

/** A portion of the Portfolio to be traded. */
public class Asset
{
    /** Full name of the asset (e.g. Chipotle Mexican Grill). */
    private String assetName;
    
    /** Stock symbol in accordance with NYSE (e.g. CMG). */
    private String symbol;
    
    /** @see Sector enums */
    private Sector sector;
    
    /** Number of shares of the asset. */
    private int volume;
    
    /** Price at which the asset was first acquired. */
    private double originalPrice;
    
    /** Price at which the asset was removed from the Portfolio. */
    private double finalPrice;
    
    /** Difference between finalPrice and originalPrice. */
    private double returns;
    
    /** Date and time at which the asset was first acquired. */
    private ZonedDateTime startDate;
    
    /** Date and time at which the asset was removed from the Portfolio. */
    private ZonedDateTime finishDate;
    
    /** Difference between finishDate and startDate */
    private Period timeHeld;
    
    /** True of the asset is owned and false if not. */
    private boolean own;
    
    /** @see OrderType enums */
    private OrderType orderType;
    
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
        this.assetName = nameOfAsset;
        this.symbol = sym;
        this.sector = Sector.valueOf(sect.toUpperCase());
        this.volume = vol;
        this.originalPrice = orgPrice;
        this.own = true;
        this.orderType = OrderType.valueOf(type.toUpperCase());
    }
    
    public void sellAsset()
    {
        this.setFinishDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        this.setOwn(false);
        this.calculateReturns();
        this.calculateTimeHeld();
        Portfolio.getPortfolio().remove(this);
    }
    
    private double calculateReturns()
    {
        double ret = this.getFinalPrice() - this.getOriginalPrice();
        this.setReturns(ret);
        return ret;
    }
    
    private Period calculateTimeHeld()
    {
        ZonedDateTime start = this.getStartDate();
        ZonedDateTime end = this.getFinishDate();
        Period duration = Period.between(start.toLocalDate(), end.toLocalDate());
        this.setTimeHeld(duration);
        return duration;
    }
    
    /**
     * The different economic sectors to which an asset can belong.
     */
    public enum Sector
    {
        CONSUMERS, ENERGY, FINANCIALS, HEALTHCARE, INDUSTRIALS, MATERIALS, REAL_ESTATE, TECHNOLOGY, TELECOM, UTILITIES
    }
    
    /**
     * https://scs.fidelity.com/webxpress/help/topics/learn_order_types_conditions.shtml for descriptions.
     */
    public enum OrderType
    {
        LIMIT, MARKET, STOP_LIMIT, STOP_LOSS, TRAILING_STOP_LIMIT, TRAILING_STOP_LOSS
    }
    
    //******************************** GETTER METHODS ********************************//
    
    public String getAssetName()
    {
        return assetName;
    }
    
    public String getSymbol()
    {
        return symbol;
    }
    
    public Sector getSector()
    {
        return sector;
    }
    
    public int getVolume()
    {
        return volume;
    }
    
    public double getOriginalPrice()
    {
        return originalPrice;
    }
    
    public double getFinalPrice()
    {
        return finalPrice;
    }
    
    public double getReturns()
    {
        return returns;
    }
    
    public ZonedDateTime getStartDate()
    {
        return startDate;
    }
    
    public ZonedDateTime getFinishDate()
    {
        return finishDate;
    }
    
    public Period getTimeHeld()
    {
        return timeHeld;
    }
    
    public boolean isOwned()
    {
        return own;
    }
    
    public OrderType getOrderType()
    {
        return orderType;
    }
    
    //******************************** SETTER METHODS ********************************//
    
    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
    }
    
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }
    
    public void setSector(Sector sector)
    {
        this.sector = sector;
    }
    
    public void setVolume(int volume)
    {
        this.volume = volume;
    }
    
    public void setOriginalPrice(double originalPrice)
    {
        this.originalPrice = originalPrice;
    }
    
    public void setFinalPrice(double finalPrice)
    {
        this.finalPrice = finalPrice;
    }
    
    public void setReturns(double returns)
    {
        this.returns = returns;
    }
    
    public void setStartDate(ZonedDateTime startDate)
    {
        this.startDate = startDate;
    }
    
    public void setFinishDate(ZonedDateTime finishDate)
    {
        this.finishDate = finishDate;
    }
    
    public void setTimeHeld(Period timeHeld)
    {
        this.timeHeld = timeHeld;
    }
    
    public void setOwn(boolean own)
    {
        this.own = own;
    }
    
    public void setOrderType(OrderType orderType)
    {
        this.orderType = orderType;
    }
}
