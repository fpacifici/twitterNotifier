package org.twitterNotifier.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JEditorPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.TableColumn;

import twitter4j.Status;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TweetList extends JPanel {
	private JTable table;

	private JEditorPane htmlTable;
	
	private TweetListModel model = new TweetListModel();
	
	private Logger logger = Logger.getLogger("TwitterUI");
	/**
	 * Create the panel.
	 */
	public TweetList() {
		setLayout(new BorderLayout(0, 0));
		htmlTable = new JEditorPane();
		htmlTable.setContentType("text/html");
		htmlTable.setEditable(false);
		table = new JTable(model);
		
		htmlTable.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
					URL u = e.getURL();
					if (java.awt.Desktop.isDesktopSupported()){
						java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
						if(desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
							try{
								desktop.browse(u.toURI());
							}catch(Exception ex){
								logger.log(Level.WARNING, "Cannot browse the link", ex);
							}
						}
					}
				}
			}
		});
		
		/*table.getColumnModel().getColumn(0).setMaxWidth(150);
		table.getColumnModel().getColumn(1).setMaxWidth(250);
		table.getColumnModel().getColumn(2).setMinWidth(500);
		table.getColumnModel().getColumn(3).setMaxWidth(50);
		table.setDefaultRenderer(String.class, new MultilineTableCellRenderer());*/
		
		add(htmlTable, BorderLayout.CENTER);
		
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
		htmlTable.setText(buildTableContent());
	}
	
	/**
	 * Creates the HTML content for the HTML table.
	 * @return
	 */
	private String buildTableContent(){
		StringBuilder builder = new StringBuilder();
		builder.append("<html><body>");
		builder.append("<table>");
		for (int i = 0 ; i < model.getRowCount() ; i++){
			builder.append(buildRow(i));
		}
		builder.append("</table></body></html>");
		return builder.toString();
	}
	
	private String buildRow(int row){
		StringBuilder srow = new StringBuilder("<tr>");
		for (int c = 0; c < model.getColumnCount() ; c++){
			srow.append("<td>");
			Object o = model.getValueAt(row,c);
			if (o instanceof String){
				srow.append(linkify(o.toString()));
			}
			srow.append("</td>");
		}
		srow.append("</tr>");
		return srow.toString();
	}

	/**
     * Finds HTTP links and wraps them in an A HTML element.
     * @param text
     * @return
     */
    private String linkify(String text){
    	String ret = new String(text);
    	Pattern p = Pattern.compile("http:\\S*");
    	Matcher m = p.matcher(text);
    	boolean f = m.find();
    	while (f){
    		String found = m.group();
    		ret = ret.replace(found, "<a href='"+found+"'>"+found+"</a>");
    		f = m.find();
    	}
    	return ret;
    }
}
