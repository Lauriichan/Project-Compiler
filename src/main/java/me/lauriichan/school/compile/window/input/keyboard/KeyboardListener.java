package me.lauriichan.school.compile.window.input.keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import me.lauriichan.school.compile.window.input.InputProvider;

public final class KeyboardListener extends KeyAdapter {

    private final InputProvider provider;

    public KeyboardListener(InputProvider provider) {
        this.provider = provider;
    }

    public InputProvider getProvider() {
        return provider;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        event.consume();
        if (event.getKeyChar() == KeyEvent.CHAR_UNDEFINED) {
            return;
        }
        provider.receive(new KeyboardPress(provider, event.getKeyCode(), event.getKeyChar()), event);
    }

}
