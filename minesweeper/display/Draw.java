package minesweeper.display;

import java.awt.*;
import javax.swing.JPanel;
import minesweeper.display.shapes.Shape;

public class Draw extends JPanel {
    private final Shape[] shapes;
    
    public Draw(Shape[] shapes, String font) {
        this.shapes = shapes;

        setPreferredSize(new Dimension(1, 1));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        for (Shape i : shapes) {
            g2 = i.draw(g2);
        }
    }
}
