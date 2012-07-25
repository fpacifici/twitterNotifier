package org.twitterNotifier.twitterapi;

import java.util.List;

import twitter4j.Status;

/**
 * Reports the presence of new tweets.
 * 
 * @author fpacifici
 *
 */
public interface TweetListener {
	
	/**
	 * Event rised in case of new tweets.
	 * @param tweets
	 */
	public void newTweets(List<Status> tweets);
}
