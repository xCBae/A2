package user;

public abstract class UserComponent  {
    private String UID;

    public String getUID(){
        return UID;
    }

    public UserComponent getComponent(int componentIndex){
        throw new UnsupportedOperationException();
    }

	public void remove(UserComponent removeUserComponent) {
		// TODO Auto-generated method stub
		
	}

}
