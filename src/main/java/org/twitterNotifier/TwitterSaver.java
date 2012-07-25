package org.twitterNotifier;

import java.util.List;

import org.twitterNotifier.config.Configuration;
import org.twitterNotifier.twitterapi.TweetListener;
import org.twitterNotifier.twitterapi.TwitterAccess;
import org.twitterNotifier.twitterapi.TwitterApiImpl;

import twitter4j.Status;

public class TwitterSaver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TwitterAccess access = new TwitterApiImpl();
		Configuration c = Configuration.getInstance();
		access.init(c.getToken(), c.getSecret());
		access.startListening(new TweetListener() {
			
			@Override
			public void newTweets(List<Status> tweets) {
				System.out.println("New Tweets: " + tweets.size());
				for (Status s : tweets){
					System.out.println(s.getCreatedAt()+ " - " + s.getUser().getName() + " - " + s.getText());
				}
				
			}
		}, 20000);
	}

}
