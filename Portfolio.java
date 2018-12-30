import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Portfolio
{
    private ArrayList<Asset> portfolio;
    private String nameOfPortFolio;
    private ZonedDateTime timeCreated;
    private String ownerFirst;
    private String ownerMiddle;
    private String ownerLast;
    private double totalReturns;
    private double portfolioValue;
    
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
    
    /*public double sellAsset(Asset asset)
    {
    }*/
}
