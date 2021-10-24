package me.lauriichan.school.compile.window.ui.component;

import java.awt.Color;
import java.awt.FontMetrics;

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
    
    private int textOffset = 0;

    private boolean centerText = false;
    private boolean allowMultiline = false;

    @Override
    public void render(Area area) {
        if (allowMultiline) {
            renderMultiText(area);
            return;
        }
        renderSingleText(area);
    }

    private void renderMultiText(Area area) {
        if (textRender == null) {
            textRender = area.analyseText(0, 0, text, fontName, fontSize, fontStyle);
            textHeight = textRender.getHeight();
        }
        int amount = textRender.getLines();
        if (amount == 0) {
            return;
        }
        FontMetrics metrics = textRender.getMetrics();
        for (int index = 0; index < textRender.getLines(); index++) {
            String line = textRender.getLine(index);
            if(line.trim().isEmpty()) {
                continue;
            }
            int y = (textHeight + textOffset) * index + 6;
            if (!centerText) {
                area.drawText(0, y, line, fontColor, fontName, fontSize, fontStyle);
                continue;
            }
            area.drawText((area.getWidth() - metrics.stringWidth(line)) / 2, y, line, fontColor, fontName, fontSize, fontStyle);
        }
    }

    private void renderSingleText(Area area) {
        if (textRender == null) {
            textRender = area.analyseWrappedText(0, 0, text, fontName, fontSize, fontStyle);
            textLine = textRender.getLines() == 0 ? "" : textRender.getLine(0);
            textWidth = textRender.getMetrics().stringWidth(textLine);
            textHeight = textRender.getHeight();
        }
        if (!centerText) {
            area.drawText(0, 12, textLine, fontColor, fontName, fontSize, fontStyle);
            return;
        }
        area.drawText((area.getWidth() - textWidth) / 2, (area.getHeight() - textHeight / 2) / 2, textLine, fontColor, fontName,
            fontSize, fontStyle);
    }

    public void setTextOffset(int textOffset) {
        this.textOffset = textOffset;
    }

    public int getTextOffset() {
        return textOffset;
    }

    public boolean isMultilineAllowed() {
        return allowMultiline;
    }

    public void setMultilineAllowed(boolean allowMultiline) {
        this.allowMultiline = allowMultiline;
        textRender = null;
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
