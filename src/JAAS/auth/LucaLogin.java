package auth;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import java.io.*;
import java.security.Principal;
import java.util.Iterator;

/**
 * Authenticates user via username and password.
 * @author hLabs (adapted from "Java Platform, Standard Edition: Security Developer's Guide")
 * @since 0.1a
 */
public class LucaLogin
{
    /**
     * Attempt to authenticate the user.
     * @param args Input arguments for this application.  These are ignored.
     */
    public static void main(String[] args)
    {
        
        /* Obtain a LoginContext, needed for authentication. Tell it to use the LoginModule implementation specified by
        the entry named "Sample" in the JAAS login configuration file and to also use the specified CallbackHandler. */
        LoginContext lc = null;
        try
        {
            // "Luca" is in reference to luca_jaas.config
            lc = new LoginContext("Luca", new MyCallbackHandler());
        }
        catch(LoginException | SecurityException le)
        {
            System.err.println("Cannot create LoginContext. " + le.getMessage());
            System.exit(-1);
        }
    
        // The user has 5 attempts to authenticate.
        int i;
        for(i = 0; i < 5; i++)
        {
            try
            {
                // Attempt authentication
                lc.login();
                
                // If no exception is returned, authentication succeeded.
                break;
            }
            catch(LoginException le)
            {
                
                System.err.println("Authentication failed:");
                System.err.println("  " + le.getMessage());
                try
                {
                    Thread.sleep(3000);
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
        
        // True if the user fails authentication 5 times.
        if(i == 5)
        {
            System.out.println("Failed login 5 times.");
            System.exit(-1);
        }
        
        System.out.println("Authentication succeeded.");
        
        // Create a subject that will be one of the identifiers of the logged-in user
        Subject loggedInSubject = lc.getSubject();
        
        // Determine the existing Principals
        Iterator principalIterator = loggedInSubject.getPrincipals().iterator();
        System.out.println("Authenticated user has the following Principals: ");
        while(principalIterator.hasNext())
        {
            Principal p = (Principal) principalIterator.next();
            System.out.println("\t" + p.toString());
        }
    
        System.out.println("User has " + loggedInSubject.getPublicCredentials().size() + " Public Credential(s)");
        
        System.exit(0);
    }
}

/**
 * The application implements the CallbackHandler.
 *
 * <p> This application is text-based.  Therefore it displays information to the user using the OutputStreams
 * System.out and System.err, and gathers input from the user using the InputStream System.in.
 */
class MyCallbackHandler implements CallbackHandler
{
    
    /**
     * Invoke an array of Callbacks.
     * <p>
     * @param callbacks An array of <code>Callback</code> objects which contain the information requested by an
     *                  underlying security service to be retrieved or displayed.
     * @throws java.io.IOException If an input or output error occurs. <p>
     * @throws UnsupportedCallbackException If the implementation of this
     *                                      method does not support one or more of the Callbacks
     *                                      specified in the <code>callbacks</code> parameter.
     */
    public void handle(Callback[] callbacks)
    throws IOException, UnsupportedCallbackException
    {
    
        for(Callback callback : callbacks)
        {
            if(callback instanceof TextOutputCallback)
            {
            
                // display the message according to the specified type
                TextOutputCallback toc = (TextOutputCallback) callback;
                switch(toc.getMessageType())
                {
                    case TextOutputCallback.INFORMATION:
                        System.out.println(toc.getMessage());
                        break;
                    case TextOutputCallback.ERROR:
                        System.out.println("ERROR: " + toc.getMessage());
                        break;
                    case TextOutputCallback.WARNING:
                        System.out.println("WARNING: " + toc.getMessage());
                        break;
                    default:
                        throw new IOException("Unsupported message type: " +
                                toc.getMessageType());
                }
            }
            else if(callback instanceof NameCallback)
            {
            
                // prompt the user for a username
                NameCallback nc = (NameCallback) callback;
            
                System.err.print(nc.getPrompt());
                System.err.flush();
                nc.setName((new BufferedReader
                        (new InputStreamReader(System.in))).readLine());
            }
            else if(callback instanceof PasswordCallback)
            {
            
                // prompt the user for sensitive information
                PasswordCallback pc = (PasswordCallback) callback;
                System.err.print(pc.getPrompt());
                System.err.flush();
                pc.setPassword(System.console().readPassword());
            }
            else
            {
                throw new UnsupportedCallbackException
                        (callback, "Unrecognized Callback");
            }
        }
    }
}