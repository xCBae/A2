package component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import user.UserComponent;

public class VisitorValidation extends Visitor{

    public boolean visit(Tree root){
        ArrayList<String> userList = new ArrayList<>();
        Queue<Tree> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()) {
            int len = queue.size();
            for(int i=0;i<len;i++) {
                Tree node = queue.poll();
                assert node != null;
                String name = node.getUserComponent().getUID();
                //Check if name has a space
                if(name.contains(" ")){
                    return false;
                }
                //Check if name is already in userList
                for(int k = 0; k < userList.size(); k++){
                    if(name.equals(userList.get(k))){
                        return false;
                    }
                }
                //Add each userGroup and userName to List
                userList.add(name);
                
                for (Tree item : node.getChildren()) {
                    queue.offer(item);
                }
            }
        }
        return true;
    }

	@Override
	public Tree visit(Tree root, UserComponent userC) {
		// TODO Auto-generated method stub
		return null;
	}
}
