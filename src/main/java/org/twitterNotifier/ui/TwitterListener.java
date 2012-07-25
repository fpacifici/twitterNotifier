package org.twitterNotifier.ui;

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Panel;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Icon;
import javax.swing.plaf.metal.MetalIconFactory;

import org.twitterNotifier.twitterapi.TweetListener;

import twitter4j.Status;

/**
 * Receives events and populates the UI
 * 
 * @author pyppo
 * 
 */
public class TwitterListener implements TweetListener {

	private TweetList uiList;

	private TrayIcon icon;

	public TwitterListener(TweetList uiList, TrayIcon icon) {
		super();
		this.uiList = uiList;
		this.icon = icon;
	}

	@Override
	public void newTweets(List<Status> tweets) {
		if (tweets.size() > 0) {
			uiList.addTweets(tweets);
			icon.setImage(getPresentImage());
		}
	}

	private static Image getPresentImage() throws HeadlessException {
		Icon defaultIcon = MetalIconFactory.getTreeHardDriveIcon();
		Image img = new BufferedImage(defaultIcon.getIconWidth(),
				defaultIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);

		return img;
	}
}
