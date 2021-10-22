package me.lauriichan.school.compile.window.view;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import me.lauriichan.school.compile.project.template.Template;
import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.component.Button;

public final class TemplateView extends View {

    private int buttonId = 0;

    public TemplateView() {
        super("Templates");
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        int amount = Template.TEMPLATES.size();
        int possible = 15;
        for (int index = 1; index < amount; index++) {
            Template template = Template.TEMPLATES.get(index);
            if (template.isHidden()) {
                continue;
            }
            Button button = createTemplateButton();
            button.setText(template.getName());
            pane.addChild(button);
            possible--;
        }
        while (possible-- != 0) {
            pane.addChild(createTemplateButton());
        }
        int offset = width / 16;
        Button random = createButton(offset, (height / 3) * 2 + offset, (width / 4) * 3 + offset * 2, offset);
        random.setText(Template.TEMPLATES.get(0).getName());
        pane.addChild(random);
    }

    private Button createTemplateButton() {
        if (buttonId == 15) {
            throw new IllegalStateException("There can't be more than 15 buttons of this Type");
        }
        int id = buttonId++;
        int size = pane.getWidth() / 4;
        int half = size / 8;
        Button button = createButton();
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

}
