package me.lauriichan.school.compile.window.view;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;
import java.io.File;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.syntaxphoenix.syntaxapi.random.NumberGeneratorType;
import com.syntaxphoenix.syntaxapi.random.RandomNumberGenerator;

import jnafilechooser.api.JnaFileChooser;
import jnafilechooser.api.JnaFileChooser.Mode;
import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.project.ProjectInfo;
import me.lauriichan.school.compile.project.template.Template;
import me.lauriichan.school.compile.util.Executor;
import me.lauriichan.school.compile.window.input.mouse.MouseButton;
import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.Dialog;
import me.lauriichan.school.compile.window.ui.Pane;
import me.lauriichan.school.compile.window.ui.Panel;
import me.lauriichan.school.compile.window.ui.RootBar;
import me.lauriichan.school.compile.window.ui.component.Button;
import me.lauriichan.school.compile.window.ui.component.Label;
import me.lauriichan.school.compile.window.ui.component.TextField;
import me.lauriichan.school.compile.window.ui.component.bar.BarBox;
import me.lauriichan.school.compile.window.ui.util.Area;
import me.lauriichan.school.compile.window.ui.util.BoxRenderers;

public final class TemplateView extends View<BasicPane> {

    private int buttonId = 0;
    private boolean locked = false;
    private final Dialog dialog = new Dialog(this::buildProjectDialog);
    private final MainView main;

    private final RandomNumberGenerator random = NumberGeneratorType.MURMUR.create(System.currentTimeMillis());

    public TemplateView(MainView main) {
        super("Templates", new BasicPane());
        this.main = main;
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        int amount = Template.TEMPLATES.size();
        int possible = 15;
        for (int index = 0; index < amount; index++) {
            Template template = Template.TEMPLATES.get(index);
            if (template.isHidden()) {
                continue;
            }
            Button button = createTemplateButton();
            button.setText(template.getName() + "\n(" + template.getType() + ')');
            button.setAction(() -> createProject(template));
            pane.addChild(button);
            possible--;
        }
        while (possible-- != 0) {
            pane.addChild(createTemplateButton());
        }
        int offset = width / 16;
        Button random = createButton(offset, height - offset * 2, (width / 4) * 3 + offset * 2, offset);
        random.setText("Zufällig");
        random.setAction(() -> createProject(getTemplate()));
        pane.addChild(random);
    }

    @Override
    protected void exit() {
        dialog.exit();
    }

    /*
     * 
     */

    public Dialog getDialog() {
        return dialog;
    }

    private Template getTemplate() {
        Template template;
        while (!(template = nextTemplate()).getType().equals("Java")) {
        }
        return template;
    }

    private Template nextTemplate() {
        return Template.TEMPLATES.get(random.nextInt(Template.TEMPLATES.size() - 1));
    }

