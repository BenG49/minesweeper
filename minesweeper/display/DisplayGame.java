package minesweeper.display;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import minesweeper.display.shapes.*;
import minesweeper.game.Game;

public class DisplayGame extends Display {
    private Game game;
    private boolean won, lost, stuck;
    private String font;

    private final int scale;

    // CONSTANTS
    private final double FONT_X_MULT = 0.35;
    private final double FONT_Y_MULT = 0.70;
    private final Color FLAG_COLOR = Color.DARK_GRAY;

    public DisplayGame(Game game) { this(game, "Cascadia Code", 40); }
    public DisplayGame(Game game, String font) { this(game, font, 40); }
    public DisplayGame(Game game, String font, int scale) {
        super(game.getWidth()*scale, game.getHeight()*scale);
        
        this.game = game;
        this.scale = scale;
        this.font = font;

        won = false;
        lost = false;
        stuck = false;
    }

    public void drawGame() {
        List<Shape> temp = new ArrayList<Shape>();

        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                temp.add(new Rect(scale*x, scale*y, scale, scale, 2, Color.BLACK));
                if (game.getNeighbors(x, y) != -1)
                    temp.add(new Text(
                        Integer.toString(game.getNeighbors(x, y)),
                        scale*x+(int)(scale*FONT_X_MULT),
                        scale*y+(int)(scale*FONT_Y_MULT),
                        scale/2,
                        getColor(game.getNeighbors(x, y))
                    ));
                    
                if (game.isFlagged(x, y))
                    temp.add(new Text(
                        "F",
                        scale*x+(int)(scale*FONT_X_MULT),
                        scale*y+(int)(scale*FONT_Y_MULT),
                        scale/2,
                        FLAG_COLOR
                    ));
                
                if (won) {
                    temp.add(new Text("YOU WON!!11!!", (int)(WIDTH*0.275), (int)(HEIGHT*0.525), 40, Color.BLACK));
                    temp.add(new Rect(
                        (int)(WIDTH*0.15),
                        (int)(HEIGHT*0.4),
                        (int)(WIDTH*0.7),
                        (int)(HEIGHT*0.2),
                        4, Color.BLACK
                    ));
                    temp.add(new FillRect(
                        (int)(WIDTH*0.15),
                        (int)(HEIGHT*0.4),
                        (int)(WIDTH*0.7),
                        (int)(HEIGHT*0.2),
                        2, new Color(1f, 1f, 1f, 0.1f)
                    ));
                }

                if (lost) {
                    // TODO: highlight lost square
                    temp.add(new Text("you lose :(", (int)(WIDTH*0.3), (int)(HEIGHT*0.525), 40, Color.BLUE));
                    temp.add(new Rect(
                        (int)(WIDTH*0.175),
                        (int)(HEIGHT*0.4),
                        (int)(WIDTH*0.65),
                        (int)(HEIGHT*0.2),
                        4, Color.BLACK
                    ));
                    temp.add(new FillRect(
                        (int)(WIDTH*0.175),
                        (int)(HEIGHT*0.4),
                        (int)(WIDTH*0.65),
                        (int)(HEIGHT*0.2),
                        2, new Color(1f, 1f, 1f, 0.1f)
                    ));
                }

                if (stuck) {
                    temp.add(new Text("got stuck :(", (int)(WIDTH*0.275), (int)(HEIGHT*0.525), 40, Color.BLUE));
                    temp.add(new Rect(
                        (int)(WIDTH*0.175),
                        (int)(HEIGHT*0.4),
                        (int)(WIDTH*0.65),
                        (int)(HEIGHT*0.2),
                        4, Color.BLACK
                    ));
                    temp.add(new FillRect(
                        (int)(WIDTH*0.175),
                        (int)(HEIGHT*0.4),
                        (int)(WIDTH*0.65),
                        (int)(HEIGHT*0.2),
                        2, new Color(1f, 1f, 1f, 0.1f)
                    ));
                }
            }
        }

        Shape[] output = new Shape[temp.size()];
        for (int i = 0; i < output.length; i++)
            output[i] = temp.get(i);

        draw(output, font);
    }

    private Color getColor(int value) {
        if (value == 1)
            return Color.BLUE;
        else if (value == 2)
            return Color.GREEN;
        else if (value == 3)
            return Color.RED;
        else if (value == 4)
            return new Color(128, 0, 128); // purple
        else if (value == 5)
            return Color.YELLOW;
        else if (value == 6)
            return new Color(0, 128, 128); // teal
        else if (value == 7)
            return Color.BLACK; // dark grey/black
        else if (value == 8)
            return Color.GRAY;  // light grey/blue
        else
            return Color.BLACK;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }
}
