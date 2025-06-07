package com.decssoft.animation;

import com.almasb.fxgl.animation.Animation;

/**
 *
 * @author Alan D.
 */
public abstract class AnimationFactory {

    protected Animation animation;

    // This must be implemented by subclasses
    protected abstract Animation buildAnimation();

    public void play() {
        System.out.println("animating");
        if (animation != null) {
            animation.start();
        }
    }

    public void stop() {
        if (animation != null) {
            animation.stop();
        }
    }

    public void pause() {
        if (animation != null) {
            animation.pause();
        }
    }

    public boolean isRunning() {
        return animation != null && animation.isAnimating();
    }
}
