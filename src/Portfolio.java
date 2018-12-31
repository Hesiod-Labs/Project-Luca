import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Portfolio
{
    private Set<Asset> portfolio; //TODO Double check to make sure Set is better than ArrayList
    private String nameOfPortFolio;
    private ZonedDateTime timeCreated;
    private String ownerFirst; //TODO necessary to have owner information?
    private String ownerMiddle;
    private String ownerLast;
    private double totalReturns; // TODO necessary to have this?
    private double portfolioValue; // TODO consider making this type Balance
    
    public Portfolio(String name, String first, String middle, String last)
    {
        this.nameOfPortFolio = name;
        this.ownerFirst = first;
        this.ownerMiddle = middle;
        this.ownerLast = last;
        this.timeCreated = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
    
    public void addAsset(Asset asset)
    {
        portfolio.add(asset);
    }
    

}
