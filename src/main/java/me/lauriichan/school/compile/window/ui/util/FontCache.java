package me.lauriichan.school.compile.window.ui.util;

import java.awt.Font;
import java.util.HashMap;

public final class FontCache {

    public static final FontCache INSTANCE = new FontCache();

    public static Font get(String name) {
        return get(name, 12);
    }

    public static Font get(String name, int size) {
        return get(name, 0, size);
    }

    public static Font get(String name, int style, int size) {
        return INSTANCE.getOrBuild(name, style, size);
    }

    private final HashMap<String, Font> fonts = new HashMap<>();

    private FontCache() {}

    public Font getOrBuild(String name, int style, int size) {
        String combine = name + '#' + size + '$' + style;
        if (!fonts.containsKey(combine)) {
            Font font = new Font(name, style, size);
            fonts.put(combine, font);
            return font;
        }
        return fonts.get(combine);
    }

    public void clear() {
        fonts.clear();
    }

}
