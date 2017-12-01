package main.java.component;

import main.java.component.base.ComponentGroup;

public class PositionGroup extends ComponentGroup<Point> {
    public PositionGroup() {
        super(ComponentType.POSITION);
    }

    @Override
    public Point getDefault(){
        return new Point(0, 0);
    }
}
