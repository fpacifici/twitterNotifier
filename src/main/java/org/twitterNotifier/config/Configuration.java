package org.twitterNotifier.config;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;


/**
 * Provides the configuraiton to the whole application
 * 
 * @author fpacifici
 *
 */
public class Configuration {
	private static Configuration instance;
	
	private Properties config;
	
	public static Configuration getInstance(){
		if (instance == null){
			instance = new Configuration();
		}
		return instance;
	}
	
	public Configuration() {
		try {
			config = new Properties();
			config.load(new FileInputStream(new File("twitterNotifier.properties")));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void setToken(String token){
		config.setProperty("oauth.token", token);
	}
	
	public void setSecret(String secret){
		config.setProperty("oauth.secret", secret);
	}
	
	public void save() {
		try{
			config.store(new FileOutputStream(new File("twitterNotifier.properties")), "twitterNotifier automatically saved config");
		}catch(IOException e){
			e.printStackTrace();
		}
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
