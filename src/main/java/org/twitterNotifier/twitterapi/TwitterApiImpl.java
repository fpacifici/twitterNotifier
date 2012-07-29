package org.twitterNotifier.twitterapi;

import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private Twitter instance = null;
	
	private RequestToken token = null;
	
	private long lastId = -1;
	
	private boolean started = false;
	
	private Logger logger = Logger.getLogger("TwitterAPI");
	
	TwitterApiImpl(){
		instance = (new TwitterFactory()).getInstance();
		instance.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	}
	
	/**b
	 * Statically build the First OAuth request for Twitter to obtain the PIN
	 * @return
	 */
	public RequestToken prepareConnectionRequest(){
		try{
			
		RequestToken requestT = instance.getOAuthRequestToken();
		logger.info("Retrieved request token " + requestT.getAuthorizationURL());
		token = requestT;
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
	public void storeAccessToken(String pin){
		if (pin == null ){
			throw new IllegalArgumentException("PIN cannot be null");
		}
		if (token == null) {
			throw new IllegalStateException("Request token has not been created");
		}
		try{
		
		AccessToken atoken = instance.getOAuthAccessToken(token,pin);
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
	public void init() {
		Configuration c = Configuration.getInstance();
		String atoken = c.getToken();
		String secret = c.getSecret();
		
		if (atoken == null || secret == null) {
			throw new TechnicalException("Token or Secret null in configuration");
		}
		
		AccessToken at = new AccessToken(atoken, secret);
		
		instance.setOAuthAccessToken(at);
		logger.info("API initialized with token "+ at.getToken());
	}
	
	

	@Override
	public boolean isConfigured() {
		Configuration c = Configuration.getInstance();
		String atoken = c.getToken();
		String secret = c.getSecret();
		
		return(atoken != null && secret != null);
	}

	@Override
	public void startListening(TweetListener listener, long period) {
		logger.info("Start listening");
		if (instance == null){
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
				logger.logrb(Level.SEVERE, "TwitterAPI", "startListening","listening", "Error while getting updates",e);
				try {
					Thread.sleep(10000);
				}catch(InterruptedException e2){
					
				}
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
			logger.fine("Retrieving Twitter updates from " + lastId);
			ret = instance.getHomeTimeline(p);
		}else {
			logger.fine("Retrieving Twitter updates");
			ret = instance.getHomeTimeline();
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
