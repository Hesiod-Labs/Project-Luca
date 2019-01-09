import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * A stock or option that is owned in the {@link Portfolio}.
 * @author hLabs
 * @since 0.1a
 */
public class Asset
{
    /**
     * Full name of the asset. Example: Chipotle Mexican Grill.
     */
    private String assetName;
    
    /**
     * Stock symbol in accordance with New York Stock Exchange. Example: CMG.
     */
    private String symbol;
    
    /**
     * Sector of the economy to which the asset belongs:
     * <ul>Consumers</ul>
     * <ul>Energy</ul>
     * <ul>Financials</ul>
     * <ul>Healthcare</ul>
     * <ul>Industrials</ul>
     * <ul>Materials</ul>
     * <ul>Real Estate</ul>
     * <ul>Technology</ul>
     * <ul>Telecom</ul>
     * <ul>Utilities</ul>
     */
    private Sector sector;
    
    /**
     * Number of shares of the asset.
     */
    private int volume;
    
    /**
     * Price at which the asset is acquired and added to the {@link Portfolio}.
     */
    private double startPrice;
    
    /**
     * Price at which the asset is removed from the {@link Portfolio}.
     */
    private double endPrice;
    
    /**
     * Difference between {@link #startPrice} and {@link #endPrice}.
     */
    private double returns;
    
    /**
     * Date and time of when the asset is acquired and added to the {@link Portfolio}.
     */
    private ZonedDateTime startDate;
    
    /**
     * Date and time of when the asset is liquidated from the {@link Portfolio}.
     */
    private ZonedDateTime endDate;
    
    /**
     * Difference between {@link #startDate} and {@link #endDate} in seconds.
     */
    private double timeHeld;
    
    /**
     * <code>true</code> if the asset is owned and <code>false</code> if not.
     */
    private boolean own;
    
    /** Different types of asset orders that determine when and at what price to acquire or liquidate an asset:
     * <ul>Limit</ul>
     * <ul>Market</ul>
     * <ul>Stop Limit</ul>
     * <ul>Stop loss</ul>
     * <ul>Trailing Stop Limit</ul>
     * <ul>Trailing Stop Loss</ul>
     */
    private OrderType orderType;
    
    //TODO Created for the purpose of filtering assets while liquidating assets comment getter and setter methods
    private Transaction acquisionTransaction;
    
    //TODO Same as above
    private Transaction liquidationTransaction;
    
    /**
     * Creates an asset to be added to the {@link Portfolio}.
     * @param nameOfAsset Full name of the asset.
     * @param sym Asset symbol in accordance with NYSE.
     * @param sect Sector to which the asset belongs.
     * @param vol Number of shares of the asset.
     * @param orgPrice Price at which the asset is first acquired.
     * @param orderType Determines at what price and when the asset is acquired or liquidated ({@link #orderType}).
     */
    public Asset(String nameOfAsset, String sym, String sect, int vol, double orgPrice, String orderType)
    {
        this.assetName = nameOfAsset;
        this.symbol = sym;
        this.sector = Sector.valueOf(sect.toUpperCase());
        this.volume = vol;
        this.startPrice = orgPrice;
        this.own = false;
        this.orderType = OrderType.valueOf(orderType.toUpperCase());
    }
    
    /**
     * Sells an asset, recording the time in which the sell action is performed, calculates the duration and returns
     * of owning the asset, and finally removes the asset from the {@link Account} portfolio.
     */
    public void sellAsset()
    {
        this.setEndDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        this.setOwn(false);
        this.setReturns(calculateReturns());
        this.calculateTimeHeld();
        Portfolio.getPortfolio().remove(this);
    }
    
    public void coverAsset()
    {
        this.setEndDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        this.setOwn(false);
        this.setReturns(-calculateReturns());
        this.calculateTimeHeld();
        Portfolio.getPortfolio().remove(this);
    }
    
