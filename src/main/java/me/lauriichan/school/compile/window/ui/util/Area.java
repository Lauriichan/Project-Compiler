package me.lauriichan.school.compile.window.ui.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import me.lauriichan.school.compile.window.util.Point;

public final class Area {

    private static final String[] EMPTY_LINES = new String[0];
    private static final Integer[] EMPTY_INDICES = new Integer[0];

    private final Graphics2D graphics;

    private final Point size;
    private final Color color;

    private String fontName = "Open Sans";
    private Color fontColor = Color.WHITE;
    private int fontStyle = 0;
    private int fontSize = 0;

    public Area(Graphics2D graphics, Color color, int x, int y, int width, int height) {
        this.graphics = (x == -1 && y == -1) ? graphics : (Graphics2D) graphics.create(x, y, width, height);
        this.graphics.setBackground(color);
        this.graphics.setColor(color);
        this.color = color;
        this.size = new Point(width, height);
    }

    public int getWidth() {
        return size.getX();
    }

    public int getHeight() {
        return size.getY();
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontColor(Color color) {
        this.fontColor = color;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void clear() {
        graphics.clearRect(0, 0, getWidth(), getHeight());
    }

    public void fill(Color color) {
        drawRectangle(0, 0, getWidth(), getHeight(), color);
    }

    public void fillOutline(Color color, int thickness, Color outline) {
        drawRectangle(0, 0, getWidth(), getHeight(), outline);
        drawRectangle(thickness, thickness, getWidth() - thickness * 2, getHeight() - thickness * 2, color);
    }

    public void fillShadow(Color color, int thickness, Color shadow) {
        drawRectangle(0, 0, getWidth(), getHeight(), shadow);
        drawRectangle(0, 0, getWidth() - thickness, getHeight() - thickness, color);
    }

    public void drawLine(int x1, int y1, int x2, int y2, float thickness) {
        Stroke prev = graphics.getStroke();
        graphics.setStroke(new BasicStroke(thickness));
        graphics.drawLine(x1, y1, x2, y2);
        graphics.setStroke(prev);
    }

    public void drawLine(int x1, int y1, int x2, int y2, float thickness, Color color) {
        graphics.setPaint(color);
        drawLine(x1, y1, x2, y2, thickness);
        graphics.setPaint(this.color);
    }

    public void drawRectangle(int x, int y, int width, int height) {
        graphics.fillRect(x, y, width, height);
    }

    public void drawRectangle(int x, int y, int width, int height, Color color) {
        graphics.setPaint(color);
        graphics.fillRect(x, y, width, height);
        graphics.setPaint(this.color);
    }

    public void outlineRectangle(int x, int y, int width, int height) {
        graphics.drawRect(x, y, width, height);
    }

    public void outlineRectangle(int x, int y, int width, int height, Color color) {
        graphics.setPaint(color);
        graphics.drawRect(x, y, width, height);
        graphics.setPaint(this.color);
    }

    public TextRender drawText(int x, int y, String text) {
        return drawText(x, y, text.toCharArray(), FontCache.get(fontName, fontStyle, fontSize));
    }

    public TextRender drawText(int x, int y, String text, Color color) {
        return drawText(x, y, text.toCharArray(), color, FontCache.get(fontName, fontStyle, fontSize));
    }

    public TextRender drawText(int x, int y, char[] text) {
        return drawText(x, y, text, fontColor, FontCache.get(fontName, fontStyle, fontSize));
    }

    public TextRender drawText(int x, int y, char[] text, Color color) {
        return drawText(x, y, text, color, FontCache.get(fontName, fontStyle, fontSize));
    }

    public TextRender drawText(int x, int y, String text, int fontSize) {
        return drawText(x, y, text.toCharArray(), FontCache.get(fontName, fontSize));
    }

    public TextRender drawText(int x, int y, String text, Color color, int fontSize) {
        return drawText(x, y, text.toCharArray(), color, FontCache.get(fontName, fontSize));
    }

    public TextRender drawText(int x, int y, char[] text, int fontSize) {
        return drawText(x, y, text, fontColor, FontCache.get(fontName, fontSize));
    }

    public TextRender drawText(int x, int y, char[] text, Color color, int fontSize) {
        return drawText(x, y, text, color, FontCache.get(fontName, fontSize));
    }

    public TextRender drawText(int x, int y, String text, String fontName, int fontSize) {
        return drawText(x, y, text.toCharArray(), FontCache.get(fontName, fontSize));
    }

    public TextRender drawText(int x, int y, String text, Color color, String fontName, int fontSize) {
        return drawText(x, y, text.toCharArray(), color, FontCache.get(fontName, fontSize));
    }

    public TextRender drawText(int x, int y, char[] text, String fontName, int fontSize) {
        return drawText(x, y, text, fontColor, FontCache.get(fontName, fontSize));
    }

    public TextRender drawText(int x, int y, char[] text, Color color, String fontName, int fontSize) {
        return drawText(x, y, text, color, FontCache.get(fontName, fontSize));
    }

    public TextRender drawText(int x, int y, String text, String fontName, int fontSize, int fontStyle) {
        return drawText(x, y, text.toCharArray(), FontCache.get(fontName, fontStyle, fontSize));
    }

    public TextRender drawText(int x, int y, String text, Color color, String fontName, int fontSize, int fontStyle) {
        return drawText(x, y, text.toCharArray(), color, FontCache.get(fontName, fontStyle, fontSize));
    }

    public TextRender drawText(int x, int y, char[] text, String fontName, int fontSize, int fontStyle) {
        return drawText(x, y, text, fontColor, FontCache.get(fontName, fontStyle, fontSize));
    }

    public TextRender drawText(int x, int y, char[] text, Color color, String fontName, int fontSize, int fontStyle) {
        return drawText(x, y, text, color, FontCache.get(fontName, fontStyle, fontSize));
    }

    public TextRender drawText(int x, int y, String text, Font font) {
        return drawText(x, y, text.toCharArray(), fontColor, font);
    }

    public TextRender drawText(int x, int y, String text, Color color, Font font) {
        return drawText(x, y, text.toCharArray(), color, font);
    }

    public TextRender drawText(int x, int y, char[] text, Font font) {
        return drawText(x, y, text, fontColor, font);
    }

    public TextRender drawText(int x, int y, char[] text, Color color, Font font) {
        graphics.setFont(font);
        TextRender render = metricText(size.getX() - x * 2, size.getY() - y, text);
        int base = (render.getHeight() / 2) + y;
        graphics.setPaint(color);
        for (int index = 0; index < render.getLines(); index++) {
            graphics.drawString(render.getLine(index), x, base + (render.getHeight() * (index)));
        }
        graphics.setPaint(this.color);
        return render;
    }

    public TextRender analyseText(int x, int y, String text) {
        return analyseText(x, y, text.toCharArray());
    }

    public TextRender analyseText(int x, int y, String text, int fontSize) {
        return analyseText(x, y, text.toCharArray(), fontSize);
    }

    public TextRender analyseText(int x, int y, String text, String fontName, int fontSize) {
        return analyseText(x, y, text.toCharArray(), fontName, fontSize);
    }

    public TextRender analyseText(int x, int y, String text, String fontName, int fontSize, int fontStyle) {
        return analyseText(x, y, text.toCharArray(), fontName, fontSize, fontStyle);
    }

    public TextRender analyseText(int x, int y, String text, Font font) {
        return analyseText(x, y, text.toCharArray(), font);
    }

    public TextRender analyseText(int x, int y, char[] text) {
        return analyseText(x, y, text, FontCache.get(fontName, fontStyle, fontSize));
    }

    public TextRender analyseText(int x, int y, char[] text, int fontSize) {
        return analyseText(x, y, text, FontCache.get(fontName, fontSize));
    }

    public TextRender analyseText(int x, int y, char[] text, String fontName, int fontSize) {
        return analyseText(x, y, text, FontCache.get(fontName, fontSize));
    }

    public TextRender analyseText(int x, int y, char[] text, String fontName, int fontSize, int fontStyle) {
        return analyseText(x, y, text, FontCache.get(fontName, fontStyle, fontSize));
    }

    public TextRender analyseText(int x, int y, char[] text, Font font) {
        graphics.setFont(font);
        return metricText(size.getX() - x * 2, size.getY() - y, text);
    }

    private TextRender metricText(int sizeX, int sizeY, char[] text) {
        FontMetrics metrics = graphics.getFontMetrics();
        if (text.length == 0) {
            return new TextRender(EMPTY_LINES, EMPTY_INDICES, metrics);
        }
        ArrayList<String> characters = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        int width = 0;
        int last = 0;
        int wordBound = 0;
        for (int index = 0; index < text.length; index++) {
            char character = text[index];
            if (Character.isSpaceChar(character) || '\n' == character) {
                wordBound = index - 1;
            }
            width += metrics.charWidth(character);
            if ((width <= sizeX && '\n' != character)) {
                continue;
            }
            if (width > sizeX) {
                if (last - wordBound <= 1) {
                    wordBound = index - 1;
                }
            }
            width = metrics.charWidth(character);
            int length = Math.abs(wordBound - last) + 1;
            if (last == 0 && Character.isWhitespace(text[last]) && length <= 2) {
                length = 0;
            }
            if (length > (text.length - last)) {
                length = text.length - last;
            }
            if (last != 0 && Character.isWhitespace(text[last])) {
                last += 1;
            }
            char[] chars = new char[length];
            System.arraycopy(text, last, chars, 0, length);
            characters.add(new String(chars));
            indices.add(last);
            last = wordBound + 1;
        }
        if ((last != 0 && wordBound + 1 == text.length)) {
            return new TextRender(characters.toArray(new String[characters.size()]), indices.toArray(new Integer[indices.size()]), metrics);
        }
        if (wordBound + 1 != text.length) {
            wordBound = text.length - 1;
        }
        int length = Math.abs(wordBound - last) + 1;
        if (length > (text.length - last)) {
            length = text.length - last + 1;
        }
        char[] chars = new char[length];
        System.arraycopy(text, last, chars, 0, length);
        String string = new String(chars);
        characters.add(string);
        indices.add(indices.contains(last) ? last + 1 : last);
        return new TextRender(characters.toArray(new String[characters.size()]), indices.toArray(new Integer[indices.size()]), metrics);
    }

    public Area create(int x, int y, int width, int height) {
        return new Area(graphics, color, x, y, width, height);
    }

    public Color getColor() {
        return color;
    }

}
