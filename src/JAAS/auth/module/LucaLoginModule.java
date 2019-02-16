package JAAS.auth.module;

import LucaMember.User;
import auth.principal.LucaPrincipal;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.LoginModule;
import java.util.Map;

/**
 * <p> This auth LoginModule authenticates users with a password.
 *
 * <p> This LoginModule only recognizes one user:       testUser
 * <p> testUser's password is:  testPassword
 *
 * <p> If testUser successfully authenticates itself,
 * a <code>SamplePrincipal</code> with the testUser's user name
 * is added to the Subject.
 *
 * <p> This LoginModule recognizes the debug option.
 * If set to true in the login Configuration,
 * debug messages will be output to the output stream, System.out.
 */
public class LucaLoginModule implements LoginModule
{
    
    // initial state
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map sharedState;
    private Map options;
    
    // configurable option
    private boolean debug = false;
    
    // the authentication status
    private boolean succeeded = false;
    private boolean commitSucceeded = false;
    
    // username and password
    private String username;
    private char[] password;
    
    // testUser's SamplePrincipal
    private LucaPrincipal userPrincipal;
    
    /**
     * Initialize this <code>LoginModule</code>.
     *
     * @param subject         the <code>Subject</code> to be authenticated. <p>
     * @param callbackHandler a <code>CallbackHandler</code> for communicating
     *                        with the end user (prompting for user names and
     *                        passwords, for example). <p>
     * @param sharedState     shared <code>LoginModule</code> state. <p>
     * @param options         options specified in the login
     *                        <code>Configuration</code> for this particular
     *                        <code>LoginModule</code>.
     */
    public void initialize(Subject subject,
                           CallbackHandler callbackHandler,
                           Map<java.lang.String, ?> sharedState,
                           Map<java.lang.String, ?> options)
    {
        
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
        
        // initialize any configured options
        debug = "true".equalsIgnoreCase((String) options.get("debug"));
    }
    
    /**
     * Authenticate the user by prompting for a user name and password.
     *
     * @return true in all cases since this <code>LoginModule</code>
     * should not be ignored.
     * @throws FailedLoginException if the authentication fails. <p>
     * @throws LoginException       if this <code>LoginModule</code>
     *                              is unable to perform the authentication.
     */
    public boolean login() throws LoginException
    {
        
        // prompt for a user name and password
        if(callbackHandler == null)
            throw new LoginException("Error: no CallbackHandler available " +
                    "to garner authentication information from the user");
        
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("user name: ");
        callbacks[1] = new PasswordCallback("password: ", false);
        
        try
        {
            callbackHandler.handle(callbacks);
            username = ((NameCallback) callbacks[0]).getName();
            char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
            if(tmpPassword == null)
            {
                // treat a NULL password as an empty password
                tmpPassword = new char[0];
            }
            password = new char[tmpPassword.length];
            System.arraycopy(tmpPassword, 0,
                    password, 0, tmpPassword.length);
            ((PasswordCallback) callbacks[1]).clearPassword();
        }
        catch(java.io.IOException ioe)
        {
            throw new LoginException(ioe.toString());
        }
        catch(UnsupportedCallbackException uce)
        {
            throw new LoginException("Error: " + uce.getCallback().toString() +
                    " not available to garner authentication information " +
                    "from the user");
        }
        
        // print debugging information
        if(debug)
        {
            System.out.println("\t\t[LucaLoginModule] " +
                    "user entered user name: " +
                    username);
            System.out.print("\t\t[LucaLoginModule] " +
                    "user entered password: ");
            for(char c : password)
            {
                System.out.print(c);
            }
            System.out.println();
        }
        
        // verify the username/password
        boolean usernameCorrect = false;
        boolean passwordCorrect = false;
        int userIndex = 0;
        User[] allUsers = new User[ABP.Account.getAccountUsers().size()];
        User admin = new User("A", "D", "M", "admin", "a", "d", "m", User.UserType.SYSTEM_ADMIN, 0);
        User ryan = new User("R", "D", "T", "running", "r", "d", "t", User.UserType.GENERAL_USER, 500);
        admin.addUser(ryan);
        ABP.Account.getAccountUsers().toArray(allUsers);
        for(int i = 0; i < allUsers.length - 1; i++)
        {
            if(allUsers[i].getUsername().equalsIgnoreCase(username))
            {
                userIndex = i;
                usernameCorrect = true;
            }
        }
        
        int numErrorsInPW = 0;
        for(int j = 0; j < password.length - 1; j++)
        {
            if(password[j] != allUsers[userIndex].getPassword().charAt(j))
                numErrorsInPW++;
        }
        
        if(usernameCorrect && password.length == allUsers[userIndex].getPassword().length() && numErrorsInPW == 0)
        {
            // authentication succeeded!!!
            passwordCorrect = true;
            if(debug)
                System.out.println("\t\t[LucaLoginModule] " +
                        "authentication succeeded");
            succeeded = true;
            return true;
        }
        else
        {
            
            // authentication failed -- clean out state
            if(debug)
                System.out.println("\t\t[LucaLoginModule] " +
                        "authentication failed");
            succeeded = false;
            username = null;
            for(int i = 0; i < password.length; i++)
                password[i] = ' ';
            password = null;
            if(!usernameCorrect)
            {
                throw new FailedLoginException("LucaMember Name Incorrect");
            }
            else
            {
                throw new FailedLoginException("Password Incorrect");
            }
        }
    }
    
