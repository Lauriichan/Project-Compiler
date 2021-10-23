package me.lauriichan.school.compile.window.view;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;
import java.io.File;

import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.component.Label;
import me.lauriichan.school.compile.window.ui.component.TextField;

public final class SettingView extends View {

    public SettingView() {
        super("Einstellungen");
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        TextField application = new TextField();
        application.setX(10);
        application.setY(40);
        application.setWidth(pane.getWidth() - application.getX() * 2);
        application.setHeight(32);
        application.setFontSize(14);
        application.setFontColor(Color.LIGHT_GRAY);
        application.setBackground(color("#6D6D6D"));
        application.setLine(color("#353737"));
        application.setInvalidBackground(color("#6F3535"));
        application.setInvalidLine(color("#353737"));
        application.setInvalidFontColor(color("#F47777"));
        application.setOutline(true);
        application.setLineSize(1);
        application.setLocked(true);
        application.setValidator(string -> {
            File file = new File(string);
            return file.isDirectory() && file.exists();
        });

        Label applicationLabel = new Label();
        applicationLabel.setText("Anwendung");
        applicationLabel.setY(application.getY() - 26);
        applicationLabel.setX(application.getX());
        applicationLabel.setWidth(120);
        applicationLabel.setHeight(32);
        applicationLabel.setTextCentered(false);

        pane.addChild(applicationLabel);
        pane.addChild(application);
    }

}
