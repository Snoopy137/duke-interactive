package com.decssoft.animation;

import com.almasb.fxgl.animation.AnimatedValue;
import com.almasb.fxgl.animation.Animation;
import static com.almasb.fxgl.dsl.FXGLForKtKt.animationBuilder;
import com.almasb.fxgl.scene3d.CustomShape3D.MeshVertex;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Duration;

/**
 *
 * @author Alan D.
 */
public class AnimationBuilder {

    public static Animation build(List<MeshVertex> startPosition, Duration duration, List<List<MeshVertex>> positions) {
        List<Animation<?>> animations = new ArrayList<>();

        for (List<MeshVertex> to : positions) {
            List<MeshVertex> from = new ArrayList<>(startPosition); // clone if needed

            Animation<?> anim = animationBuilder()
                    .duration(duration)
                    .animate(new AnimatedValue<>(0.0, 1.0))
                    .onProgress(t -> {
                        for (int i = 0; i < from.size(); i++) {
                            MeshVertex v = from.get(i);
                            MeshVertex a = from.get(i);
                            MeshVertex b = to.get(i);

                            v.setX(a.getX() * (1 - t) + b.getX() * t);
                            v.setY(a.getY() * (1 - t) + b.getY() * t);
                            v.setZ(a.getZ() * (1 - t) + b.getZ() * t);
                        }
                    })
                    .build();

            startPosition = to;
            animations.add(anim);
        }

        return animationBuilder().buildSequence(animations.toArray(new Animation<?>[0]));
    }
}
