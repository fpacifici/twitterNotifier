package org.twitterNotifier.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.table.AbstractTableModel;

import twitter4j.Status;

/**
 * Conatins the list of tweets to show.
 * 
 * @author fpacifici
 *
 */
public class TweetListModel extends AbstractTableModel {

	private List<Status> shownStatus = new LinkedList<Status>(); 
	
	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return shownStatus.size();
	}

	
	
	@Override
	public Class<?> getColumnClass(int arg0) {
		if (arg0 == 2){
			return String.class;
		}else {
			return super.getColumnClass(arg0);
		}
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		if (arg0 < 0 || arg0 > shownStatus.size() - 1){
			throw new IllegalArgumentException("Requested line " + arg0+ " is not valid");
		}
		Status row = shownStatus.get(arg0);
		if (arg1 < 0 || arg1 > 3){
			throw new IllegalArgumentException("Requested column "+arg1+" is not valid");
		}
		switch(arg1){
		case 0:
			Date ret = row.getCreatedAt();
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			return df.format(ret);
		case 1:
			return row.getUser().getName();
		case 2:
			return row.getText();
		default:
			return null;
		}
	}

	/**
	 * Adds some tweets to the list.
	 * @param tweets
	 */
	public void addTweets(List<Status> tweets){
		if (tweets != null && tweets.size() > 0){
			ListIterator<Status> it = tweets.listIterator(tweets.size());
			while (it.hasPrevious()){
				Status tweet = it.previous();
				shownStatus.add(0, tweet);
			}
		}
		//normalize the list to 20
		if (shownStatus.size() > 20){
			Iterator<Status> it2 = shownStatus.listIterator(20);
			while (it2.hasNext()){
				it2.next();
				it2.remove();
			}
		}
		
	}
}
