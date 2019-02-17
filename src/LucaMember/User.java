package LucaMember;

import ABP.*;
import BTA.*;
import LASER.*;

import java.io.IOException;
import java.math.*;
import java.security.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * LucaMember associated with the {@link Account}. Each user has a {@link #username}, which is based on the user's first,
 * middle, and last initials. The user's {@link #password} is randomly generated via {@link java.security}. A user with
 * admin permissions has the ability to resolve requested transactions, change the status of other users, and make
 * changes to the {@link Account}, such as adding ({@link #addUser(User)}) or removing ({@link #removeUser(User)} users.
 * Each user has a {@link #userBalance}, which can be used to keep track of outstanding dues or annual dividends.
 * Each user also has a {@link #userContribution}, which comprises the {@link Account} funds. The date and time in
 * which the user is created is assigned to {@link #timeCreated}.
 * @author hLabs
 * @since 0.1a
 */
public class User //TODO Consider separating non-admin and admin users as two subclasses of LucaMember.
{
    
    /**
     * Initials of first, middle, and last names, plus three numbers randomly generated by
     * {@link #makeUsername(String, String, String)}. Used to log in to the Luca account.
     */
    private String username;
    
    /**
     * A randomly generated password using {@link java.security}. Used to log in to the Luca account.
     * //TODO For now, the password is generated by the user and stored directly as a private field.
     */
    private String password;
    
    /**
     * First name initial of the user; used to generate first letter of the username.
     */
    private String firstInit;
    
    /**
     * Middle name initial of the user; used to generate second letter of the username.
     */
    private String middleInit;
    
    /**
     * Last name initial of the user; used to generate third letter of the username.
     */
    private String lastInit;
    
    /**
     * Date and time in which the user account was created according to the America/New York time zone.
     * Contains year, month, day, hour, minute, second, and time zone information.
     * //TODO might make it system default time zone in the case of users outside of the aforementioned time zone.
     */
    private ZonedDateTime timeCreated;
    
    private UserType clearance;
    
    private UserSector userSector;
    
    /**
     * Total dollar amount a user has added to the {@link Account} funds, with associated timestamps.
     */
    
    private Balance userContribution;
    
    /**
     * Dollar amount a user would have if all funds were to be liquidated and withdrawn from the account.
     * Unlike contribution, the {@link Balance} of a user is dependent upon the current value of the {@link Portfolio},
     * the amount the user has contributed in relation to the total amount contributed by all users, and if the user
     * is considered a general partner, a limited partner, or an investor.
     * // TODO Decide if this will represent the current value of investment (i.e. as it is) or something else
     */
    
    private Balance userBalance;
    
    private PublicKey userPublicKey;
    
    private PrivateKey userPrivateKey;
    
    private String runTimeHash;
    
    /**
     * Creates a user associated with the {@link Account}.
     * @param first Initial of the first name of the user (not case-sensitive).
     * @param middleInit Initial of the middle name of the user (not case-sensitive).
     * @param last Last name of the user (not case-sensitive).
     * @param password Randomly generated; used for logging in to the {@link Account}.
     * @param contribution Dollar amount given by the user for investing.
     */
    public User(String first, String middleInit, String last, String password, String w1, String w2, String w3,
                UserType role, double contribution)
    {
        this.firstInit = first.toUpperCase();
        this.middleInit = middleInit.toUpperCase();
        this.lastInit = last.toUpperCase();
        this.username = makeUsername(first.toUpperCase(), middleInit.toUpperCase(), last.toUpperCase());
        this.password = password;
        this.runTimeHash = Encryption.applySHA256(w1 + w2 + w3);
        KeyPair kp = Encryption.generateKeyPair();
        this.userPrivateKey = kp.getPrivate();
        this.userPublicKey = kp.getPublic();
        this.clearance = role;
        this.timeCreated = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        this.userContribution = new Balance(contribution); // TODO lines 92 and 93
        userContribution.updateBalance(userContribution);
        Account.getAccountUsers().add(this);
        //this.userBalance = new Balance(); //TODO Should contributions be a part of the userBalance?
        Account.getAccountBalance().updateBalance( new Balance(contribution));
        Bank.getBankBalance().updateBalance(new Balance(contribution));
    }
    
    /**
     * Creates a username for the user that is the initials of first, middle, and last names, and three randomly
     * generated numbers.
     * @see Random
     * @param first Initial of the first name of the user.
     * @param middle Initial of the middle name of the user.
     * @param last Initial of the last name of the user.
     * @return Username (e.g. ABC123)
     */
    public String makeUsername(String first, String middle, String last)
    {
        String[] userArray = new String[6]; // Array to hold elements to create username.
    
        userArray[0] = first;
        userArray[1] = middle;
        userArray[2] = last;
        //userArray[3] = "1";
        //userArray[4] = "2";
        //userArray[5] = "3";
        
        Random randomGen = new Random(); // To generate the three random numbers following the user initials.
    
        // Generates three random numbers and adds them to userArray. Numbers generated are between 0 and 9.
        IntStream.range(3, 6).forEach(i -> {
            int num = randomGen.nextInt(9);
            userArray[i] = Integer.toString(num);
        });
        
    
        // To concatenate the initials and numbers.
        StringBuilder nameBuilder = new StringBuilder();
        String name = null;
    
        // Create the username and assign it to the user.
        for(String s : userArray)
        {
            nameBuilder.append(s);
            name = nameBuilder.toString();
        }
        return name;
    }
    
    //TODO Add JavaDoc
    public double calculatePctHoldings()
    {
        MathContext mc = new MathContext(6, RoundingMode.HALF_UP);
        double thisUserContributed = this.getUserContribution().getCurrentValue();
        double accountContributions = 0;
        for(User user : Account.getAccountUsers())
        {
            double eachUserContrib = user.getUserContribution().getCurrentValue();
            accountContributions += eachUserContrib;
            
        }
        
        return (thisUserContributed/accountContributions)*100;
    }
    
    /**
     * //TODO Add conditions such that 0.9996 rounds to 1.000 and 0.0004 rounds to 0.000
     * @param num Number to be rounded to three decimal places.
     * @return
     */
    public double roundToThree(double num)
    {
        String numString = Double.toString(num);
       
        int decimalPlace = numString.indexOf('.');
        System.out.println(decimalPlace);

        String roundedNumString = numString.substring(0, decimalPlace + 4);
        System.out.println(roundedNumString);
        return  Double.parseDouble(roundedNumString);
    }
    
    //TODO Add JavaDoc
    public double calculateHoldingsValue()
    {
        //TODO For now, assumes net zero gain from all assets owned
        // TODO Once real-time stock quotes are available use that price
        return (Account.getAccountBalance().getCurrentValue())*(this.calculatePctHoldings()/100);
    }
    
    /**
     * Request a {@link Transaction}. All users can request transactions, which are stored in a Linked List in the
     * {@link Transaction} class. If a user is an admin, the request is immediately resolved. Otherwise, the request
     * is added to the transaction request list.
     * @param request Requested transaction.
     */
    public void requestTransaction(Transaction request) throws IOException
    {
        // Confirms the action to request a transaction during runtime.
        if(request.confirmAction())
        {
            request.setRequestDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
            request.setTimestamp(request.getRequestDate().toEpochSecond());
            request.setUserPublicKey(this.getUserPublicKey());
            request.setUserPrivateKey(this.getUserPrivateKey());
            request.setRequestUser(this);
            request.setTransactionData(request.getTransactionType().toString() + " " +
                    request.getRequestDate().toString());
            request.setSignature(Encryption.applySignature(request.getRequestUser().getUserPrivateKey(),
                    request.getTransactionData()));
            
            // If the user is an admin, resolve the transaction immediately.
            if(request.getRequestUser().getClearance() >= 2)
            {
                resolveTransaction(request, request.getMatchingStatus().toString());
            }
            else
            {
                // Otherwise, add the request to the list of requests.
                request.addTransactionRequest();
            }
            if(Laser.getBlockchain().size() == 0) {
                Laser.createGenesisBlock();
                Laser.initializeFile(Laser.transactionHistory);
            }
            if(!Laser.validateTransaction(request))
                request.setTransactionStatus(Transaction.Status.CANCELLED);
        }
        else
            // If the user declines confirmation, the transaction request is cancelled.
            request.setTransactionStatus(Transaction.Status.CANCELLED);
    }
    
    /**
     * Resolves a requested {@link Transaction}. Only an admin {@link User} can resolve requests.
     * @param transaction Requested {@link Transaction} to be resolved.
     * @param updatedStatus Status after the {@link Transaction} is resolved.
     */
    public void resolveTransaction(Transaction transaction, String updatedStatus)
    {
        // If the user is an admin.
        if(this.getClearance() >= 2)
        {
            // Confirmation that the admin wants to resolve the transaction.
            if(transaction.confirmAction())
            {
                Transaction.Status newStatus = Transaction.Status.valueOf(updatedStatus.toUpperCase());
                switch(newStatus)
                {
                    case DENIED:    transaction.setTransactionStatus(Transaction.Status.DENIED);
                        break;
                    case DEPOSITED: Bank.deposit(transaction);
                        break;
                    case WITHDRAWN: Bank.withdraw(transaction);
                        break;
                    case BOUGHT:    Portfolio.buyOrder(transaction);
                        break;
                    case SOLD:      Portfolio.sellOrder(transaction);
                        break;
                    case SHORTED:   Portfolio.shortOrder(transaction);
                        break;
                    case COVERED:   Portfolio.coverOrder(transaction);
                        break;
                        default:
                            System.out.println("Please be sure your updated status matches one of the following: " +
                                    "\n\t" + "DEPOSITED" + "\n\t" + "WITHDRAWN" + "\n\t" + "BOUGHT" + "\n\t" + "SOLD" +
                                    "\n\t" + "SHORTED" + "\n\t" + "COVERED");
                            //TODO Have recursive call, but right now, would give infinite loop -- requires new user input
                            break;
                }
                transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(
                        ChronoUnit.SECONDS));
                transaction.setResolveUser(this);
                transaction.removeTransactionRequest();
                transaction.addToTransactionHistory();
            }
            else
                System.out.println("Transaction resolution was cancelled, but remains requested.");
        }
        else
            System.out.println("You do not have permission to resolve transactions.");
    }
    
    /**
     * Allows a user to contribute more towards investing.
     * @param amount Dollar amount to add to the {@link Account}.
     */
    public void contribute(double amount) throws IOException
    {
        this.userContribution.updateBalance(new Balance(amount));
        this.requestTransaction(new Transaction("Deposit", amount));
    }
    
    /**
     * //TODO Keep System.out.Println statements?
     * Adds a user to the {@link Account}. Only available to admin users.
     * @param newUser LucaMember to be added.
     */
    public void addUser(User newUser)
    {
        // If the user is an admin.
        if(this.getClearance() >= 3)
        {
            // If the account does not already contain the user.
            if(!Account.getAccountUsers().contains(newUser))
            {
                Account.getAccountUsers().add(newUser);
                // Confirmation that the user was added.
                if(!Account.getAccountName().isEmpty())
                    System.out.println("LucaMember successfully added to " + Account.getAccountName() + ".");
                else
                    System.out.println("LucaMember successfully added to the account.");
            }
            // Otherwise, the user already exists in the account and does not need to be added.
            else
            {
                if(!Account.getAccountName().isEmpty())
                    System.out.println("LucaMember already exists in " + Account.getAccountName() + ".");
                else
                    System.out.println("LucaMember already exists in the account.");
            }
        }
        // Otherwise, the user is not an admin and does not have permission to add users.
        else
            System.out.println("You do not have admin permission to add a user to the account.");
    }
    
    /**
     * Removes a user from the account. Available only to admin users.
     * //TODO Need to add functionality to update account, bank, and portfolio balances
     * //TODO Consider try/catch statements. If user has funds in portfolio, must deal with those assets first before removal
     * //TODO Also, if the removal of the user leads to a negative bank balance, then the user cannot be removed.
     * //TODO This is also contingent upon whether the user has made any contributions to the account.
     * //TODO These things also apply to addUser.
     * @param userToRemove LucaMember to be removed.
     */
    public void removeUser(User userToRemove)
    {
        // If the user is either an SYSTEM_ADMIN or OFFICER
        if(this.getClearance() >= 3)
        {
            // If the user to remove exists within the account.
            if(Account.getAccountUsers().contains(userToRemove))
            {
                // Confirmation that the user is removed from the account.
                Account.getAccountUsers().remove(userToRemove);
                System.out.println("LucaMember successfully removed from " + Account.getAccountName() + ".");
                System.out.println("LucaMember successfully removed from the account.");
            }
            else
            {
                // Otherwise, the user does not exist in the account, so cannot be removed.
                if(!Account.getAccountName().isEmpty())
                    System.out.println("LucaMember does not exist in " + Account.getAccountName() + ".");
                else
                    System.out.println("LucaMember does not exist in the account.");
            }
        }
        // Otherwise, the user is not an admin so does not have permission to remove the user.
        else
            System.out.println("You do not have admin permission to add a user to the account.");
    }
    
    /**
     * // TODO Add functionality if user is not in the account
     * // TODO Should only be available to admin users.
     * Allows a user's status to be changed to either regular or admin.
     * @param otherUser The user whose clearance is to be changed.
     * @param newType The new clearance level to be assigned to the user.
     */
    public void changeClearance(User otherUser, UserType newType)
    {
        if(this.getClearance() >= otherUser.getClearance())
            otherUser.setClearance(newType);
        else
        {
            System.out.println("Your status, " + this.getClearance() +
                    ", is insufficient to change clearance to " + newType);
        }
    }
    
    public enum UserSector
    {
        CRYPTO, CS_IT, FOREX, HEALTHCARE, MEI, OPTIONS, REIT_FIG
    }
    
    public enum UserType
    {
        SYSTEM_ADMIN (4), OFFICER (3), SECTOR_HEAD (2), GENERAL_USER (1);

        private int rank;
        UserType(int rank)
        {
            this.rank = rank;
        }
    }
    
    /**
      * @return Username of the user. Used during login.
     */
    public String getUsername()
    {
        return username;
    }
    
    /**
     * @return Password of the user. Used during login.
     */
    public String getPassword()
    {
        return password;
    }
    
    /**
     * @return First name initial of the user.
     */
    public String getFirstInit()
    {
        return firstInit;
    }
    
    /**
     * @return Middle name initial of the user.
     */
    public String getMiddleInit()
    {
        return middleInit;
    }
    
    /**
     * @return Last name initial of the user.
     */
    public String getLastInit()
    {
        return lastInit;
    }
    
    /**
     * @return Date and time in which the user was created in Luca. Timezone set to America/New York.
     * @see ZonedDateTime
     */
    public ZonedDateTime getTimeCreated()
    {
        return timeCreated;
    }
    
    public PrivateKey getUserPrivateKey()
    {
        return userPrivateKey;
    }
    
    public PublicKey getUserPublicKey()
    {
        return userPublicKey;
    }
    
    public int getClearance()
    {
        return this.clearance.rank;
    }
    
    /**
     * @return Balance statements that detail how much the user has added to the funds for investing.
     */
    public Balance getUserContribution()
    {
        return userContribution;
    }
    
    /**
     * @return Current and past balance statements of the user. Top balance statement is the most recent.
     */
    public Balance getUserBalance()
    {
        return userBalance;
    }
    
    public UserType getUserType()
    {
        return this.clearance;
    }
    
    /**
     * Sets the first name initial of the user.
     * @param init First name initial.
     */
    public void setFirstInit(String init)
    {
        this.firstInit = init;
    }
    
    /**
     * Sets the middle name initial of the user.
     * @param init Middle name initial.
     */
    public void setMiddleInit(String init)
    {
        this.middleInit = init;
    }
    
    /**
     * Sets the last name initial of the user.
     * @param init Last name initial.
     */
    public void setLastInit(String init)
    {
        this.lastInit = init;
    }
    
    /**
     * // TODO add functionality to change username and password.
     * Sets the username of the user.
     * WARNING: DO NOT USE UNLESS THE USERNAME IS EITHER NOT ALREADY SET OR WANT TO BE SOMETHING UNRELATED TO THE USER'S
     * INITIALS.
     * @param username Used to log in to Luca.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    /**
     * Sets the password of the user.
     * @param password Used to log in to Luca.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    /**
     * Date and time in which the user was created in Luca. Timezone set to America/New York.
     * WARNING: DO NOT USE UNLESS THE DATE AND TIME HAVE NOT ALREADY BEEN SET OR ARE INCORRECT.
     * @param timeCreated Date and time of creation.
     */
    public void setTimeCreated(ZonedDateTime timeCreated)
    {
        this.timeCreated = timeCreated;
    }
    
    /**
     * Sets the dollar amount a user has contributed to the funds.
     * WARNING: DO NOT USE UNLESS THE CONTRIBUTION BALANCE HAS NOT ALREADY BEEN SET. OTHERWISE, PAST CONTRIBUTIONS
     * INFORMATION WILL BE LOSSED.
     * @param userContribution Dollar amount contributed to the {@link Account} funds.
     */
    public void setUserContribution(double userContribution)
    {
        this.userContribution = new Balance(userContribution);
    }
    
    /**
     * // TODO Still need to decide what the user Balance will represent.
     * Sets balance of the user.
     * @param userBalance Contains current and past balance statements with associated amounts, timestamps, and
     *                    transactions.
     */
    
    public void setUserBalance(Balance userBalance)
    {
        this.userBalance = userBalance;
    }
    
    public void setClearance(UserType newType)
    {
        this.clearance = newType;
    }
    
    public void setUserPrivateKey(PrivateKey userPrivateKey)
    {
        this.userPrivateKey = userPrivateKey;
    }
    
    public void setUserPublicKey(PublicKey userPublicKey)
    {
        this.userPublicKey = userPublicKey;
    }

    public String getRunTimeHash() { return this.runTimeHash; }
}
