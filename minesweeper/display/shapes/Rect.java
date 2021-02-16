package minesweeper.display.shapes;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.*;

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

    public ShapeType getShapeType() {
        return ShapeType.RECTANGLE;
    }

    public Graphics2D draw(Graphics2D g) {
        g.setColor(c);
        g.setStroke(new BasicStroke(border));
        g.drawRect(x, y, width, height);

        return g;
    }
}