    public void createProject(Template template) {
        if (isLocked()) {
            return;
        }
        setButtonLocked(true);
        Pane pane = dialog.getPanel().getPane();
        for (int index = 3; index < 6; index++) {
            Component component = pane.getChild(index);
            if (!(component instanceof TextField)) {
                continue;
            }
            ((TextField) component).setContent("");
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        dialog.show((state, input) -> createProject(state, input, template));
    }

    private void createProject(Boolean state, Object input, Template template) {
        if (!state || input == null || !(input instanceof ProjectInfo)) {
            setButtonLocked(false);
            return;
        }
        setButtonLocked(false);
        ProjectInfo info = (ProjectInfo) input;
        boolean existed = Project.get(info.getName()) != null;
        Project.create(info.getName(), info.getPackageName(), new File(info.getDirectory()), template);
        Project project = Project.get(info.getName());
        if (project == null) {
            return;
        }
        Executor.execute(() -> project.open());
        if (existed) {
            main.updateProject(project);
            return;
        }
        main.addProject(project);
    }

    public void setButtonLocked(boolean locked) {
        if (this.locked == locked) {
            return;
        }
        this.locked = locked;
        for (Component component : pane.getChildren()) {
            if (!(component instanceof Button)) {
                continue;
            }
            ((Button) component).setLocked(locked);
        }
    }

    public boolean isButtonLocked() {
        return locked;
    }

    /*
     * 
     */

    private Button createTemplateButton() {
        if (buttonId == 15) {
            throw new IllegalStateException("There can't be more than 15 buttons of this Type");
        }
        int id = buttonId++;
        int size = pane.getWidth() / 4;
        int half = size / 8;
        Button button = createButton();
        button.setMultilineAllowed(true);
        button.setSize(size, half * 2);
        button.setPosition(half * 2 + (half * 2 + size) * (id % 3), half * 2 + (half * 3) * Math.floorDiv(id, 3));
        return button;
    }

    private Button createButton(int x, int y, int width, int height) {
        Button button = createButton();
        button.setSize(width, height);
        button.setPosition(x, y);
        return button;
    }

    private Button createButton() {
        Button button = new Button();
        button.setPress(color("#353737"));
        button.setShadow(color("#515252"));
        button.setHover(color("#484949"), color("#5A5B5B"));
        button.setHoverFade(0.3, 0.2);
        button.setHoverShadow(color("#515252"));
        button.setHoverShadowFade(0.35, 0.25);
        button.setShadowThickness(3);
        button.setTextCentered(true);
        return button;
    }

    private void buildProjectDialog(Dialog dialog, Panel panel) {
        Pane pane = panel.getPane();
        panel.setWidth(720);
        panel.setHeight(340);
        panel.setBarHeight(40);
        panel.setBackground(Color.GRAY);

        Component outline = new Component() {
            @Override
            public void render(Area area) {
                area.fillOutline(panel.getBackground(), 2, Color.DARK_GRAY);
            }
        };
        outline.setWidth(panel.getWidth());
        outline.setHeight(pane.getHeight());
        pane.addChild(outline);

        // Root bar
        RootBar bar = panel.getBar();
        BarBox close = bar.createBox(BoxRenderers.CROSS);
        close.setIcon(Color.GRAY, color("#F26161"));
        close.setIconFade(0.3, 0.15);
        close.setBox(Color.DARK_GRAY);
        close.setAction(MouseButton.LEFT, () -> dialog.fail(null));

        Label nameLabel = new Label();
        nameLabel.setText("Name");
        nameLabel.setY(16);
        nameLabel.setX(10);
        nameLabel.setWidth(120);
        nameLabel.setHeight(32);
        nameLabel.setTextCentered(false);
        pane.addChild(nameLabel);

        Label packageLabel = new Label();
        packageLabel.setText("Package Name");
        packageLabel.setY(86);
        packageLabel.setX(10);
        packageLabel.setWidth(120);
        packageLabel.setHeight(32);
        packageLabel.setTextCentered(false);
        pane.addChild(packageLabel);

        Label directoryLabel = new Label();
        directoryLabel.setText("Projekt Ordner");
        directoryLabel.setY(156);
        directoryLabel.setX(10);
        directoryLabel.setWidth(120);
        directoryLabel.setHeight(32);
        directoryLabel.setTextCentered(false);
        pane.addChild(directoryLabel);

        TextField name = new TextField();
        name.setX(10);
        name.setY(40);
        name.setWidth(700);
        name.setHeight(32);
        name.setFontSize(14);
        name.setBackground(color("#6D6D6D"));
        name.setLine(color("#353737"));
        name.setInvalidBackground(color("#6F3535"));
        name.setInvalidLine(color("#353737"));
        name.setInvalidFontColor(color("#F47777"));
        name.setOutline(true);
        name.setLineSize(1);
        name.setFilter(
            character -> !(Character.isAlphabetic(character) || Character.isDigit(character) || character == ' ' || character == '_'));
        name.setValidator(string -> !(string.startsWith(" ") || string.length() < 4));
        pane.addChild(name);

        TextField packageName = new TextField();
        packageName.setX(10);
        packageName.setY(110);
        packageName.setWidth(700);
        packageName.setHeight(32);
        packageName.setFontSize(14);
        packageName.setBackground(color("#6D6D6D"));
        packageName.setLine(color("#353737"));
        packageName.setInvalidBackground(color("#6F3535"));
        packageName.setInvalidLine(color("#353737"));
        packageName.setInvalidFontColor(color("#F47777"));
        packageName.setOutline(true);
        packageName.setLineSize(1);
        Predicate<String> predicate = Pattern.compile("^[^\\d_.][^.]*(\\.[^._][^.]*)*$").asPredicate();
        packageName.setValidator(string -> predicate.test(string));
        packageName.setSpaceAllowed(false);
        packageName.setFilter(character -> {
            if (!Character.isAlphabetic(character) && !(Character.isDigit(character) || character == '.' || character == '_')) {
                return true;
            }
            character = Character.toLowerCase(character);
            return character == 'ö' || character == 'ü' || character == 'ä';
        });
        packageName.setMapper(Character::toLowerCase);
        packageName.setLimit(35);
        pane.addChild(packageName);

        TextField directory = new TextField();
        directory.setX(10);
        directory.setY(180);
        directory.setWidth(610);
        directory.setHeight(32);
        directory.setFontSize(14);
        directory.setFontColor(Color.LIGHT_GRAY);
        directory.setBackground(color("#6D6D6D"));
        directory.setLine(color("#353737"));
        directory.setInvalidBackground(color("#6F3535"));
        directory.setInvalidLine(color("#353737"));
        directory.setInvalidFontColor(color("#F47777"));
        directory.setOutline(true);
        directory.setLineSize(1);
        directory.setLocked(true);
        directory.setValidator(string -> {
            File file = new File(string);
            return file.isDirectory() && file.exists();
        });
        pane.addChild(directory);

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
        openButton.setHeight(directory.getHeight());
        openButton.setX(630);
        openButton.setY(180);
        openButton.setAction(() -> {
            JnaFileChooser file = new JnaFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setMode(Mode.Directories);
            file.showOpenDialog(panel.getFrame());
            File selected = file.getSelectedFile();
            if (selected != null && selected.isDirectory()) {
                directory.setContent(selected.getPath());
            }
        });
        pane.addChild(openButton);

        int btnHeight = pane.getHeight() - directory.getHeight() - 20;
        Button createButton = new Button();
        createButton.setText("Erstellen");
        createButton.setTextCentered(true);
        createButton.setPress(color("#646363"));
        createButton.setShadow(color("#646363"));
        createButton.setHover(Color.DARK_GRAY, color("#353737"));
        createButton.setHoverFade(0.3, 0.125);
        createButton.setHoverShadow(Color.DARK_GRAY, color("#353737"));
        createButton.setHoverShadowFade(0.3, 0.125);
        createButton.setWidth(200);
        createButton.setHeight(directory.getHeight());
        createButton.setX(80);
        createButton.setY(btnHeight);
        createButton.setAction(() -> {
            if (!name.isValid() || !packageName.isValid() || !directory.isValid()) {
                return;
            }
            dialog.success(new ProjectInfo(name.getContent(), packageName.getContent(), directory.getContent()));
        });
        pane.addChild(createButton);

        Button abortButton = new Button();
        abortButton.setText("Abbrechen");
        abortButton.setTextCentered(true);
        abortButton.setPress(color("#646363"));
        abortButton.setShadow(color("#646363"));
        abortButton.setHover(Color.DARK_GRAY, color("#353737"));
        abortButton.setHoverFade(0.3, 0.125);
        abortButton.setHoverShadow(Color.DARK_GRAY, color("#353737"));
        abortButton.setHoverShadowFade(0.3, 0.125);
        abortButton.setWidth(200);
        abortButton.setHeight(directory.getHeight());
        abortButton.setX(pane.getWidth() - abortButton.getWidth() - createButton.getX());
        abortButton.setY(btnHeight);
        abortButton.setAction(() -> dialog.fail(null));
        pane.addChild(abortButton);
    }

}
