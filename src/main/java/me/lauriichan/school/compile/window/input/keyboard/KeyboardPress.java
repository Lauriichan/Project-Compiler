package me.lauriichan.school.compile.window.input.keyboard;

import java.awt.event.KeyEvent;

import me.lauriichan.school.compile.window.input.Input;
import me.lauriichan.school.compile.window.input.InputProvider;

public class KeyboardPress extends Input {

    private final int code;
    private final char character;

    public KeyboardPress(InputProvider provider, int code, char character) {
        super(provider);
        this.code = code;
        this.character = character;
    }

    public int getCode() {
        return code;
    }
    
    public boolean hasChar() {
        return character != KeyEvent.CHAR_UNDEFINED;
    }

    public char getChar() {
        return character;
    }

}
