package user;

public abstract class UserComponent  {
    private String UID;
    private long creationTime;

    public String getUID(){
        return UID;
    }

    public long getCreationTime(){
        return creationTime;
    }

    public void setCreationTime(long time){
        this.creationTime = time;
    }

    public UserComponent getComponent(int componentIndex){
        throw new UnsupportedOperationException();
    }

	public void remove(UserComponent removeUserComponent) {
		// TODO Auto-generated method stub
		
	}

}