    /**
     * Calculates the returns on an owned asset by subtracting the price at acquisition ({@link #startPrice}) from
     * the price at liquidation({@link #endPrice}). Can also be used to calculate hypothetical returns given any
     * start and finish prices ({@link #calculateHypotheticalReturns}).
     * @return Dollar amount earned or lossed by investing the asset.
     */
    private double calculateReturns()
    {
        double returnsPerShare = this.getEndPrice() - this.getStartPrice();
        return (returnsPerShare*this.getVolume());
    }
    
    /**
     * Calculates the returns of an asset given any acquisition price ({@link #startPrice}) and liquidation price
     * ({@link #endPrice}).
     * @param startPrice Share price of asset at time of acquisition.
     * @param endPrice Share price of asset at time of liquidation.
     * @return Hypothetical returns of owning the asset.
     */
    public double calculateHypotheticalReturns(double startPrice, double endPrice)
    {
        Asset hypothetical = this;
        hypothetical.setStartPrice(startPrice);
        hypothetical.setEndPrice(endPrice);
        return hypothetical.calculateReturns();
    }
    
    /**
     * //TODO Check that ZonedDateTime accounts for Zone offset when calculating
     * Calculates the amount of time between when the asset is acquired and liquidated.
     * @see Portfolio#sellOrder(Transaction)
     * @return The amount of time in seconds.
     */
    private double calculateTimeHeld()
    {
        ZonedDateTime start = this.getStartDate();
        ZonedDateTime end = this.getEndDate();
        double duration = ChronoUnit.SECONDS.between(start, end);
        this.setTimeHeld(duration);
        return duration;
    }
    
    /** The different economic sectors to which an asset can belong. */
    public enum Sector
    {
        CONSUMERS, ENERGY, FINANCIALS, HEALTHCARE, INDUSTRIALS, MATERIALS, REAL_ESTATE, TECHNOLOGY, TELECOM, UTILITIES
    }
    
    /**
     * //TODO https://scs.fidelity.com/webxpress/help/topics/help_definition_t.shtml#trailingstoplimit
     * Different ways in which an asset can be acquired or liquidated. Descriptions originally from Fidelity.com.
     * <ul>Limit: The stock is eligible to be purchased at or below the limit price, but never above it. When a
     * limit order is placed to sell, the stock is eligible to be sold at or above the limit price, but never below it.
     * Although a limit order allows for a specified price limit, it does not guarantee that your order will be
     * executed. </ul>
     * <ul>Market: The stock is eligible to be purchased at the next available price. A market order remains in
     * effect only for the day, and usually results in the prompt purchase or sale of all the shares in question, as
     * long as the security is actively traded and market conditions permit.</ul>
     * <ul>Stop Limit (stock): Automatically becomes a limit order when the stop price is reached. May be filled
     * in whole, in part, or not at all, depending on the number of shares available for sale or purchase at the
     * time.</ul>
     * <ul>Stop Limit (option): a stop limit order to buy automatically becomes a limit order when the bid price is at
     * or above the stop price, or the option trades at or above the stop price. A stop limit order to sell
     * automatically becomes a limit order when the ask price is at or below the stop price, or when the option trades
     * at or below the stop price. The option stop election is based on the exchange's best bid or off (BBO) where the
     * stop order resides.</ul>
     * <ul>Trailing Stop Limit: Order becomes a limit order when the order is triggered.</ul>
     * <ul>Trailing Stop Loss: Order adjusts in price with favorable market movement on the security. To sell, the
     * stop price moves up as the price of the security moves up. To buy, the stop price moves down as the price of
     * security goes down. If the price of the security is moving against the customer's order, the stop price does not
     * adjust.</ul>
     */
    public enum OrderType
    {
        LIMIT, MARKET, STOP_LIMIT, STOP_LOSS, TRAILING_STOP_LIMIT, TRAILING_STOP_LOSS
    }
    
    /**
     * @return Full name of the asset.
     */
    public String getAssetName()
    {
        return assetName;
    }
    
    /**
     * @return Symbol of the asset, per the New York Stock Exchange.
     */
    public String getSymbol()
    {
        return symbol;
    }
    
    /**
     * @return Economic sector to which the asset belongs.
     */
    public Sector getSector()
    {
        return sector;
    }
    
    /**
     * @return Number of shares of the asset.
     */
    public int getVolume()
    {
        return volume;
    }
    
