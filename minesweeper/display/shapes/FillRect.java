package minesweeper.display.shapes;

import java.awt.Color;
import java.awt.*;

public class FillRect extends Shape {
    private final int x, y, width, height, border;
    private final Color fill;

    public FillRect(int x, int y, int width, int height, int border, Color fill) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.border = border;
        this.fill = fill;
    }

    public ShapeType getShapeType() {
        return ShapeType.FILL_RECTANGLE;
    }

    public Graphics2D draw(Graphics2D g) {
        g.setColor(fill);
        g.setStroke(new BasicStroke(border));
        g.fillRect(x, y, width, height);
        
        return g;
    }
}
