package minesweeper.display;

import java.awt.Color;
import java.awt.Font;

import bglib.display.shapes.*;
import minesweeper.game.Game;
import bglib.input.InputDisplay;

public class DisplayGame extends InputDisplay {
    private Game game;
    private boolean won, lost, stuck;

    private final String font;
    private final int scale;
    private final Font gameFont;
    private final Font titleFont;

    // CONSTANTS
    private final double FONT_X_MULT = 0.35;
    private final double FONT_Y_MULT = 0.70;
    private final Color FLAG_COLOR = Color.DARK_GRAY;

    public DisplayGame(Game game) { this(game, "Cascadia Code", 40); }
    public DisplayGame(Game game, String font) { this(game, font, 40); }
    public DisplayGame(Game game, String font, int scale) {
        super(game.getWidth()*scale, game.getHeight()*scale, Color.WHITE);
        
        this.game = game;
        this.scale = scale;
        this.font = font;

        won = false;
        lost = false;
        stuck = false;

        gameFont = new Font(font, Font.PLAIN, scale/2);
        titleFont = new Font(font, Font.BOLD, 30);
    }

    public void drawGame() {
        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                frameAdd(new Rect(scale*x, scale*y, scale, scale, 2, Color.BLACK));

                if (game.getNeighbors(x, y) != -1)
                    frameAdd(new Text(Integer.toString(game.getNeighbors(x, y)), scale*x+(int)(scale*FONT_X_MULT),
                        scale*y+(int)(scale*FONT_Y_MULT), getColor(game.getNeighbors(x, y)), gameFont));
                    
                if (game.isFlagged(x, y))
                    frameAdd(new Text( "F", scale*x+(int)(scale*FONT_X_MULT), scale*y+(int)(scale*FONT_Y_MULT),
                        FLAG_COLOR, new Font(font, Font.PLAIN, scale/2)));

                if (lost && game.getLostPos()[0] == x && game.getLostPos()[1] == y)
                    frameAdd(new FillRect(scale*x, scale*y, scale, scale, 1, new Color(1f, 0f, 0f, 0.5f)));
            }
        }
                
        if (won || lost || stuck) {
            if (won) 
                drawTextCenter("YOU WON!!1!1!", 40, Color.BLACK);

            if (lost)
                drawTextCenter("you lose :(", 40, Color.BLUE);

            if (stuck) 
                drawTextCenter("got stuck :(", 40, Color.RED);
            
            frameAdd(new Text("Press enter to sim again", (int) (WIDTH*0.15), (int) (HEIGHT*0.62),
                Color.DARK_GRAY, titleFont));
            frameAdd(new Text("press q to quit", (int) (WIDTH*0.275), (int) (HEIGHT*0.685),
                Color.DARK_GRAY, titleFont));
        }

        draw();
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

    private void drawTextCenter(String text, int size, Color color) {
        final float BOX_PADDING = 1.5f;
        final Color FILL_COLOR = new Color(0f, 0f, 0f, 0.15f);

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
        frameAdd(new Text(text, (int)((WIDTH-txtWidth)/2), (int)((WIDTH-txtHeight)/2+txtHeight*TXT_HEIGHT_CONST),
            color, new Font(font, Font.BOLD, size)));

        // add fill box
        frameAdd(new FillRect( (int)((WIDTH-txtWidth*BOX_PADDING)/2), (int)((WIDTH-txtHeight*BOX_PADDING)/2),
            (int)(txtWidth*BOX_PADDING), (int)(txtHeight*BOX_PADDING), 4, FILL_COLOR));

        // add border box
        frameAdd(new Rect( (int)((WIDTH-txtWidth*BOX_PADDING)/2), (int)((WIDTH-txtHeight*BOX_PADDING)/2),
            (int)(txtWidth*BOX_PADDING), (int)(txtHeight*BOX_PADDING), 4, Color.BLACK));
    }
}
