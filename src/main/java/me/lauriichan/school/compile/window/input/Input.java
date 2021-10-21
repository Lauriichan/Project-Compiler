package me.lauriichan.school.compile.window.input;

public abstract class Input {

    private final InputProvider provider;
    private boolean consumed = false;

    public Input(InputProvider provider) {
        this.provider = provider;
    }

    public void consume() {
        consumed = true;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public InputProvider getProvider() {
        return provider;
    }

    public boolean isShiftDown() {
        return provider.isShiftDown();
    }

    public boolean isAltDown() {
        return provider.isAltDown();
    }

    public boolean isControlDown() {
        return provider.isControlDown();
    }

}
