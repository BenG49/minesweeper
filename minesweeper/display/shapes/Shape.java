package minesweeper.display.shapes;

import java.util.List;

public abstract class Shape {
    public enum ShapeType { RECTANGLE, TEXT, FILL_RECTANGLE };

    public abstract List<Object> getData();
    public abstract ShapeType getShapeType();
}
