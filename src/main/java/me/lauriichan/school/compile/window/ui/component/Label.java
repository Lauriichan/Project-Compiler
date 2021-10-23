package me.lauriichan.school.compile.window.ui.component;

import java.awt.Color;

import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.util.Area;
import me.lauriichan.school.compile.window.ui.util.TextRender;

public final class Label extends Component {

    private String text = "";

    private int textWidth;
    private int textHeight;
    private String textLine;
    private TextRender textRender;

    private String fontName = "Open Sans";
    private int fontSize = 12;
    private int fontStyle = 0;
    private Color fontColor = Color.WHITE;

    private boolean centerText = false;

    @Override
    public void render(Area area) {
        if (textRender == null) {
            textRender = area.analyseText(0, 0, text, fontName, fontSize, fontStyle);
            textLine = textRender.getLines() == 0 ? "" : textRender.getLine(0);
            textWidth = textRender.getMetrics().stringWidth(textLine);
            textHeight = textRender.getHeight();
        }
        if (!centerText) {
            area.drawText(0, 12, textLine, fontColor, fontName, fontSize, fontStyle);
            return;
        }
        area.drawText((area.getWidth() - textWidth) / 2, (area.getHeight() - textHeight / 2) / 2, textLine, fontColor, fontName, fontSize,
            fontStyle);
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

}
