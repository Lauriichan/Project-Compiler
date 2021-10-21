package me.lauriichan.school.compile.window.util.tick;

@FunctionalInterface
public interface ITickReceiver {
    
    void onTick(long deltaTime);

}
