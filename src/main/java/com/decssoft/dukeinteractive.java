package com.decssoft;

import com.almasb.fxgl.animation.AnimatedValue;
import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Camera3D;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.onKeyDown;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.animationBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.scene3d.CustomShape3D.MeshVertex;
import com.almasb.fxgl.scene3d.Model3D;
import com.decssoft.animation.AnimationSequence;
import com.decssoft.factory.GameFactory;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

/**
 *
 * @author Alan D.
 */
public class dukeinteractive extends GameApplication {

    private Camera3D camera;
    double distance = 4;
    Entity duke;
    private boolean isWaving = false;
    Animation anim1;

    private List<MeshVertex> verticesPos0;
    private List<MeshVertex> verticesPos1;
    private List<MeshVertex> verticesPos2;
    private AnimationSequence wave;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    protected void initGame() {
        camera = getGameScene().getCamera3D();
        getGameWorld().addEntityFactory(new GameFactory());
        duke = spawn("duke", 0, 0, 0);
        getGameScene().setFPSCamera(true);

        // get vertices for original position
        verticesPos0 = duke.getViewComponent().getChild(0, Model3D.class).getVertices();
        verticesPos1 = getAssetLoader().loadModel3D("dukewave1.obj").getVertices();
        verticesPos2 = getAssetLoader().loadModel3D("dukewave2.obj").getVertices();
        wave = new AnimationSequence(verticesPos0, Duration.ZERO, verticesPos1, verticesPos2);
        //load vertices for wave positio 1 and 2 (hand to right, hand to left)
        wave.setAnimation();
    }

    @Override
    protected void onUpdate(double tpf) {
        //handle camera position to be looking at Duke all the time and move around him
        double yset = distance * Math.sin(Math.toRadians(camera.getTransform().getRotationX())) + duke.getY() - .1;
        double dist = distance * Math.cos(Math.toRadians(camera.getTransform().getRotationX()));
        double zset = dist * Math.cos(Math.toRadians(camera.getTransform().getRotationY())) * -1 + duke.getZ();
        double xset = dist * Math.sin(Math.toRadians(camera.getTransform().getRotationY())) * -1 + duke.getX();
        camera.getTransform().setPosition3D(xset, yset, zset);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.set3D(true);
        settings.setWidth(1280);
        settings.setHeight(720);
    }

    @Override
    protected void initInput() {
        //handle zoom in and zoom out
        getGameScene().getRoot().addEventHandler(ScrollEvent.SCROLL, (s) -> {
            if (s.getDeltaY() < 0) {
                if (distance < 4) {
                    distance += .040;
                }
            }
            if (s.getDeltaY() > 0) {
                if (distance > 1.5) {
                    distance -= .040;
                }
            }
        });

        onKeyDown(KeyCode.A, "Wave", () -> {
            wave.play();
        });
//            Animation anim1 = animationBuilder()
//                    .duration(Duration.seconds(0.3))
//                    .animate(new AnimatedValue<>(0.0, 1.0))
//                    .onProgress(t -> {
//                        for (int i = 0; i < verticesPos0.size(); i++) {
//                            MeshVertex v = verticesPos0.get(i);
//                            MeshVertex from = verticesPos0.get(i);
//                            MeshVertex to = verticesPos1.get(i);
//
//                            double x = from.getX() * (1 - t) + to.getX() * t;
//                            double y = from.getY() * (1 - t) + to.getY() * t;
//                            double z = from.getZ() * (1 - t) + to.getZ() * t;
//
//                            v.setX(x);
//                            v.setY(y);
//                            v.setZ(z);
//                        }
//                    })
//                    .build();
//            animationBuilder()
//                    .duration(Duration.seconds(0.7))
//                    .animate(new AnimatedValue<>(0.0, 1.0))
//                    .onProgress(t -> {
//                        for (int i = 0; i < verticesPos0.size(); i++) {
//                            MeshVertex v = verticesPos0.get(i);
//                            MeshVertex from = verticesPos0.get(i);
//                            MeshVertex to = verticesPos1.get(i);
//
//                            double x = from.getX() * (1 - t) + to.getX() * t;
//                            double y = from.getY() * (1 - t) + to.getY() * t;
//                            double z = from.getZ() * (1 - t) + to.getZ() * t;
//
//                            v.setX(x);
//                            v.setY(y);
//                            v.setZ(z);
//                        }
//                    })
//                    .buildAndPlay();
//        });
        //            if (isWaving)
        //                return;
        //
        //            isWaving = true;
        //
        //            toPosition(verticesPos1, () -> {
        //                toPosition(verticesPos2, () -> {
        //                    isWaving = false;
        //                });
        //            });
        //        });
    }

    private void toPosition(List<MeshVertex> pos, Runnable onFinished) {
        Animation anim1 = animationBuilder()
                .onFinished(onFinished)
                .duration(Duration.seconds(0.3))
                .animate(new AnimatedValue<>(0.0, 1.0))
                .onProgress(t -> {
                    for (int i = 0; i < verticesPos0.size(); i++) {
                        MeshVertex v = verticesPos0.get(i);
                        MeshVertex from = verticesPos0.get(i);
                        MeshVertex to = pos.get(i);

                        double x = from.getX() * (1 - t) + to.getX() * t;
                        double y = from.getY() * (1 - t) + to.getY() * t;
                        double z = from.getZ() * (1 - t) + to.getZ() * t;

                        v.setX(x);
                        v.setY(y);
                        v.setZ(z);
                    }
                })
                .build();

        Animation anim2 = animationBuilder()
                .onFinished(onFinished)
                .duration(Duration.seconds(0.3))
                .animate(new AnimatedValue<>(0.0, 1.0))
                .onProgress(t -> {
                    for (int i = 0; i < verticesPos0.size(); i++) {
                        MeshVertex v = verticesPos0.get(i);
                        MeshVertex from = verticesPos0.get(i);
                        MeshVertex to = pos.get(i);

                        double x = from.getX() * (1 - t) + to.getX() * t;
                        double y = from.getY() * (1 - t) + to.getY() * t;
                        double z = from.getZ() * (1 - t) + to.getZ() * t;

                        v.setX(x);
                        v.setY(y);
                        v.setZ(z);
                    }
                })
                .build();
        animationBuilder().buildSequence(anim1, anim2);
    }
}
