package com.decssoft;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Camera3D;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.scene3d.CustomShape3D.MeshVertex;
import com.almasb.fxgl.scene3d.Model3D;
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

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    protected void initGame() {
        camera = getGameScene().getCamera3D();
        getGameWorld().addEntityFactory(new GameFactory());
        duke = spawn("duke", 0, 0, 0);
        getGameScene().setFPSCamera(true);
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

        getInput().addAction(new UserAction("wave") {
            List<MeshVertex> hi;
            List<MeshVertex> hi1;
            List<MeshVertex> original;

            @Override
            protected void onActionBegin() {
                //load vertices for wave positio 1 and 2 (hand to right, hand to left)
                hi = getAssetLoader().loadModel3D("dukewave1.obj").getVertices();
                hi1 = getAssetLoader().loadModel3D("dukewave2.obj").getVertices();
                // get vertices for original position
                original = duke.getViewComponent().getChild(0, Model3D.class).getVertices();
            }

            @Override
            protected void onAction() {
                final double[] t = {0.0};
                final double speed = 0.02;

                //moving Duke to position 1 by translating vertices
                getGameTimer().runAtInterval(() -> {
                    for (int i = 0; i < original.size(); i++) {
                        MeshVertex v = original.get(i);
                        MeshVertex from = original.get(i);
                        MeshVertex to = hi.get(i);

                        double x = from.getX() * (1 - t[0]) + to.getX() * t[0];
                        double y = from.getY() * (1 - t[0]) + to.getY() * t[0];
                        double z = from.getZ() * (1 - t[0]) + to.getZ() * t[0];

                        v.setX(x);
                        v.setY(y);
                        v.setZ(z);
                    }

                    t[0] += speed;

                    if (t[0] >= 1.0) {
                        t[0] = 1.0;
                    }
                }, Duration.seconds(1.0 / 60), 1);

                // Schedule next animation immediately after this one ends
                // moving duke to position 2 by translating vertices
                getGameTimer().runOnceAfter(() -> {
                    for (int i = 0; i < original.size(); i++) {
                        MeshVertex v = original.get(i);
                        MeshVertex from = original.get(i);
                        MeshVertex to = hi1.get(i);

                        double x = from.getX() * (1 - t[0]) + to.getX() * t[0];
                        double y = from.getY() * (1 - t[0]) + to.getY() * t[0];
                        double z = from.getZ() * (1 - t[0]) + to.getZ() * t[0];

                        v.setX(x);
                        v.setY(y);
                        v.setZ(z);
                    }

                    t[0] += speed;

                    if (t[0] >= 1.0) {
                        t[0] = 1.0;
                        System.out.println("second complete");
                    }
                }, Duration.seconds(1.0 / 60));
            }
        }, KeyCode.A);
    }
}
