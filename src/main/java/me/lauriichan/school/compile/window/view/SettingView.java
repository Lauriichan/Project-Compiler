package me.lauriichan.school.compile.window.view;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;
import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.syntaxphoenix.syntaxapi.utils.java.Strings;

import jnafilechooser.api.JnaFileChooser;
import jnafilechooser.api.JnaFileChooser.Mode;
import me.lauriichan.school.compile.data.ISetting;
import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.project.Application;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.component.Button;
import me.lauriichan.school.compile.window.ui.component.Label;
import me.lauriichan.school.compile.window.ui.component.TextField;

public final class SettingView extends View {

    public SettingView() {
        super("Einstellungen");
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        addExecutableChooser(40, "Anwendung", () -> {
            Application app = Application.get(Application.getDefault());
            return app == null ? null : app.getExecutable().getPath();
        }, file -> Application.setDefault(Strings.firstLetterToUpperCase(file.getName()), file));
        addExecutableChooser(110, "Java", () -> {
            ISetting setting = Singleton.get(Settings.class).get("java", Settings.USER_SETTINGS);
            return setting.isValid() ? setting.getAs(String.class) : null;
        }, file -> Singleton.get(Settings.class).put(Settings.USER_SETTINGS.of("java", String.class, true)).set(file.getPath()));
    }

    private void addExecutableChooser(int y, String label, Supplier<String> current, Consumer<File> setter) {
        TextField application = new TextField();
        application.setX(10);
        application.setY(y);
        application.setWidth(pane.getWidth() - application.getX() * 3 - 80);
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
            return file.isFile() && file.getName().endsWith(".exe") && file.exists();
        });
        String value = current.get();
        if (value != null) {
            application.setContent(value);
        }

        Label applicationLabel = new Label();
        applicationLabel.setText(label);
        applicationLabel.setY(application.getY() - 26);
        applicationLabel.setX(application.getX());
        applicationLabel.setWidth(120);
        applicationLabel.setHeight(32);
        applicationLabel.setTextCentered(false);

        Button openButton = new Button();
        openButton.setText("Öffnen");
        openButton.setTextCentered(true);
        openButton.setPress(color("#646363"));
        openButton.setShadow(color("#646363"));
        openButton.setHover(Color.DARK_GRAY, color("#353737"));
        openButton.setHoverFade(0.3, 0.125);
        openButton.setHoverShadow(Color.DARK_GRAY, color("#353737"));
        openButton.setHoverShadowFade(0.3, 0.125);
        openButton.setWidth(80);
        openButton.setHeight(application.getHeight());
        openButton.setX(application.getX() + application.getWidth() + 10);
        openButton.setY(application.getY());
        openButton.setAction(() -> {
            JnaFileChooser file = new JnaFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setMode(Mode.Files);
            file.addFilter("Executable (*.exe)", "exe");
            file.showOpenDialog(pane.getInput().getPanel().getFrame());
            File selected = file.getSelectedFile();
            if (selected != null) {
                application.setContent(selected.getPath());
                setter.accept(selected);
            }
        });

        pane.addChild(openButton);
        pane.addChild(applicationLabel);
        pane.addChild(application);
    }

}
