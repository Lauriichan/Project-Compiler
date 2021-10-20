package me.lauriichan.school.compile.window.ui.util;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public final class Area {

    public static final Color DEFAULT = Color.BLACK;

    private final Graphics2D graphics;

    private final Point position;
    private final Point size;

    public Area(Graphics2D graphics, int x, int y, int width, int height) {
        this.graphics = (x == -1 && y == -1) ? graphics : (Graphics2D) graphics.create(x, y, width, height);
        this.graphics.setColor(DEFAULT);
        this.position = new Point(Math.max(0, x), Math.max(0, y));
        this.size = new Point(width, height);
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
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

    public void drawRectangle(int x, int y, int width, int height) {
        if (x + width > size.getX()) {
            width = size.getX() - x;
        }
        if (y + height > size.getY()) {
            height = size.getY() - y;
        }
        graphics.fillRect(x, y, width, height);
    }

    public void drawRectangle(int x, int y, int width, int height, Color color) {
        if (x + width > size.getX()) {
            width = size.getX() - x;
        }
        if (y + height > size.getY()) {
            height = size.getY() - y;
        }
        graphics.setPaint(color);
        graphics.fillRect(x, y, width, height);
        graphics.setPaint(DEFAULT);
    }

    public void outlineRectangle(int x, int y, int width, int height) {
        if (x + width > size.getX()) {
            width = size.getX() - x;
        }
        if (y + height > size.getY()) {
            height = size.getY() - y;
        }
        graphics.drawRect(x, y, width, height);
    }

    public void outlineRectangle(int x, int y, int width, int height, Color color) {
        if (x + width > size.getX()) {
            width = size.getX() - x;
        }
        if (y + height > size.getY()) {
            height = size.getY() - y;
        }
        graphics.setPaint(color);
        graphics.drawRect(x, y, width, height);
        graphics.setPaint(DEFAULT);
    }

    public void drawText(int x, int y, String text) {
        drawText(x, y, text.toCharArray());
    }

    public void drawText(int x, int y, String text, Color color) {
        drawText(x, y, text.toCharArray(), color);
    }

    public void drawText(int x, int y, char[] text) {
        ArrayList<char[]> chars = metricText(size.getX() - x, size.getY() - y, text);
        int height = graphics.getFontMetrics().getHeight();
        for (int index = 0; index < chars.size(); index++) {
            graphics.drawString(new String(chars.get(index)), x, y + (height * index));
        }
    }

    public void drawText(int x, int y, char[] text, Color color) {
        ArrayList<char[]> chars = metricText(size.getX() - x, size.getY() - y, text);
        int height = graphics.getFontMetrics().getHeight();
        graphics.setPaint(color);
        for (int index = 0; index < chars.size(); index++) {
            graphics.drawString(new String(chars.get(index)), x, y + (height * index));
        }
        graphics.setPaint(DEFAULT);
    }

    private ArrayList<char[]> metricText(int sizeX, int sizeY, char[] text) {
        FontMetrics metrics = graphics.getFontMetrics();
        ArrayList<char[]> characters = new ArrayList<>();
        int width = 0;
        int fontSize = metrics.getHeight();
        int height = fontSize;
        int last = 0;
        int wordBound = 0;
        for (int index = 0; index < text.length; index++) {
            char character = text[index];
            if (Character.isSpaceChar(character)) {
                wordBound = index - 1;
            }
            width += metrics.charWidth(character);
            if (width > sizeX) {
                width = metrics.charWidth(character);
                int length = wordBound - last;
                char[] chars = new char[length];
                System.arraycopy(characters, last, chars, wordBound, length);
                characters.add(chars);
                last = wordBound + 1;
                if (height + fontSize > sizeY) {
                    break;
                }
                height += fontSize;
            }
        }
        if (wordBound + 1 == last || height + fontSize > sizeY) {
            return characters;
        }
        int length = wordBound - last;
        char[] chars = new char[length];
        System.arraycopy(characters, last, chars, wordBound, length);
        characters.add(chars);
        return characters;
    }

    public Area create(int x, int y, int width, int height) {
        return new Area(graphics, x, y, width, height);
    }

}
