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

    private Runnable action = null;

    private int textWidth;
    private int textHeight;
    private String textLine;
    private TextRender textRender;

    private String fontName = "Open Sans";
    private int fontSize = 12;
    private int fontStyle = 0;
    private Color fontColor = Color.WHITE;

    private Color press = Color.WHITE;
    private Color shadow = Color.BLACK;
    private int shadowThickness = 2;

    private boolean pressed = false;
    private boolean centerText = false;
    
    private boolean locked = false;

    @Override
    public void render(Area area) {
        renderBackground(area);
        renderText(area);
    }

    private void renderText(Area area) {
        if (textRender == null) {
            textRender = area.analyseText(0, 0, text, fontName, fontSize, fontStyle);
            textLine = textRender.getLines() == 0 ? "" : textRender.getLine(0);
            textWidth = textRender.getMetrics().stringWidth(textLine);
            textHeight = textRender.getHeight();
        }
        if (!centerText) {
            area.drawText(10, 12, textLine, fontColor, fontName, fontSize, fontStyle);
            return;
        }
        area.drawText((area.getWidth() - textWidth) / 2, (area.getHeight() - textHeight / 2) / 2, textLine, fontColor, fontName, fontSize,
            fontStyle);
    }

    private void renderBackground(Area area) {
        if (pressed) {
            area.fillShadow(press, shadowThickness, shadow);
            return;
        }
        area.fillShadow(hover.getValue(), shadowThickness, hoverShadow.getValue());
    }

    @Override
    public void update(long deltaTime) {
        hover.tick(deltaTime);
        hoverShadow.tick(deltaTime);
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public Runnable getAction() {
        return action;
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

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public Color getShadow() {
        return shadow;
    }

    public void setShadow(Color shadow) {
        this.shadow = shadow;
    }

    public Color getPress() {
        return press;
    }

    public void setPress(Color press) {
        this.press = press;
    }

    public int getShadowThickness() {
        return shadowThickness;
    }

    public void setShadowThickness(int shadowThickness) {
        this.shadowThickness = shadowThickness;
    }

    public void setHover(Color color) {
        setHover(color, color);
    }

    public void setHover(Color start, Color end) {
        hover.setStart(start);
        hover.setEnd(end);
    }

    public void setHoverFade(double fadeIn, double fadeOut) {
        hover.setFade(fadeIn, fadeOut);
    }

    public void setHoverShadow(Color color) {
        setHoverShadow(color, color);
    }

    public void setHoverShadow(Color start, Color end) {
        hoverShadow.setStart(start);
        hoverShadow.setEnd(end);
    }

    public void setHoverShadowFade(double fadeIn, double fadeOut) {
        hoverShadow.setFade(fadeIn, fadeOut);
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
        if(locked) {
            return;
        }
        pressed = true;
    }

    @Listener
    public void onRelease(MouseRelease release) {
        if (!pressed || release.getButton() != MouseButton.LEFT) {
            return;
        }
        release.consume();
        pressed = false;
        if (action != null) {
            action.run();
        }
    }

}