    /**
     * This method is called if the LoginContext's
     * overall authentication succeeded
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
     * succeeded).
     * <p>
     * If this LoginModule's own authentication attempt
     * succeeded (checked by retrieving the private state saved by the
     * <code>login</code> method), then this method associates a
     * <code>SamplePrincipal</code>
     * with the <code>Subject</code> located in the
     * <code>LoginModule</code>.  If this LoginModule's own
     * authentication attempted failed, then this method removes
     * any state that was originally saved.
     *
     * @return true if this LoginModule's own login and commit
     * attempts succeeded, or false otherwise.
     */
    public boolean commit()
    {
        if(!succeeded)
        {
            return false;
        }
        else
        {
            // add a Principal (authenticated identity)
            // to the Subject
            
            // assume the user we authenticated is the LucaPrincipal
            userPrincipal = new LucaPrincipal(username);
            subject.getPrincipals().add(userPrincipal);
            
            if(debug)
            {
                System.out.println("\t\t[LucaLoginModule] " +
                        "added SamplePrincipal to Subject");
            }
            
            // in any case, clean out state
            username = null;
            for(int i = 0; i < password.length; i++)
                password[i] = ' ';
            password = null;
            
            commitSucceeded = true;
            return true;
        }
    }
    
    /**
     * This method is called if the LoginContext's overall authentication failed.
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules did not succeed).
     * <p>
     * If this LoginModule's own authentication attempt succeeded (checked by retrieving the private state saved by the
     * <code>login</code> and <code>commit</code> methods), then this method cleans up any state that was originally
     * saved.
     * @return false if this LoginModule's own login and/or commit attempts
     * failed, and true otherwise.
     */
    public boolean abort()
    {
        if(!succeeded)
        {
            return false;
        }
        else
        if(!commitSucceeded)
        {
            // login succeeded but overall authentication failed
            succeeded = false;
            reset();
        }
        else
        {
            logout(); // overall authentication succeeded and commit succeeded, but someone else's commit failed
        }
        return true;
    }
    
    /**
     * Logout the user.
     * <p>
     * This method removes the <code>SamplePrincipal</code>
     * that was added by the <code>commit</code> method.
     *
     * @return true in all cases since this <code>LoginModule</code>
     * should not be ignored.
     */
    public boolean logout()
    {
        
        subject.getPrincipals().remove(userPrincipal);
        succeeded = false;
        succeeded = commitSucceeded;
        reset();
        return true;
    }
    
    private void reset()
    {
        username = null;
        if(password != null)
        {
            for(int i = 0; i < password.length; i++)
                password[i] = ' ';
            password = null;
        }
        userPrincipal = null;
    }
}