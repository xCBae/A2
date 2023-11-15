package component;

import java.util.ArrayList;

public class Feed implements Observer, Observable {

    private  ArrayList<String> messages;
    private ArrayList<Observer> observerList = new ArrayList<>();

    public Feed(){
        this.messages = new ArrayList<String>();
    }

    //Add messages to newsfeed
    @Override
    public void update(String tweet){
        messages.add(tweet);
        notifyFollowers("");
    }

    //Return the messages in user newsfeed
    public ArrayList<String> getMessages(){
        return messages;
    }

    //Count tweets in user's newsfeeds
    public int countMessages(){
        return messages.size();
    }

   //Count positive tweets
    public int countPositiveMessages(){
        int posMsg = 0;
        String[] positiveWords = {"good","great","excellent","fun"};
        for(int i = 0; i < messages.size(); i++){
            for(int k = 0; k < positiveWords.length; k++){
                if(this.messages.get(i).contains(positiveWords[k])){
                    posMsg++;
                }
            }
        }
        return posMsg;
    }
    
    @Override
    public void attach(Observer o) {
        observerList.add(o);
    }

    @Override
    public void detach(Observer o) {
        observerList.remove(o);
        
    }

    public void notifyFollowers(String tweet) {
        for(Observer follower : observerList){
            follower.update(tweet);
        }  
    }

	@Override
	public void notifyFollowers() {
		// TODO Auto-generated method stub
		
	}
}
