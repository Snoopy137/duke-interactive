package com.decssoft.animation;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.scene3d.CustomShape3D.MeshVertex;
import java.util.Arrays;
import java.util.List;
import javafx.util.Duration;

/**
 *
 * @author Alan D.
 */
public class AnimationSequence extends AnimationFactory {

    private final List<MeshVertex> start;
    private final Duration duration;
    private final List<List<MeshVertex>> frames;

    public AnimationSequence(List<MeshVertex> startPosition, Duration duration, List<MeshVertex>... positions) {
        this.start = startPosition;
        this.duration = duration;
        this.frames = Arrays.asList(positions);
    }

    @Override
    protected Animation buildAnimation() {
        return AnimationBuilder.build(start, duration, frames);
    }

    public void setAnimation() {
        animation = buildAnimation();
    }
}
