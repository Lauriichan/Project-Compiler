package me.lauriichan.school.compile.window.ui.util;

import java.awt.Color;
import java.util.HashMap;

public final class ColorCache {

    public static final ColorCache INSTANCE = new ColorCache();

    public static Color color(String hex) {
        return color(hex, 255);
    }

    public static Color color(String hex, int alpha) {
        return INSTANCE.getOrBuild(hex, alpha);
    }

    private final HashMap<String, Color> colors = new HashMap<>();

    private ColorCache() {}

    public Color getOrBuild(String hex, int alpha) {
        String combine = (hex.startsWith("#") ? hex.substring(1) : hex) + '$' + alpha;
        if (!colors.containsKey(combine)) {
            Color color = Color.decode('#' + hex);
            color = new Color(color.getRed(), color.getBlue(), color.getBlue(), alpha);
            colors.put(combine, color);
            return color;
        }
        return colors.get(combine);
    }

    public void clear() {
        colors.clear();
    }

}