    /**
     * @return Dollar price at which the asset is acquired in the {@link Portfolio}.
     */
    public double getStartPrice()
    {
        return startPrice;
    }
    
    /**
     * @return Dollar price at which the asset is liquidated from the {@link Portfolio}.
     */
    public double getEndPrice()
    {
        return endPrice;
    }
    
    /**
     * @return Dollar amount earned or lossed in trading the asset. Known only after the {@link #startPrice} and
     * {@link #endPrice} are set.
     */
    public double getReturns()
    {
        return returns;
    }
    
    /**
     * @return Date and time in which the asset is added to the {@link Portfolio}.
     */
    public ZonedDateTime getStartDate()
    {
        return startDate;
    }
    
    /**
     * @return Date and time in which the asset is liquidated from the {@link Portfolio}.
     */
    public ZonedDateTime getEndDate()
    {
        return endDate;
    }
    
    /**
     * @return Number of seconds between {@link #startDate} and {@link #endDate}.
     */
    public double getTimeHeld()
    {
        return timeHeld;
    }
    
    /**
     * @return <code>true</code> of the asset exists in the {@link Portfolio} and <code>false</code> if not.
     */
    public boolean isOwned()
    {
        return own;
    }
    
    /**
     * @return The order type that determines when and at what price the asset should be acquired or liquidated.
     */
    public OrderType getOrderType()
    {
        return orderType;
    }
    
    public Transaction getAcquisitionTransaction()
    {
        return acquisionTransaction;
    }
    
    public Transaction getLiquidationTransaction()
    {
        return liquidationTransaction;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT NAME IS INCORRECT.
     * @param assetName Full name of the asset.
     */
    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT SYMBOL IS INCORRECT.
     * @param symbol Asset symbol per the New York Stock Exchange.
     */
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT SECTOR IS INCORRECT.
     * @param sector Economic sector to which the asset belongs.
     */
    public void setSector(Sector sector)
    {
        this.sector = sector;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT VOLUME IS INCORRECT.
     * @param volume Number of shares of an asset.
     */
    public void setVolume(int volume)
    {
        this.volume = volume;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT START PRICE IS INCORRECT.
     * @param startPrice Dollar price at which the asset is acquired.
     */
    public void setStartPrice(double startPrice)
    {
        this.startPrice = startPrice;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT END PRICE IS INCORRECT.
     * @param endPrice Dollar price at which the asset is liquidated.
     */
    public void setEndPrice(double endPrice)
    {
        this.endPrice = endPrice;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE AMOUNT CALCULATED AFTER LIQUIDATION IS INCORRECT.
     * @param returns Dollar amount earned or lossed from trading an asset.
     */
    public void setReturns(double returns)
    {
        this.returns = returns;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT START DATE IS INCORRECT.
     * @param startDate Date and time in which the asset is acquired.
     */
    public void setStartDate(ZonedDateTime startDate)
    {
        this.startDate = startDate;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT END DATE IS INCORRECT.
     * @param endDate Date and time in which the asset is liquidated.
     */
    public void setEndDate(ZonedDateTime endDate)
    {
        this.endDate = endDate;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT END DATE IS INCORRECT.
     * @param timeHeld Duration, in seconds, between the {@link #startDate} and {@link #endDate}.
     */
    public void setTimeHeld(double timeHeld)
    {
        this.timeHeld = timeHeld;
    }
    
    /**
     * @param own <code>true</code> if the asset exists in the {@link Portfolio} and <code>false</code> if not.
     */
    public void setOwn(boolean own)
    {
        this.own = own;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT ORDER TYPE IS INCORRECT.
     * @param orderType Order type of that asset that details when and at what price to acquire or liquidate the asset.
     */
    public void setOrderType(OrderType orderType)
    {
        this.orderType = orderType;
    }
    
    public void setAcquisionTransaction(Transaction acquisionTransaction)
    {
        this.acquisionTransaction = acquisionTransaction;
    }
    
    public void setLiquidationTransaction(Transaction liquidationTransaction)
    {
        this.liquidationTransaction = liquidationTransaction;
    }
}
