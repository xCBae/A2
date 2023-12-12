package component;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import user.UserComponent;
import user.UserName;

public class Tree extends Visitable {
    
    private String uid;
    private List<Tree> children = new LinkedList<>();
    private UserComponent userC;
    public Tree(String userid, UserComponent component){
        uid = userid;
        userC = component;
    }

    //Return user id
    public String getUID(){
        return this.uid;
    }

    //Return children of node
    public List<Tree> getChildren(){
        return this.children;
    }

    //Count the total number of messages sent from all users in index 0 and 
    public int[] countMsg(Tree root){
        int msgCount = 0;
        int posMsg = 0;
        int[] c = new int[2];
        Queue<Tree> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()) {
            int len = queue.size();
            for(int i=0;i<len;i++) {
                Tree node = queue.poll();
                assert node != null;
                if(node.userC instanceof UserName){
                    UserName user = (UserName)node.userC;
                    msgCount += user.getNewsFeed().countMessages();
                    posMsg += user.getNewsFeed().countPositiveMessages();
                }
                for (Tree item : node.children) {
                    queue.offer(item);
                }
            }
        }
        c[0] = msgCount;
        c[1] = posMsg;
        return c;
    }

    //Return the user component associated with the node
    public UserComponent getUserComponent(){
        return userC;
    }

    //Accept visitors
    public Tree accept(Visitor visitor, UserComponent userComp){
        return visitor.visit(this, userComp);
    }

    public boolean accept(VisitorValidation visitor){
        return visitor.visit(this);
    }

    public String accept(UserUpdateVisitor visitor){
        return visitor.visit(this);
    }

	@Override
	public Tree accept(Visitor v, Tree userComp) {
		return null;
	}

	@Override
	public String accept(Visitor v) {
		// TODO Auto-generated method stub
		return null;
	}
}