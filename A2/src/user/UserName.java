package user;

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

    public UserName(String username){
        this.UID = username;
        this.attach(Feed);
    }
    
    //Returns username
    public String getUID(){
        return UID;
    }
    
    //Returns followers list
    public ArrayList getObserverList() {
        return observerList;
    }

    //Returns followed users
    public ArrayList<UserName> getFollowing() {
        return following;
    }

    //Get list of following
    public String[] getFollowingNames(){
        String[] s = new String[500]; //Up to 500
        for(int i = 0; i < following.size(); i++){
            s[i] = this.following.get(i).getUID();
        }
        return s;
    }

    public String[] getFollowerList(){
        String[] s = new String[500]; //Up to 500
        for(int i = 0; i < followerList.size(); i++){
            s[i] = this.followerList.get(i).getUID();
        }
        return s;
    }

    //Return user news feed
    public Feed getNewsFeed() {
        return Feed;
    }

    //Output own tweet in feed and notify followers
    public void tweet(String tweet){
        this.messages.add(tweet);
        notifyFollowers(tweet);
    }

    //Return tweets
    public ArrayList<String> getTweets(){
        return this.messages;
    }

    //Add usernames to following list
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

    //Notify followers
    public void notifyFollowers(String tweet){
        for(Observer follower : observerList){
            follower.update((String) this.UID + ": " + tweet);
        }   
    }

	@Override
	public void notifyFollowers() {
		// TODO Auto-generated method stub
		
	}
}
