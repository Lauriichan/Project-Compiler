package me.lauriichan.school.compile.window.ui.component;

import java.awt.Color;

import me.lauriichan.school.compile.window.input.Listener;
import me.lauriichan.school.compile.window.input.mouse.MouseButton;
import me.lauriichan.school.compile.window.input.mouse.MouseHover;
import me.lauriichan.school.compile.window.input.mouse.MousePress;
import me.lauriichan.school.compile.window.input.mouse.MouseRelease;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.animation.Animators;
import me.lauriichan.school.compile.window.ui.animation.FadeAnimation;
import me.lauriichan.school.compile.window.ui.util.Area;
import me.lauriichan.school.compile.window.ui.util.InputHelper;
import me.lauriichan.school.compile.window.ui.util.TextRender;

public final class Button extends Component {

    private final FadeAnimation<Color> hover = new FadeAnimation<>(Animators.COLOR);
    private final FadeAnimation<Color> hoverShadow = new FadeAnimation<>(Animators.COLOR);

    private String text = "";

    private int textWidth;
    private int textHeight;
    private String textLine;
    private TextRender textRender;

    private String fontName;
    private int fontSize;
    private int fontStyle;
    private Color fontColor;

    private Color press = Color.WHITE;
    private Color shadow = Color.BLACK;
    private int shadowThickness = 2;

    private boolean pressed = false;
    private boolean centerText = false;

    @Override
    protected void render(Area area) {
        renderBackground(area);
        renderText(area);
    }

    private void renderText(Area area) {
        if (textRender == null) {
            textRender = area.analyseText(0, 0, text, fontName, fontSize, fontStyle);
            textLine = textRender.getLine(0);
            textWidth = textRender.getMetrics().stringWidth(textLine);
            textHeight = textRender.getHeight();
        }
        if (centerText) {
            area.drawText(10, 12, textLine, fontColor, fontName, fontSize, fontStyle);
            return;
        }
        area.drawText((getWidth() - textWidth) / 2, (getHeight() - textHeight) / 2, textLine, fontColor, fontName, fontSize, fontStyle);
    }

    private void renderBackground(Area area) {
        if (pressed) {
            area.fillShadow(press, shadowThickness, shadow);
            return;
        }
        area.fillShadow(hover.getValue(), shadowThickness, hoverShadow.getValue());
    }

    @Override
    protected void update(long deltaTime) {
        hover.tick(deltaTime);
    }

    public void setTextCentered(boolean centered) {
        this.centerText = centered;
    }

    public boolean isTextCentered() {
        return centerText;
    }

    public String getShownText() {
        return textLine;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = (text == null ? "" : text);
        textRender = null;
    }
    
    
    /*
     * 
     */

    @Listener
    public void onMove(MouseHover hover) {
        InputHelper.hover(hover, this, hoverShadow);
        this.hover.setTriggered(hoverShadow.isTriggered());
    }

    @Listener
    public void onPress(MousePress press) {
        if (!isInside(press.getX(), press.getY()) || press.getButton() != MouseButton.LEFT) {
            return;
        }
        press.consume();
        pressed = true;
    }

    @Listener
    public void onRelease(MouseRelease release) {
        if (!pressed || release.getButton() != MouseButton.LEFT) {
            return;
        }
        release.consume();
        pressed = false;
    }

}
