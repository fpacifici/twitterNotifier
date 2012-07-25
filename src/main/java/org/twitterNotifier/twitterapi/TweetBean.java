package org.twitterNotifier.twitterapi;

import java.util.Date;

/**
 * 
 * Represents a tweet
 * 
 * @author fpacifici
 *
 */
public class TweetBean {
	private final String tweet;
	
	private final String author;
	
	private final Date timestamp;
	
	public TweetBean(String tweet, String author, Date timestamp) {
		super();
		this.tweet = tweet;
		this.author = author;
		this.timestamp = timestamp;
	}

	public String getTweet() {
		return tweet;
	}

	public String getAuthor() {
		return author;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	
}
