package org.twitterNotifier;

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

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalIconFactory;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) throws Exception {
	      TrayIcon icon = new TrayIcon(getImage(), "Java application as a tray icon", 
	            createPopupMenu());
	      icon.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            JOptionPane.showMessageDialog(null, "Hey, you activated me!");
	         }
	      });
	      SystemTray.getSystemTray().add(icon);

	     
	      Thread.sleep(3000);

	      icon.displayMessage("Attention", "Please click here", 
	            TrayIcon.MessageType.WARNING);
	      
	      while (true) {
	    	  Thread.sleep(5000);
	    	  icon.setImage(getAnotherImage());
	    	  Thread.sleep(5000);
	    	  icon.setImage(getImage());
	      }
	   }

	   private static Image getImage() throws HeadlessException {
	      Icon defaultIcon = MetalIconFactory.getTreeHardDriveIcon();
	      Image img = new BufferedImage(defaultIcon.getIconWidth(), 
	            defaultIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
	      defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);

	      return img;
	   }
	   
	   private static Image getAnotherImage() throws HeadlessException {
		      Icon defaultIcon = MetalIconFactory.getTreeFloppyDriveIcon();
		      Image img = new BufferedImage(defaultIcon.getIconWidth(), 
		            defaultIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		      defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);

		      return img;
		   }

	   private static PopupMenu createPopupMenu() throws HeadlessException {
	      PopupMenu menu = new PopupMenu();

	      MenuItem exit = new MenuItem("Exit");
	      exit.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            System.exit(0);
	         }
	      });
	      menu.add(exit);

	      return menu;
	   }
}
