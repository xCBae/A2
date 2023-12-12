package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat; 
import component.Observer;
import component.Tree;
import component.UserComponentVisitor;
import user.UserName;
import java.awt.Color;

public class UserPanel extends JFrame implements Observer{
    
    private  JPanel contentPane;
	private  JTextField userTxtField;
	private  JTextField message_box;      
    private  UserName user;
	private  Tree root;
	private  UserComponentVisitor findUserC = new UserComponentVisitor();
	private  JList following_JList;
	private  JList newsFeed_Jlist;

    public UserPanel(UserName u, Tree tree){
    	setBackground(new Color(0, 128, 192));
        user = u;
		root = tree;
        makeGui();
    }

	//Return the user object of this class
	private UserName getUser(){
		return user;
	}

	//Call methods to setup the UI
    private void makeGui(){
        setTitle("User Panel: " + user.getUID());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		timePanel();
		followUser();
		followingList();
		tweetPanel();
		newsFeed();
        setVisible(true);
    }

	//Create UI to allow for users to be followed
    private  void followUser(){
        JPanel follow_panel = new JPanel();
		contentPane.add(follow_panel);
		follow_panel.setLayout(new BoxLayout(follow_panel, BoxLayout.X_AXIS));
		follow_panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		JPanel userTxt_panel = new JPanel();
		userTxt_panel.setBackground(new Color(0, 128, 192));
		follow_panel.add(userTxt_panel);
		
		userTxtField = new JTextField();
		userTxt_panel.add(userTxtField);
		userTxtField.setColumns(10);
		
		JPanel followBtn_panel = new JPanel();
		followBtn_panel.setBackground(new Color(0, 128, 192));
		follow_panel.add(followBtn_panel);
		
		JButton followButton = new JButton("Follow User");
		followButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserName u = new UserName(null);
				try {
					u = (UserName)root.accept(findUserC, new UserName(userTxtField.getText())).getUserComponent();
					u.attach(getUser().getNewsFeed());
					user.follow(u);
				} catch (Exception userNotFoundException) {
					System.out.println("User does not exist");
				}
				//Set the following list data and update the UI 
				following_JList.setListData(getUser().getFollowingNames());
				following_JList.updateUI();
			}
		});
		followButton.setPreferredSize(new Dimension(125, 20));
		followBtn_panel.add(followButton);
    }

	//Create UI to show the following list of the user
    private void followingList(){
        JPanel follower_panel;
		follower_panel = new JPanel();
		contentPane.add(follower_panel);
		follower_panel.setLayout(new BoxLayout(follower_panel, BoxLayout.Y_AXIS));

        JPanel ftitle_panel;
		ftitle_panel = new JPanel();
		ftitle_panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		follower_panel.add(ftitle_panel);
		ftitle_panel.setLayout(new BoxLayout(ftitle_panel, BoxLayout.X_AXIS));
		
		JLabel followingLabel = new JLabel("Following");
		followingLabel.setFont(new Font("Arial", Font.BOLD, 13));
		ftitle_panel.add(followingLabel);
		
		JPanel flist_panel;
		flist_panel = new JPanel();
		follower_panel.add(flist_panel);
		flist_panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		//Add JList to scroll panel
		JScrollPane scrollPane = new JScrollPane();
		flist_panel.add(scrollPane);
		following_JList = new JList();
		scrollPane.setViewportView(following_JList);
		following_JList.setFont(new Font("Arial", Font.PLAIN, 12));
		following_JList.setListData(getUser().getFollowingNames());
    }

	//Allow the user to make tweets
    private  void tweetPanel(){
        JPanel tweet_panel = new JPanel();
        tweet_panel.setBackground(new Color(0, 128, 192));
		contentPane.add(tweet_panel);
		tweet_panel.setLayout(new BoxLayout(tweet_panel, BoxLayout.X_AXIS));
		tweet_panel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
		
		JPanel msgBox_panel = new JPanel();
		msgBox_panel.setBackground(new Color(0, 128, 192));
		tweet_panel.add(msgBox_panel);
		
		message_box = new JTextField();
		message_box.setHorizontalAlignment(SwingConstants.LEFT);
		msgBox_panel.add(message_box);
		message_box.setColumns(10);
		
		JPanel tweetBtn_panel = new JPanel();
		tweetBtn_panel.setBackground(new Color(0, 128, 192));
		tweet_panel.add(tweetBtn_panel);
		
		JButton tweetBtn = new JButton("Tweet");
		tweetBtn.setPreferredSize(new Dimension(100, 20));
		tweetBtn.addActionListener(new ActionListener() {
			//Reverse the news feed to show recent messages on top and older ones on bottom
			public void actionPerformed(ActionEvent e) {
				getUser().tweet(message_box.getText());
				//Update tweet box UI with updates list of tweets
				update("");
			}
		});
		tweetBtn_panel.add(tweetBtn);
    }

	//Set the UI to showcase the newsfeed of the user
    private void newsFeed(){
        JPanel newsFeed_panel = new JPanel();
		contentPane.add(newsFeed_panel);
		newsFeed_panel.setLayout(new BoxLayout(newsFeed_panel, BoxLayout.Y_AXIS));
		
		JPanel nfTitle_panel = new JPanel();
		newsFeed_panel.add(nfTitle_panel);
		nfTitle_panel.setLayout(new BoxLayout(nfTitle_panel, BoxLayout.X_AXIS));
		nfTitle_panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		
		JLabel newsFeedLbl = new JLabel("Feed");
		newsFeedLbl.setFont(new Font("Arial", Font.BOLD, 13));
		nfTitle_panel.add(newsFeedLbl);
		
		JPanel nfList_panel = new JPanel();
		newsFeed_panel.add(nfList_panel);
		nfList_panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		nfList_panel.add(scrollPane_1);
		
		newsFeed_Jlist = new JList();
		scrollPane_1.setViewportView(newsFeed_Jlist);
		newsFeed_Jlist.setFont(new Font("Arial", Font.PLAIN, 12));
		//Reverse the newsFeed so that most recent messages are shown on top
		ArrayList<String> userNewsFeed = getUser().getNewsFeed().getMessages();
		getUser().getNewsFeed().attach(this);
		ArrayList<String> revArrayList = new ArrayList<String>();
				for (int i = userNewsFeed.size() - 1; i >= 0; i--) {
					revArrayList.add(userNewsFeed.get(i));
        		}
		newsFeed_Jlist.setListData(revArrayList.toArray());
    }

	//Time
	private void timePanel(){
		JPanel updatePanel = new JPanel();
		contentPane.add(updatePanel);
		updatePanel.setLayout(new GridLayout(1, 0, 0, 0));
		//Time format
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");    
		Date resultdate = new Date(user.getCreationTime());
		
		//Most recent update time
		JPanel lastUpdate_panel = new JPanel();
		lastUpdate_panel.setBackground(new Color(0, 128, 192));
		updatePanel.add(lastUpdate_panel);
		
		JLabel lastUpdate_label = new JLabel("");
		lastUpdate_label.setBackground(new Color(255, 255, 255));
		lastUpdate_label.setFont(new Font("Tahoma", Font.BOLD, 11));
		Date updateTime = new Date(user.getLastUpdateTime());
		lastUpdate_label.setText(sdf.format(updateTime));
		lastUpdate_panel.add(lastUpdate_label);
	}

	//Update the user UI tweets when changes are made in news feed
	@Override
	public void update(String tweet) {
		ArrayList<String> userNewsFeed = getUser().getNewsFeed().getMessages();
		ArrayList<String> revArrayList = new ArrayList<String>();
		for (int i = userNewsFeed.size() - 1; i >= 0; i--) {
			revArrayList.add(userNewsFeed.get(i));
        }
		newsFeed_Jlist.setListData(revArrayList.toArray());
		newsFeed_Jlist.updateUI();
	}
}
