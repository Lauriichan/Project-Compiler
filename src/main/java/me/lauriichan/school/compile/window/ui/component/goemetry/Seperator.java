package me.lauriichan.school.compile.window.ui.component.goemetry;

public abstract class Seperator extends Geometry {

    protected final boolean vertical;

    public Seperator() {
        this(true);
    }

    public Seperator(boolean vertical) {
        this.vertical = vertical;
    }

    public final boolean isVertical() {
        return vertical;
    }

}
