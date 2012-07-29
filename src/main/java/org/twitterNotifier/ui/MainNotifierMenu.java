package org.twitterNotifier.ui;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Builds the main menu and wires it to the listener.
 *  
 * @author fpacifici
 *
 */
public class MainNotifierMenu extends PopupMenu {

	private MainMenuExecutor executor;
	
	private MenuItem exit;
	
	private MenuItem setup;
	
	private MenuItem start;
	
	private MenuItem stop;
	
	private MenuItem showTweets;
	
	public MainNotifierMenu(MainMenuExecutor executor){
		this.executor = executor;
		initGraphics();
	}
	
	private void initGraphics(){
		exit = new MenuItem("Exit");
		setup = new MenuItem("Setup");
		start = new MenuItem("Start listening");
		stop = new MenuItem("Stop listening");
		showTweets = new MenuItem("Show recent tweets");
		
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				executor.clickExit();
			}
		});
		
		setup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				executor.clickSetup();
			}
		});
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				executor.clickStart();
			}
		});
		
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				executor.clickStop();
			}
		});
		
		showTweets.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				executor.clickShow();
			}
		});
		
		add(start);
		add(stop);
		add(showTweets);
		add(setup);
		add(exit);
		
	}
	
	/**
	 * Sets the stop.
	 * @param enabled
	 */
	public void setStopEnabled(boolean enabled){
		stop.setEnabled(enabled);
	}
	
	public void setStartEnabled(boolean enabled){
		start.setEnabled(enabled);
	}
}
