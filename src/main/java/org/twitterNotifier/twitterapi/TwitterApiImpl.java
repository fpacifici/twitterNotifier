package org.twitterNotifier.twitterapi;

import java.net.URL;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.twitterNotifier.config.Configuration;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Base implementation opf the Tqitter access API which works as a facade of Twitter4J
 * 
 * @author fpacifici
 *
 */
public class TwitterApiImpl implements TwitterAccess {

	private static final String CONSUMER_KEY = "OkfHDsRj6wvndOo7Bl0g";
	
	private static final String CONSUMER_SECRET = "TaiNrIzch0MYT161weKYOzToHRUbKfB350iVgaEteyY";
	
	private Twitter twitter;
	
	private long lastId = -1;
	
	private boolean started = false;
	
	/**b
	 * Statically build the First OAuth request for Twitter to obtain the PIN
	 * @return
	 */
	public static RequestToken prepareConnectionRequest(){
		try{
		Twitter twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		RequestToken requestT = twitter.getOAuthRequestToken();
		return requestT;
		}catch(TwitterException e){
			throw new TechnicalException("Cannot contact Twitter", e);
		}
	}
	
	/**
	 * Sends the PIN and saves the configuration
	 * @param token
	 * @param pin
	 */
	public static void storeAccessToken(RequestToken token, String pin){
		try{
		Twitter twitter = TwitterFactory.getSingleton();
		AccessToken atoken = twitter.getOAuthAccessToken(token,pin);
		Configuration c = Configuration.getInstance();
		c.setToken(atoken.getToken());
		c.setSecret(atoken.getTokenSecret());
		c.save();
		}catch(TwitterException e){
			throw new TechnicalException("Cannot contact twitter",e);
		}catch(ConfigurationException e){
			throw new TechnicalException("Cannot save configuration",e);
		}
	}


	@Override
	public void init(String token, String secret) {
		twitter = TwitterFactory.getSingleton();
		AccessToken at = new AccessToken(token, secret);
		twitter.setOAuthConsumer(CONSUMER_KEY,CONSUMER_SECRET);
		twitter.setOAuthAccessToken(at);
	}

	@Override
	public void startListening(TweetListener listener, long period) {
		if (twitter == null){
			throw new IllegalStateException("twitter API not initialized");
		}
		if (listener == null){
			throw new IllegalArgumentException("Need a listener.");
		}
		started = true;
		while (started){
			try{
				List<Status> status = getUpdates();
				listener.newTweets(status);
				Thread.sleep(period);
			}catch(TwitterException e){
				throw new TechnicalException("Cannot get updates ",e);
			}catch(InterruptedException e){
				started = false;
			}
		}
	}
	
	/**
	 * Loads the updates at this moment
	 * @return
	 */
	private List<Status> getUpdates() throws TwitterException{
		List<Status> ret  = null;
		if (lastId > 0){
			Paging p = new Paging(lastId);
			ret = twitter.getHomeTimeline(p);
		}else {
			ret = twitter.getHomeTimeline();
		}
		if (ret != null && ret.size() > 0){
			Status s = ret.get(0);
			lastId = s.getId();
		}
		return ret;
	}

	@Override
	public void stopListening() {
		started = false;
	}

}
