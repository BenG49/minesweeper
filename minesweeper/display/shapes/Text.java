package minesweeper.display.shapes;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.*;

public class Text extends Shape {
    private final String text, font;
    private final int x, y, size;
    private final Color c;
    private final boolean bold;

    public Text(String text, int x, int y, int size, Color c, String font, boolean bold) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.size = size;
        this.c = c;
        this.font = font;
        this.bold = bold;
    }

    public ShapeType getShapeType() {
        return ShapeType.TEXT;
    }

    public Graphics2D draw(Graphics2D g) {
        if (bold)
            g.setFont(new Font(font, Font.BOLD, size));
        else
            g.setFont(new Font(font, Font.PLAIN, size));

        g.setColor(c);
        g.drawString(text, x, y);

        return g;
    }
}
