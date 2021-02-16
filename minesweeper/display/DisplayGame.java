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
        List<Shape> shapes = new ArrayList<Shape>();

        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                shapes.add(new Rect(scale*x, scale*y, scale, scale, 2, Color.BLACK));

                if (game.getNeighbors(x, y) != -1)
                    shapes.add(new Text(Integer.toString(game.getNeighbors(x, y)), scale*x+(int)(scale*FONT_X_MULT),
                        scale*y+(int)(scale*FONT_Y_MULT), scale/2, getColor(game.getNeighbors(x, y)), font, false));
                    
                if (game.isFlagged(x, y))
                    shapes.add(new Text( "F", scale*x+(int)(scale*FONT_X_MULT),
                        scale*y+(int)(scale*FONT_Y_MULT), scale/2, FLAG_COLOR, font, false));

                if (lost && game.getLostPos()[0] == x && game.getLostPos()[1] == y)
                    shapes.add(new FillRect(scale*x, scale*y, scale, scale, 1, new Color(1f, 0f, 0f, 0.5f)));
            }
        }
                
        if (won) 
            shapes = drawTextCenter("YOU WON!!1!1!", 40, Color.BLACK, shapes);

        if (lost) {
            shapes = drawTextCenter("you lose :(", 40, Color.BLUE, shapes);
            // TODO: highlight lost square
        }

        if (stuck) 
            shapes = drawTextCenter("got stuck :(", 40, Color.RED, shapes);

        Shape[] output = new Shape[shapes.size()];
        for (int i = 0; i < output.length; i++)
            output[i] = shapes.get(i);

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

    private List<Shape> drawTextCenter(String text, int size, Color color, List<Shape> shapes) {
        final float BOX_PADDING = 1.5f;
        final Color FILL_COLOR = new Color(0f, 0f, 0f, 0.1f);

        final float TXT_WIDTH_MULT;
        final float TXT_HEIGHT_MULT;
        final float TXT_HEIGHT_CONST;

        if (font == "Times New Roman") {
            // doesn't really work
            TXT_WIDTH_MULT = 0.6f;
            TXT_HEIGHT_MULT = 2f;
            TXT_HEIGHT_CONST = 0.7f;
        } else { // "Cascadia Code"
            TXT_WIDTH_MULT = 0.8f;
            TXT_HEIGHT_MULT = 2f;
            TXT_HEIGHT_CONST = 0.75f;
        }

        // font measurement of em height
        float px;
        if (size == 1)
            px = 1;
        else
            px = size*0.75f;
        
        int txtWidth =  (int) (px*text.length()*TXT_WIDTH_MULT);
        int txtHeight = (int) (px*TXT_HEIGHT_MULT);

        // add text
        shapes.add(new Text(text, (int)((WIDTH-txtWidth)/2), (int)((WIDTH-txtHeight)/2+txtHeight*TXT_HEIGHT_CONST),
            size, color, font, true));

        // add border box
        shapes.add(new Rect( (int)((WIDTH-txtWidth*BOX_PADDING)/2), (int)((WIDTH-txtHeight*BOX_PADDING)/2),
            (int)(txtWidth*BOX_PADDING), (int)(txtHeight*BOX_PADDING), 4, Color.BLACK));

        // add fill box
        shapes.add(new FillRect( (int)((WIDTH-txtWidth*BOX_PADDING)/2), (int)((WIDTH-txtHeight*BOX_PADDING)/2),
            (int)(txtWidth*BOX_PADDING), (int)(txtHeight*BOX_PADDING), 4, FILL_COLOR));

        return shapes;
    }

}
