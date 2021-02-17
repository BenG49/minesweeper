package minesweeper.display;

import javax.swing.JFrame;
import java.awt.Color;

import minesweeper.display.shapes.Shape;


public class Display extends JFrame {
    public final int WIDTH;
    public final int HEIGHT;

    protected Color background;

    protected Draw currentDraw;

    public Display() { this(500, 500, Color.WHITE); }
    public Display(Color background) { this(500, 500, background); }
    public Display(int width, int height, Color background) {
        super("Minesweeper");

        WIDTH = width;
        HEIGHT = height;
        this.background = background;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);

        getContentPane().setBackground(background);
    }

    public void draw(Shape[] shapes) {
        try {
            remove(currentDraw);
        } catch(NullPointerException e) {}

        Draw d = new Draw(shapes, background);
        currentDraw = d;
       
        add(d);
        revalidate();
    }

    public void setBackground(Color background) {
        this.background = background;

        getContentPane().setBackground(background);
    }
}
