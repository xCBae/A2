package component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import user.UserComponent;
import user.UserName;

public class UserUpdateVisitor extends Visitor{
    
    //Traverse all users and find the user with the most recent update time
    public String visit(Tree root){
        ArrayList<String> userList = new ArrayList<>();
        Queue<Tree> queue = new LinkedList<>();
        queue.offer(root);
        UserName mostRecentUpdate = new UserName("TempUser");
        mostRecentUpdate.setLastUpdateTime(0);
        while(!queue.isEmpty()) {
            int len = queue.size();
            for(int i=0;i<len;i++) {
                Tree node = queue.poll();
                assert node != null;

                //If node is a user then compare the last updated times
                if(node.getUserComponent() instanceof UserName){
                    UserName user = (UserName)node.getUserComponent();
                    //If last updated time is less than the new last updated time then set the mostRecentUpdated user
                    if(mostRecentUpdate.getLastUpdateTime() < user.getLastUpdateTime()){
                        mostRecentUpdate = user;
                    }
                }
                
                for (Tree item : node.getChildren()) {
                    queue.offer(item);
                }
            }
        }
        return mostRecentUpdate.getUID();
    }

	@Override
	public Tree visit(Tree root, UserComponent userC) {
		// TODO Auto-generated method stub
		return null;
	}
}
