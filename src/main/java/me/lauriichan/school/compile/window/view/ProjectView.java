package me.lauriichan.school.compile.window.view;

import me.lauriichan.school.compile.window.ui.component.Button;
import me.lauriichan.school.compile.window.ui.component.TextField;

final class ProjectView extends View {
    
    private final Button newBtn = new Button();
    private final Button saveBtn = new Button();
    private final Button openBtn = new Button();
    private final Button compileBtn = new Button();
    
    private final TextField nameTxt = new TextField();
    private final TextField packageTxt = new TextField();

    public ProjectView() {
        super("Compiler - Project");
    }

}
