package org.twitterNotifier.config;

import java.util.logging.Level;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Provides the configuraiton to the whole application
 * 
 * @author fpacifici
 *
 */
public class Configuration {
	private static Configuration instance;
	
	private PropertiesConfiguration config;
	
	public static Configuration getInstance(){
		if (instance == null){
			instance = new Configuration();
		}
		return instance;
	}
	
	public Configuration() {
		try {
			config = new PropertiesConfiguration("twitterNotifier.properties");
		}catch(ConfigurationException e){
			e.printStackTrace();
		}
	}
	
	public void setToken(String token){
		config.setProperty("oauth.token", token);
	}
	
	public void setSecret(String secret){
		config.setProperty("oauth.secret", secret);
	}
	
	public void save() throws ConfigurationException{
		config.save();
	}
	 
	public String getToken(){
		return (String)config.getProperty("oauth.token");
	}
	
	public String getSecret() {
		return (String)config.getProperty("oauth.secret");
	}
	
	public Level getLogLevel(){
		String level = (String)config.getProperty("loglevel");
		return Level.parse(level);
	}
}
