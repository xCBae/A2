package user;

import java.util.ArrayList;

public class UserGroup extends UserComponent{

    private String UID;
    private  ArrayList<UserComponent> UserComponents = new ArrayList();

    public UserGroup(String newGroupName){
        UID = newGroupName;
    }

    @Override
    public String getUID(){
        return UID;
    }

    //Remove a user or group
    @Override
    public void remove(UserComponent removeUserComponent) {
        UserComponents.remove(removeUserComponent);
    }

    //Gets one specific node
    @Override
    public UserComponent getComponent(int componentIndex) {
        return UserComponents.get(componentIndex);
    }
    
}
