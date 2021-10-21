package me.lauriichan.school.compile.window.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

public final class Area {

    private final Graphics2D graphics;

    private final Point size;
    private final Color color;

    private Color fontColor = Color.WHITE;

    public Area(Graphics2D graphics, Color color, int x, int y, int width, int height) {
        this.graphics = (x == -1 && y == -1) ? graphics : (Graphics2D) graphics.create(x, y, width, height);
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

    public void setFont(Font font) {
        graphics.setFont(font);
    }

    public Font getFont() {
        return graphics.getFont();
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

    public void drawText(int x, int y, String text) {
        drawText(x, y, text.toCharArray());
    }

    public void drawText(int x, int y, String text, Color color) {
        drawText(x, y, text.toCharArray(), color);
    }

    public void drawText(int x, int y, char[] text) {
        drawText(x, y, text, fontColor);
    }

    public void drawText(int x, int y, char[] text, Color color) {
        ArrayList<char[]> chars = metricText(size.getX() - x * 2, size.getY() - y, text);
        int height = graphics.getFontMetrics().getHeight();
        System.out.println(height);
        int base = (height / 2) + y;
        graphics.setPaint(color);
        for (int index = 0; index < chars.size(); index++) {
            char[] array = chars.get(index);
            graphics.drawChars(array, 0, array.length, x, base + (height * (index)));
        }
        graphics.setPaint(this.color);
    }

    private ArrayList<char[]> metricText(int sizeX, int sizeY, char[] text) {
        FontMetrics metrics = graphics.getFontMetrics();
        ArrayList<char[]> characters = new ArrayList<>();
        if (text.length == 0) {
            return characters;
        }
        int width = 0;
        int fontSize = metrics.getHeight();
        int height = fontSize;
        int last = 0;
        int wordBound = 0;
        for (int index = 0; index < text.length; index++) {
            char character = text[index];
            if (Character.isSpaceChar(character) || '\n' == character) {
                wordBound = index - 1;
            }
            width += metrics.charWidth(character);
            if (width > sizeX || '\n' == character) {
                if(width > sizeX) {
                    if(last - wordBound <= 1) {
                        wordBound = index - 1;
                    }
                }
                width = metrics.charWidth(character);
                int length = Math.abs(wordBound - last) + 1;
                char[] chars = new char[length];
                if (length > (text.length - last)) {
                    length = text.length - last;
                }
                if(last != 0 && Character.isWhitespace(text[last])) {
                    last += 1;
                }
                System.arraycopy(text, last, chars, 0, length);
                characters.add(chars);
                last = wordBound + 1;
                if (height + fontSize > sizeY) {
                    System.out.println("HMMM");
                    break;
                }
                height += fontSize;
            }
        }
        System.out.println(height + fontSize > sizeY);
        System.out.println(last != 0 && wordBound + 1 == text.length);
        if (height + fontSize > sizeY || (last != 0 && wordBound + 1 == text.length)) {
            return characters;
        }
        if (wordBound + 1 != text.length) {
            wordBound = text.length - 1;
        }
        int length = Math.abs(wordBound - last) + 1;
        if (length > (text.length - last)) {
            length = text.length - last;
        }
        char[] chars = new char[length];
        System.arraycopy(text, last, chars, 0, length);
        characters.add(chars);
        return characters;
    }

    public Area create(int x, int y, int width, int height) {
        return new Area(graphics, color, x, y, width, height);
    }

    public Color getColor() {
        return color;
    }

}
