package org.twitterNotifier.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.twitterNotifier.twitterapi.TechnicalException;
import org.twitterNotifier.twitterapi.TwitterAccess;
import org.twitterNotifier.twitterapi.TwitterAccessFactory;
import org.twitterNotifier.twitterapi.TwitterApiImpl;

import twitter4j.auth.RequestToken;

/**
 * Window allowing the creation of the access token through PIN based authorization.
 * 
 * @author fpacifici
 *
 */
public class TwitterRegistrationPanel extends JPanel {
	
	private JTextField pinField = new JTextField();
	
	private JTextField urlField = new JTextField();
	
	private JButton okButton = new JButton("OK");
	
	private JButton cancelButton = new JButton("Cancel");
	
	private RequestToken token;
	
	private TwitterAccess twitter;
	
	/**
	 * prepares the layout
	 */
	public TwitterRegistrationPanel() {
		initGraphics();
		attachActions();
		TwitterAccessFactory.reset();
		twitter = TwitterAccessFactory.getInstance();
		try{
			token = twitter.prepareConnectionRequest();
			urlField.setText(token.getAuthorizationURL());
		}catch(TechnicalException e){
			displayTwitterError(this, e);
			okButton.setEnabled(false);
		}
	}
	
	/**
	 * Display an error message.
	 * @param p
	 * @param e
	 */
	private void displayTwitterError(Component p, Throwable e){
		e.printStackTrace();
		String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
		JOptionPane.showMessageDialog(p, "Cannot get registration URL:\n"+message);
	}
	
	/**
	 * Builds the window
	 */
	private void initGraphics() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		
		JLabel label1 = new JLabel("You need to authorize TwitterNotifier your Twitter account to get notifications.");
		
		JLabel label2 = new JLabel("To do so, put the address below in your browser, follow the instructions and, once done");
		JLabel label3 = new JLabel("put the PIN code provided by Twitter in the text field.");
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(3, 1));
		p2.add(label1);
		p2.add(label2);
		p2.add(label3);
		add(p2,c);
		
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridwidth = 1;
		c2.gridx = 0;
		c2.gridy = 1;
		c2.weightx = 0.3;
		
		add (new JLabel("URL"),c2);
		
		GridBagConstraints c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.gridwidth = 1;
		c3.gridx = 1;
		c3.gridy = 1;
		c3.weightx = 0.7;
		
		add (urlField, c3);
		
		GridBagConstraints c4 = new GridBagConstraints();
		c4.fill = GridBagConstraints.HORIZONTAL;
		c4.gridwidth = 1;
		c4.gridx = 0;
		c4.gridy = 2;
		c4.weightx = 0.3;
		
		add(new JLabel("PIN"),c4);
		
		GridBagConstraints c5 = new GridBagConstraints();
		c5.fill = GridBagConstraints.HORIZONTAL;
		c5.gridwidth = 1;
		c5.gridx = 1;
		c5.gridy = 2;
		c5.weightx = 0.7;
		
		add (pinField,c5);
		
		GridBagConstraints c6 = new GridBagConstraints();
		c6.fill = GridBagConstraints.HORIZONTAL;
		c6.gridwidth = 1 ;
		c6.gridx = 0;
		c6.gridy = 3;
		c6.weightx = 0.5;
		
		add (okButton,c6);
	
		GridBagConstraints c7 = new GridBagConstraints();
		c7.fill = GridBagConstraints.HORIZONTAL;
		c7.gridwidth = 1 ;
		c7.gridx = 1;
		c7.gridy = 3;
		c7.weightx = 0.5;
		
		add (cancelButton, c7);
	}
	
	private void attachActions(){
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TwitterRegistrationPanel.this.getParent().setVisible(false);
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String pin = pinField.getText();
				if (pin != null){
					try{
						twitter.storeAccessToken(pin);
						twitter.init();
						TwitterRegistrationPanel.this.getParent().setVisible(false);
					}catch(TechnicalException e2){
						e2.printStackTrace();
						TwitterRegistrationPanel.this.displayTwitterError(TwitterRegistrationPanel.this, e2);
					}
				}
			}
		});
	}
}
