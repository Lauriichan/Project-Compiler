package me.lauriichan.school.compile.window.ui.animation;

import me.lauriichan.school.compile.window.ui.animation.util.TimeHelper;

public final class BlinkAnimation<E> implements IAnimation<E> {

    private E valueOn;
    private E valueOff;
    private E valueHidden;

    private E current;

    private double blinkOn = 0;
    private double blinkOff = 0;
    private double blinkRatio = 0;

    private double blinkTime = 0;
    private boolean stay = false;

    private boolean triggered = false;

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
        return null;
    }

    @Override
    public void tick(long deltaTime) {
        if (!triggered) {
            if (current == valueHidden) {
                return;
            }
            current = valueHidden;
            blinkTime = 0;
            stay = false;
            return;
        }
        double second = TimeHelper.nanoAsSecond(deltaTime);
        if (stay) {
            if (blinkOn == 0 && current != valueOn) {
                current = valueOn;
                blinkTime = 0;
            }
            if (blinkTime == blinkOn) {
                stay = false;
                return;
            }
            blinkTime += Math.min(blinkOn - blinkTime, second);
            current = valueOn;
            return;
        }
        if (blinkOff == 0 && current != valueOff) {
            current = valueOn;
            blinkTime = 0;
        }
        if (blinkTime == 0) {
            stay = true;
            return;
        }
        blinkTime -= Math.min(blinkTime, second / blinkRatio);
        current = valueOff;
    }

    @Override
    public E getValue() {
        return current;
    }

    public void setBlink(double blinkOn, double blinkOff) {
        this.blinkOn = Math.max(0, blinkOn);
        this.blinkOff = Math.max(0, blinkOff);
        updateBlink();
    }

    public void setBlinkOn(double blinkOn) {
        this.blinkOn = Math.max(0, blinkOn);
        updateBlink();
    }

    public void setBlinkOff(double blinkOff) {
        this.blinkOff = Math.max(0, blinkOff);
        updateBlink();
    }

    private void updateBlink() {
        if (blinkOn == 0) {
            blinkRatio = 1;
            return;
        }
        this.blinkRatio = blinkOff / blinkOn;
    }

    @Override
    public void setStart(E value) {
        this.valueOn = value;
        if (current == null) {
            this.current = value;
        }
    }

    @Override
    public void setEnd(E value) {
        this.valueOff = value;
    }

    public void setHidden(E valueHidden) {
        this.valueHidden = valueHidden;
    }

}