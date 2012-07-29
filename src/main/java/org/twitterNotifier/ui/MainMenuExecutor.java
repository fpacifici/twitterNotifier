package org.twitterNotifier.ui;

/**
 * To be implemented by who wants to listene to menu events.
 * @author fpacifici
 *
 */
public interface MainMenuExecutor {
	public void clickExit();
	
	public void clickSetup();
	
	public void clickStart();
	
	public void clickStop();
	
	public void clickShow();
}
