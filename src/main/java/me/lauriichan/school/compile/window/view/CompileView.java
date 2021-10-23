package me.lauriichan.school.compile.window.view;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;

import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.component.Button;

public final class CompileView extends View {

    private int buttonId = 0;

    public CompileView() {
        super("Compiler");
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        Button btnOpen = createButton();
        btnOpen.setText("Ordner öffnen");
        pane.addChild(btnOpen);

        Button btnCompile = createButton();
        btnCompile.setText("Kompilieren");
        pane.addChild(btnCompile);

        Button btnExecute = createButton();
        btnExecute.setText("Ausführen");
        pane.addChild(btnExecute);
    }

    private Button createButton() {
        if (buttonId == 3) {
            throw new IllegalStateException("There can't be more than 3 buttons of this Type");
        }
        int id = buttonId++;
        int offset = 20;
        int size = (pane.getWidth() - offset * 4) / 3;
        Button button = new Button();
        button.setPress(color("#646363"));
        button.setShadow(color("#646363"));
        button.setHover(Color.DARK_GRAY, color("#353737"));
        button.setHoverFade(0.3, 0.125);
        button.setHoverShadow(Color.DARK_GRAY, color("#353737"));
        button.setHoverShadowFade(0.3, 0.125);
        button.setTextCentered(true);
        button.setSize(size, 32);
        button.setPosition(offset + (size + offset) * id, offset);
        return button;
    }

}
