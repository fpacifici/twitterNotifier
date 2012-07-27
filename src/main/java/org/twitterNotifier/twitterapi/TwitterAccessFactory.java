package org.twitterNotifier.twitterapi;

/**
 * Resettable Singleton for TwitterAccesss.
 * 
 * It creates an access on demand and it is able to reset it in order to reset the 
 * @author pyppo
 *
 */
public class TwitterAccessFactory {
	private static TwitterAccess instance = null;

	public static void reset(){
		instance = new TwitterApiImpl();
	}
	
	public synchronized static TwitterAccess getInstance(){
		if (instance == null){
			reset();
		}
		return instance;
	}
}
