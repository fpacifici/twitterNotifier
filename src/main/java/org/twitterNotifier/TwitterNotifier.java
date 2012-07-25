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
import java.awt.image.BufferedImage;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalIconFactory;

import org.twitterNotifier.config.Configuration;
import org.twitterNotifier.twitterapi.TwitterAccess;
import org.twitterNotifier.twitterapi.TwitterApiImpl;
import org.twitterNotifier.ui.TweetList;
import org.twitterNotifier.ui.TwitterListener;

import twitter4j.auth.RequestToken;

/**
 * Main class for twitter notifier. It manages the whole app cycle
 * 
 * @author fpacifici
 * 
 */
public class TwitterNotifier {

	private TwitterAccess twitterAccess;

	private TrayIcon icon;

	private TweetList tList;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TwitterNotifier notifier = new TwitterNotifier();
		notifier.startCycle();
	}

	/**
	 * Basic instantiation
	 */
	public TwitterNotifier() {
		tList = new TweetList();
		icon = new TrayIcon(getImage(), "Java application as a tray icon",
				createPopupMenu());
		icon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Hey, you activated me!");
			}
		});
		try {
			SystemTray.getSystemTray().add(icon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * start twitter listening.
	 */
	public void startCycle() {
		twitterAccess = new TwitterApiImpl();
		Configuration c = Configuration.getInstance();
		twitterAccess.init(c.getToken(), c.getSecret());

		TwitterListener listener = new TwitterListener(tList, icon);
		twitterAccess.startListening(listener, 30000);
	}

	/**
	 * Creates the popup menu to show tweets and to exit.
	 * 
	 * @return
	 * @throws HeadlessException
	 */
	private PopupMenu createPopupMenu() throws HeadlessException {
		PopupMenu menu = new PopupMenu();

		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(exit);
		MenuItem show = new MenuItem("Show recent tweets");
		show.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Recent tweets");
				frame.setSize(300, 400);
				frame.add(tList);
				frame.setVisible(true);
				icon.setImage(getImage());
			}
		});

		menu.add(show);
		return menu;
	}

	/**
	 * generates the image for the icon
	 * 
	 * @return
	 * @throws HeadlessException
	 */
	private Image getImage() throws HeadlessException {
		Icon defaultIcon = MetalIconFactory.getTreeFloppyDriveIcon();
		Image img = new BufferedImage(defaultIcon.getIconWidth(),
				defaultIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);

		return img;
	}
}
