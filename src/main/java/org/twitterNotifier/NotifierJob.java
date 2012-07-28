package org.twitterNotifier;

import java.awt.TrayIcon;
import java.util.logging.Logger;

import org.twitterNotifier.twitterapi.TechnicalException;
import org.twitterNotifier.twitterapi.TwitterAccess;
import org.twitterNotifier.twitterapi.TwitterAccessFactory;
import org.twitterNotifier.ui.TweetList;
import org.twitterNotifier.ui.TwitterListener;

/**
 * Encapsulates the logic that gets the updates from twitter.
 * 
 * @author fpacifici
 * 
 */
public class NotifierJob {

	private boolean running = false;

	private boolean init = false;

	private TwitterAccess twitterAccess;

	private TwitterListener listener;

	private Logger logger = Logger.getLogger("TwitterJob");

	/**
	 * Start he job
	 */
	public synchronized void setStart() {
		logger.info("Setting start");
		running = true;
		notifyAll();
	}

	/**
	 * Stops the job
	 */
	public void setStop() {
		logger.info("Setting stop");
		running = false;
		twitterAccess.stopListening();
	}

	public void reset(TweetList tList, TrayIcon icon) {
		twitterAccess = TwitterAccessFactory.getInstance();
		logger.info("Reset Job");
		if (twitterAccess.isConfigured()) {
			twitterAccess.init();
			init = true;
			listener = new TwitterListener(tList, icon);
		}
	}

	/**
	 * Main method that manages the update cycle. This does never return.
	 */
	public synchronized void run() {
		logger.info("Job started");
		while (true) {
			logger.info("Looping");
			if (init && running) {
				logger.info("Start listener");
				twitterAccess.startListening(listener, 30000);
			}
			try {
				logger.info("Waiting");
				wait();
			} catch (InterruptedException e) {

			}

		}
	}
}
