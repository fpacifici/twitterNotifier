package org.twitterNotifier.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.TableColumn;

import twitter4j.Status;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TweetList extends JPanel {
	private JTable table;

	private TweetListModel model = new TweetListModel();
	
	/**
	 * Create the panel.
	 */
	public TweetList() {
		setLayout(new BorderLayout(0, 0));
		
		table = new JTable(model);
		add(table, BorderLayout.CENTER);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TweetList.this.getParent().setVisible(false);
			}
		});
		add(btnClose, BorderLayout.SOUTH);

	}
	
	/**
	 * Adds some tweets to the list.
	 * @param tweets
	 */
	public void addTweets(List<Status> tweets){
		model.addTweets(tweets);
		table.setModel(model);
	}

}
