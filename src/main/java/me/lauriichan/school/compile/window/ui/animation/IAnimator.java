package me.lauriichan.school.compile.window.ui.animation;

@FunctionalInterface
public interface IAnimator<E> {
    
    E update(E start, E end, double percentage);
    
}
