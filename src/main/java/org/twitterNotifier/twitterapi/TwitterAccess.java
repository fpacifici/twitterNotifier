package org.twitterNotifier.twitterapi;

/**
 * Main access interface to twitter.
 * 
 * It provides a facade for getting the list of new tweets.
 * 
 * An instance of the implementaiton of this interface represents a statefull connection
 * with a Twitter account.
 * 
 * @author fpacifici
 *
 */
public interface TwitterAccess {
	
	/**
	 * Initializes the api, and performs the login.
	 *
	 */
	public void init(String token, String secret);
	
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
