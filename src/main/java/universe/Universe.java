package main.java.universe;

import main.java.component.ComponentType;
import main.java.component.MeshGroup;
import main.java.component.Point;
import main.java.component.PositionGroup;
import main.java.component.base.ComponentGroupIterator;
import main.java.component.base.ComponentGroupManager;
import main.java.rendering.Mesh;
import main.java.util.TextureLoader;

public class Universe {

    private ComponentGroupManager componentManager;

    public Universe() {
        this.componentManager = new ComponentGroupManager();
    }

    public ComponentGroupIterator createGroupIterator(String base, String[] keys, String[] required) {
        return componentManager.createGroupIterator(base, keys, required);
    }

    public void init(){
        componentManager
                .add(new PositionGroup())
                .add(new MeshGroup())
        ;
        componentManager.get(ComponentType.POSITION).addComponent(0, new Point(0, 0));
        componentManager.get(ComponentType.MESH).addComponent(0, new Mesh(
                new float[]{
                        -0.5f, -0.5f,
                        -0.5f, 0.5f,
                        0.5f, 0.5f,
                        0.5f, -0.5f
                },
                new float[]{
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f
                },
                new int[]{
                        0, 1, 2,
                        0, 2, 3
                },
                TextureLoader.getTexture("test")
        ));
    }

    public void cleanUp(){
        for(Object obj : componentManager.get(ComponentType.MESH).getCompoents()){
            Mesh m = (Mesh)obj;
            m.cleanUp();
        }
    }
}
