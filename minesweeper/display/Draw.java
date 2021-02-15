package minesweeper.display;

import java.awt.*;
import javax.swing.JPanel;
import java.util.List;
import minesweeper.display.shapes.Shape;
import minesweeper.display.shapes.Shape.ShapeType;

public class Draw extends JPanel {
    private final Shape[] shapes;
    private final String font;
    
    public Draw(Shape[] shapes, String font) {
        this.shapes = shapes;
        this.font = font;

        setPreferredSize(new Dimension(1, 1));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        for (Shape i : shapes) {
            if (i.getShapeType() == ShapeType.RECTANGLE) {
                List<Object> temp = i.getData();

                g2.setColor((Color)temp.get(5));
                g2.setStroke(new BasicStroke((int)temp.get(4)));
                g2.drawRect((int)temp.get(0), (int)temp.get(1), (int)temp.get(2), (int)temp.get(3));

            } else if (i.getShapeType() == ShapeType.TEXT) {
                List<Object> temp = i.getData();

                g2.setColor((Color)temp.get(4));
                g2.setFont(new Font(font, Font.BOLD, (int)temp.get(3)));

                g2.drawString((String)temp.get(0), (int)temp.get(1), (int)temp.get(2));

            } else if (i.getShapeType() == ShapeType.FILL_RECTANGLE) {
                List<Object> temp = i.getData();

                g2.setColor((Color)temp.get(5));
                g2.setStroke(new BasicStroke((int)temp.get(4)));
                g2.fillRect((int)temp.get(0), (int)temp.get(1), (int)temp.get(2), (int)temp.get(3));
            }
        }
    }
}
