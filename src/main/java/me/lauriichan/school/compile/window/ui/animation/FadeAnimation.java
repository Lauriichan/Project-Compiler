package me.lauriichan.school.compile.window.ui.animation;

import me.lauriichan.school.compile.window.ui.animation.util.TimeHelper;

public final class FadeAnimation<E> implements IAnimation<E> {

    private final IAnimator<E> animator;

    private E start;
    private E end;

    private E current;

    private double fadeIn = 0;
    private double fadeOut = 0;
    private double fadeRatio = 0;

    private double fadeTime = 0;
    private boolean triggered = false;

    public FadeAnimation(IAnimator<E> animator) {
        this.animator = animator;
    }

    @Override
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    @Override
    public boolean isTriggered() {
        return triggered;
    }

    @Override
    public IAnimator<E> getAnimator() {
        return animator;
    }

    @Override
    public void tick(long deltaTime) {
        double second = TimeHelper.nanoAsSecond(deltaTime);
        if (triggered) {
            if (fadeIn == 0 && current != start) {
                current = start;
                fadeTime = 0;
                return;
            }
            if (fadeTime == fadeIn) {
                return;
            }
            fadeTime += Math.min(fadeIn - fadeTime, second);
            current = animator.update(start, end, fadeTime / fadeIn);
            return;
        }
        if (fadeOut == 0 && current != end) {
            current = end;
            fadeTime = 0;
            return;
        }
        if (fadeTime == 0) {
            return;
        }
        fadeTime -= Math.min(fadeTime, second / fadeRatio);
        current = animator.update(start, end, fadeTime / (fadeOut / fadeRatio));
    }

    @Override
    public E getValue() {
        return current;
    }

    public void setFade(double fadeIn, double fadeOut) {
        this.fadeIn = Math.max(0, fadeIn);
        this.fadeOut = Math.max(0, fadeOut);
        updateFade();
    }

    public void setFadeIn(double fadeIn) {
        this.fadeIn = Math.max(0, fadeIn);
        updateFade();
    }

    public void setFadeOut(double fadeOut) {
        this.fadeOut = Math.max(0, fadeOut);
        updateFade();
    }

    private void updateFade() {
        if (fadeIn == 0) {
            fadeRatio = 1;
            return;
        }
        this.fadeRatio = fadeOut / fadeIn;
    }

    @Override
    public void setStart(E value) {
        this.start = value;
        if (current == null) {
            this.current = value;
        }
    }

    @Override
    public void setEnd(E value) {
        this.end = value;
    }

}
