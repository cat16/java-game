package main.java.component;

import main.java.component.base.ComponentGroup;
import main.java.rendering.Mesh;

public class MeshGroup extends ComponentGroup<Mesh> {

    public MeshGroup() {
        super(ComponentType.MESH);
    }

    @Override
    public Mesh getDefault() {
        return null;
    }
}
