package minesweeper.display.shapes;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public class Text extends Shape {
    private final String text;
    private final int x, y, size;
    private final Color c;

    public Text(String text, int x, int y, int size, Color c) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.size = size;
        this.c = c;
    }

    public List<Object> getData() {
        List<Object> output = new ArrayList<Object>();

        output.add(text);
        output.add(x);
        output.add(y);
        output.add(size);
        output.add(c);
        
        return output;
    }

    public ShapeType getShapeType() {
        return ShapeType.TEXT;
    }
}
