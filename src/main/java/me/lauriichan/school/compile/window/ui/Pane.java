package me.lauriichan.school.compile.window.ui;

public abstract class Pane extends Component implements Iterable<Component> {

    public abstract boolean addChild(Component component);

    public abstract boolean removeChild(Component component);

    public abstract int getChildrenCount();

    public abstract Component getChild(int index);

}
