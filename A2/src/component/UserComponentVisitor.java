package component;

import java.util.LinkedList;
import java.util.Queue;
import user.UserComponent;

public class UserComponentVisitor extends Visitor{
    
    public Tree visit(Tree root,UserComponent userC){
        if(root == null) return null;
        Queue<Tree> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()) {
            int len = queue.size();
            for(int i=0;i<len;i++) {
                Tree node = queue.poll();
                assert node != null;
                if(node.getUID().equals(userC.getUID()) && node.getUserComponent().getClass().equals(userC.getClass())){
                    return node;
                }
                for (Tree item : node.getChildren()) {
                    queue.offer(item);
                }
            }
        }
        return null;
    }
}