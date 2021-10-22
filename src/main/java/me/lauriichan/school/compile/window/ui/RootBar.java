package me.lauriichan.school.compile.window.ui;

import me.lauriichan.school.compile.window.ui.bar.BarBox;
import me.lauriichan.school.compile.window.ui.bar.IBoxRenderer;

public abstract class RootBar extends TriggerBar<BarBox> {

    public abstract BarBox createBox(IBoxRenderer renderer);

}
