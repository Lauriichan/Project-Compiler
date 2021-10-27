package me.lauriichan.school.compile.window.view;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;
import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.syntaxphoenix.syntaxapi.utils.java.Strings;

import jnafilechooser.api.JnaFileChooser;
import jnafilechooser.api.JnaFileChooser.Mode;
import me.lauriichan.school.compile.data.translation.Translation;
import me.lauriichan.school.compile.project.Application;
import me.lauriichan.school.compile.util.UserSettings;
import me.lauriichan.school.compile.util.file.FileHelper;
import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.component.Button;
import me.lauriichan.school.compile.window.ui.component.CheckButton;
import me.lauriichan.school.compile.window.ui.component.Label;
import me.lauriichan.school.compile.window.ui.component.TextField;
import me.lauriichan.school.compile.window.ui.component.goemetry.LineSeperator;
import me.lauriichan.school.compile.window.ui.util.BoxRenderers;
import me.lauriichan.school.compile.window.ui.util.ICharFilter;
import me.lauriichan.school.compile.window.ui.util.ICharMapper;
import me.lauriichan.school.compile.window.ui.util.IStringValidator;

public final class SettingView extends View<BasicPane> {

    private final DebugView debug = new DebugView();

    public SettingView() {
        super("ui.view.setting", new BasicPane());
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        addExecutableChooser(40, "ui.setting.application", () -> {
            Application app = Application.getDefault();
            return app == null ? null : app.getExecutable().getPath();
        }, file -> Application.setDefaultName(Strings.firstLetterToUpperCase(file.getName()), file));
        addExecutableChooser(110, "ui.setting.java", () -> {
            String value = UserSettings.getString("java");
            return value.isEmpty() ? null : value;
        }, file -> UserSettings.setString("java", file.getPath()));
        seperator(160);
        addField(210, "ui.setting.language (key.restart.required)", 
            Translation::has,
            character -> !(Character.isAlphabetic(character) || character == '-'), 
            character -> Character.toLowerCase(character),
            () -> {
                Translation translation = Translation.getDefault();
                return translation.getCode().isEmpty() ? Translation.getDefaultCode() : translation.getName();
            }, 
            string -> UserSettings.setString("language", Translation.get(string).getCode()));
        seperator(260);
        addOption(290, "ui.setting.debug", () -> UserSettings.getBoolean("debug"), state -> {
            UserSettings.setBoolean("debug", state);
            if (state) {
                getManager().add(debug);
                return;
            }
            getManager().remove(debug);
        });

    }

    private void seperator(int y) {
        LineSeperator seperator = new LineSeperator(false);
        seperator.setColor(Color.DARK_GRAY);
        seperator.setThickness(2);
        seperator.setX(10);
        seperator.setY(y);
        seperator.setWidth(pane.getWidth() - seperator.getX() * 2);
        seperator.setHeight(16);
        pane.addChild(seperator);
    }

    private void addOption(int y, String label, Supplier<Boolean> current, Consumer<Boolean> listener) {
        CheckButton button = new CheckButton();
        button.setX(10);
        button.setY(y);
        button.setSize(20, 20);
        button.setBox(Color.DARK_GRAY, color("#353737"));
        button.setBoxFade(0.3, 0.125);
        button.setLine(Color.DARK_GRAY);
        button.setIcon(Color.GRAY, Color.GREEN);
        button.setIconFade(0.3, 0.125);
        button.setOffset(0);
        button.setOffRender(BoxRenderers.CROSS);
        button.setOnRender(BoxRenderers.CHECKMARK);
        button.setListener(listener);
        button.setState(current.get());

        Label btnLabel = new Label();
        btnLabel.setText(Translation.getDefault().translate(label));
        btnLabel.setY(button.getY() - 6);
        btnLabel.setX(button.getX() + 30);
        btnLabel.setWidth(120);
        btnLabel.setHeight(32);
        btnLabel.setTextCentered(false);

        pane.addChild(btnLabel);
        pane.addChild(button);
    }

    private void addField(int y, String label, IStringValidator validator, ICharFilter filter, ICharMapper mapper, Supplier<String> current,
        Consumer<String> setter) {
        TextField field = new TextField();
        field.setX(10);
        field.setY(y);
        field.setWidth(pane.getWidth() - field.getX() * 2);
        field.setHeight(32);
        field.setFontSize(14);
        field.setFontColor(Color.LIGHT_GRAY);
        field.setLine(color("#353737"));
        field.setBackground(color("#6D6D6D"));
        field.setInvalidBackground(color("#6F3535"));
        field.setInvalidLine(color("#353737"));
        field.setInvalidFontColor(color("#F47777"));
        field.setOutline(true);
        field.setLineSize(1);
        field.setLocked(false);
        field.setValidator(validator);
        String value = current.get();
        if (value != null) {
            field.setContent(value);
        }
        field.setValidConsume(setter);
        if (filter != null) {
            field.setFilter(filter);
        }
        if (mapper != null) {
            field.setMapper(mapper);
        }

        Label fieldLabel = new Label();
        fieldLabel.setText(Translation.getDefault().translate(label));
        fieldLabel.setY(field.getY() - 26);
        fieldLabel.setX(field.getX());
        fieldLabel.setWidth(240);
        fieldLabel.setHeight(32);
        fieldLabel.setTextCentered(false);

        pane.addChild(fieldLabel);
        pane.addChild(field);
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

        Translation translation = Translation.getDefault();

        Label applicationLabel = new Label();
        applicationLabel.setText(translation.translate(label));
        applicationLabel.setY(application.getY() - 26);
        applicationLabel.setX(application.getX());
        applicationLabel.setWidth(120);
        applicationLabel.setHeight(32);
        applicationLabel.setTextCentered(false);

        Button openButton = new Button();
        openButton.setText(translation.translate("ui.button.open"));
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
                selected = FileHelper.asRelative(selected);
                application.setContent(selected.getPath());
                setter.accept(selected);
            }
        });

        pane.addChild(openButton);
        pane.addChild(applicationLabel);
        pane.addChild(application);
    }

}
