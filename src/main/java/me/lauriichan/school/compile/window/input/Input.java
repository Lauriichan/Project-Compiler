package me.lauriichan.school.compile.window.input;

public abstract class Input {
    
    private final InputProvider provider;
    
    public Input(InputProvider provider) {
        this.provider = provider;
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
