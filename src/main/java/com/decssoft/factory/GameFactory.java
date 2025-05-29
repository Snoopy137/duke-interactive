package com.decssoft.factory;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.scene3d.Model3D;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.shape.Box;

/**
 *
 * @author Alan D.
 */
public class GameFactory implements EntityFactory {

    @Spawns("duke")
    public Entity duke(SpawnData data) throws FileNotFoundException, IOException {
        var view = new Box(3, 4, 3);
        Model3D object = getAssetLoader().loadModel3D("duke.obj");
        object.setScaleX(10);
        object.setScaleY(10);
        object.setScaleZ(10);
        return entityBuilder(data)
                .type(EntityType.DUKE)
                .bbox(BoundingShape.box3D(1.5, 4.5, 1))
                .view(object)
                .collidable()
                .buildAndAttach();
    }

}
