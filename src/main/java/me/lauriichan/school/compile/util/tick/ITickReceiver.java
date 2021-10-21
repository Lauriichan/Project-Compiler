package me.lauriichan.school.compile.util.tick;

@FunctionalInterface
public interface ITickReceiver {
    
    void onTick(long deltaTime);

}
