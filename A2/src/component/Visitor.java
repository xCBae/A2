package component;

import user.UserComponent;

public abstract class Visitor {
    public abstract Tree visit(Tree root, UserComponent userC);
 }