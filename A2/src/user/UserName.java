package user;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import component.Observable;
import component.Observer;
import component.Feed;

public class UserName extends UserComponent implements Observable{
    
    private String UID;
    private ArrayList<Observer> observerList = new ArrayList();
    private ArrayList<UserName> following = new ArrayList();
    private Feed Feed = new Feed();
    private ArrayList<String> messages = new ArrayList<String>();
    private ArrayList<UserName> followerList = new ArrayList();
    private long lastUpdateTime;

    public UserName(String username){
        this.UID = username;
        this.attach(Feed);
        this.setCreationTime(System.currentTimeMillis());
        this.lastUpdateTime = this.getCreationTime();
    }
    
    //Returns the username
    public String getUID(){
        return UID;
    }
    
    //Returns the list of followers
    public ArrayList getObserverList() {
        return observerList;
    }

    //Returns the list of users that are being followed
    public ArrayList<UserName> getFollowing() {
        return following;
    }

    public long getLastUpdateTime(){
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long time){
        this.lastUpdateTime = time;
    }

    //Get list of names that user is following
    public String[] getFollowingNames(){
        String[] s = new String[500];
        for(int i = 0; i < following.size(); i++){
            s[i] = this.following.get(i).getUID();
        }
        return s;
    }

    public String[] getFollowerList(){
        String[] s = new String[500];
        for(int i = 0; i < followerList.size(); i++){
            s[i] = this.followerList.get(i).getUID();
        }
        return s;
    }

    //Return the user's newsfeed
    public Feed getNewsFeed() {
        return Feed;
    }

    //Add tweet to user and notify observers
    public void tweet(String tweet){
        this.messages.add(tweet);
        notifyFollowers(tweet);
        setLastUpdateTime(this.Feed.getLastUpdateTime());
        updateTime();
    }

    //Returns the tweets that the user has made
    public ArrayList<String> getTweets(){
        return this.messages;
    }

    //Add other userNames to following list
    public void follow(UserName user){
        this.following.add(user);
        user.followerList.add(this);
    }

    //Add observers
    public void attach(Observer o){
        observerList.add(o);
    }

    public void detach(Observer o){
        observerList.remove(o);
    }

    //Update followers time
    public void updateTime(){
        for(int i = 0; i < followerList.size(); i++){
            long lastUpTime = followerList.get(i).getNewsFeed().getLastUpdateTime();
            followerList.get(i).setLastUpdateTime(lastUpTime);
        }
    }
    
    //Update followers
    public void notifyFollowers(String tweet) {
        for (Observer follower : observerList) {
            long currentTime = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("(MMM, dd yyyy - HH:mm:ss) ");
            String timestamp = sdf.format(new Date(currentTime));
            
            String tweetWithTimestamp = timestamp + "- " + this.UID + ": " + tweet;
            follower.update(tweetWithTimestamp);
        }      
    }

	@Override
	public void notifyFollowers() {
		// TODO Auto-generated method stub
		
	}
}
