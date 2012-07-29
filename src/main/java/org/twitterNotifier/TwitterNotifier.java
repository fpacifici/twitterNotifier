package org.twitterNotifier;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalIconFactory;

import org.twitterNotifier.config.Configuration;
import org.twitterNotifier.twitterapi.TechnicalException;
import org.twitterNotifier.twitterapi.TwitterAccess;
import org.twitterNotifier.twitterapi.TwitterAccessFactory;
import org.twitterNotifier.twitterapi.TwitterApiImpl;
import org.twitterNotifier.ui.MainMenuExecutor;
import org.twitterNotifier.ui.MainNotifierMenu;
import org.twitterNotifier.ui.TweetList;
import org.twitterNotifier.ui.TwitterListener;
import org.twitterNotifier.ui.TwitterRegistrationPanel;

import twitter4j.auth.RequestToken;


/**
 * Main class for twitter notifier. It manages the whole app cycle
 * 
 * @author fpacifici
 * 
 */
public class TwitterNotifier implements MainMenuExecutor{
	
	private TrayIcon icon;

	private TweetList tList;
	
	private MainNotifierMenu menu;
	
	private NotifierJob job;

	private Logger logger = Logger.getLogger("Main");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TwitterNotifier notifier = new TwitterNotifier();
		notifier.job.run();
	}

	/**
	 * Basic instantiation
	 */
	public TwitterNotifier() {
		Configuration c = Configuration.getInstance();
		
		tList = new TweetList();
		menu = createPopupMenu();
		icon = new TrayIcon(getImage(), "Java application as a tray icon",
				menu);
		
		try {
			SystemTray.getSystemTray().add(icon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		job = new NotifierJob();
		job.reset(tList, icon);
	}

	/**
	 * Creates the popup menu to show tweets and to exit.
	 * 
	 * @return
	 * @throws HeadlessException
	 */
	private MainNotifierMenu createPopupMenu() throws HeadlessException {
		MainNotifierMenu menu = new MainNotifierMenu(this);
		
		return menu;
	}
	
	

	@Override
	public void clickExit() {
		System.exit(0);
	}

	@Override
	public void clickSetup() {
		job.setStop();
		job.reset(tList, icon);
		
		JDialog dialog = new JDialog();
		dialog.add(new TwitterRegistrationPanel());
		dialog.setSize(700, 150);
		
		dialog.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				logger.info("Resetting the job");
				job.reset(tList, icon);
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		dialog.setVisible(true);
		
	}

	@Override
	public void clickStart() {
		job.setStart();
		menu.setStartEnabled(false);
		menu.setStopEnabled(true);
		
	}

	@Override
	public void clickStop() {
		job.setStop();
		menu.setStartEnabled(true);
		menu.setStopEnabled(false);
	}

	
	@Override
	public void clickShow() {
		JDialog frame = new JDialog();
		frame.setSize(700, 400);
		frame.add(tList);
		frame.setVisible(true);
		icon.setImage(getImage());
	}

	/**
	 * generates the image for the icon
	 * 
	 * @return
	 * @throws HeadlessException
	 */
	private Image getImage() throws HeadlessException {
		ImageIcon defaultIcon = new ImageIcon(this.getClass().getResource("twitterIconGray.gif"));
		return defaultIcon.getImage();
	}
}
