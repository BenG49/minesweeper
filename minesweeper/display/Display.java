package minesweeper.display;

import javax.swing.JFrame;
import minesweeper.display.shapes.Shape;

public class Display extends JFrame {
    public final int WIDTH;
    public final int HEIGHT;

    private Draw currentDraw;

    public Display() { this(500, 500); }
    public Display(int width, int height) {
        WIDTH = width;
        HEIGHT = height;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setName("Minesweeper");
        setVisible(true);
    }

    public void draw(Shape[] shapes, String font) {
        try {
            remove(currentDraw);
        } catch(NullPointerException e) {}

        Draw d = new Draw(shapes, font);
        currentDraw = d;
       
        add(d);
        revalidate();
    }
}
