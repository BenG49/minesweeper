package minesweeper.display.shapes;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;

public class Rect extends Shape {
    private final int x, y, width, height, border;
    private final Color c;

    public Rect(int x, int y, int width, int height, int border, Color c) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.border = border;
        this.c = c;
    }

    public List<Object> getData() {
        List<Object> output = new ArrayList<Object>();

        output.add(x);
        output.add(y);
        output.add(width);
        output.add(height);
        output.add(border);
        output.add(c);
        
        return output;
    }

    public ShapeType getShapeType() {
        return ShapeType.RECTANGLE;
    }
}
