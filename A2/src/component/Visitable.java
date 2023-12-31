package component;

public abstract class Visitable {
    public abstract Tree accept(Visitor v, Tree userComp);
    public abstract boolean accept(VisitorValidation v);
    public abstract String accept(Visitor v);
}
