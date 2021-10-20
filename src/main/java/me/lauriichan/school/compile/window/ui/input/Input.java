package me.lauriichan.school.compile.window.ui.input;

public abstract class Input {
    
    private final InputProvider provider;
    
    public Input(InputProvider provider) {
        this.provider = provider;
    }
    
    public InputProvider getProvider() {
        return provider;
    }

}
