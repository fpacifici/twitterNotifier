package org.twitterNotifier.twitterapi;

import twitter4j.auth.RequestToken;

/**
 * Main access interface to twitter.
 * 
 * It provides a facade for getting the list of new tweets.
 * 
 * An instance of the implementaiton of this interface represents a statefull connection
 * with a Twitter account.
 * 
 * 
 * 
 * @author fpacifici
 *
 */
public interface TwitterAccess {
	
	/**
	 * Writes given the pin and stores into configuration
	 * @param token
	 * @param pin
	 */
	public void storeAccessToken(String pin);
	
	/**
	 * Creates a connection request URL.
	 * @return
	 */
	public RequestToken prepareConnectionRequest();
	
	/**
	 * Initializes the api, and performs the login.
	 *
	 */
	public void init();
	
	/**
	 * Return true if I have an access token and an access secret.
	 * @return
	 */
	public boolean isConfigured();
	
	/**
	 * Starts the listener.
	 * 
	 * @param listener
	 * @param period
	 */
	public void startListening(TweetListener listener, long period);

	/**
	 * Stop the listener if it is started.
	 */
	public void stopListening();
}
