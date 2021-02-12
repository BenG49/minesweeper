package minesweeper.game;

import java.util.Random;

public class Game {
    public enum Difficulty {
        EASY(10, 10, 10),
        MEDIUM(16, 16, 40),
        HARD(16, 30, 99),
        // myles' addition
        DEFAULT(MEDIUM);

        private Difficulty(Difficulty d) {
            this(d.getX(), d.getY(), d.getBombs());
        }
        
        private int x, y, bombs;

        private Difficulty(int x, int y, int bombs) {
            this.x = x;
            this.y = y;
            this.bombs = bombs;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getBombs() {
            return bombs;
        }
    }

    private Mine[][] mines;

    public Game() { this(Difficulty.DEFAULT); }
    public Game(Difficulty d) { this(d.getX(), d.getY(), d.getBombs()); }
    public Game(int x, int y, int bombCount) {
        mines = new Mine[y][x];

        // initialize array
        for (int b = 0; b < getHeight(); b++) {
            for (int a = 0; a < getWidth(); a++)
                mines[b][a] = new Mine();
        }

        Random rand = new Random();
        int j, i;

        // places bombs and fills array
        for (int bombs = 0; bombs < bombCount; bombs++) {
            for (;;) {
                j = rand.nextInt(getWidth());
                i = rand.nextInt(getHeight());

                if (!isBomb(j, i))
                    break;
            }

            setBomb(j, i, true);
        }

        // make starting island
        for (;;) {
            j = rand.nextInt(getWidth());
            i = rand.nextInt(getHeight());

            setDiscovered(j, i, true);

            if (getNeighbors(j, i) == 0 && !isBomb(j, i))
                break;

            setDiscovered(j, i, false);
        }

        uncoverSquare(j, i);
    }

    public int getWidth() { return mines[0].length; }
    public int getHeight() { return mines.length; }

    public boolean isFlagged(int x, int y) {
        return mines[y][x].isFlagged();
    }

    public boolean isDiscovered(int x, int y) {
        return mines[y][x].isDiscovered();
    }
    
    private boolean isBomb(int x, int y) {
        return mines[y][x].isBomb();
    }

    private void setBomb(int x, int y, boolean bomb) {
        mines[y][x].setBomb(bomb);
    }

    private void setDiscovered(int x, int y, boolean discovered) {
        mines[y][x].setDiscovered(discovered);
    }

    // returns true if you hit a bomb
    public boolean uncoverSquare(int x, int y) {
        if (isBomb(x, y)) {
            // game over
            System.out.println(x+", "+y+" - game over :sad:");
            return true;
        }

        setDiscovered(x, y, true);
        
        if (getNeighbors(x, y) == 0) {
            for (int i = Math.max(y - 1, 0); i < Math.min(y + 2, getHeight()); i++) {
                for (int j = Math.max(x - 1, 0); j < Math.min(x + 2, getWidth()); j++) {
                    if (!isDiscovered(j, i) && !isBomb(j, i)) {
                        uncoverSquare(j, i);
                    }
                }
            }
        }

        return false;
    }
    
    public void printBoard() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (isFlagged(x, y))
                    System.out.print("|F");
                else if (isDiscovered(x, y)) {
                    System.out.print("|"+getNeighbors(x, y));
                } else
                    System.out.print("| ");
            }

            System.out.println("|");
        }
    }

    public void flagSquare(int x, int y) {
        if (isFlagged(x, y))
            mines[y][x].setFlagged(false);
        else
            mines[y][x].setFlagged(true);
    }

    public int getNeighbors(int x, int y) {
        if (isDiscovered(x, y)) {
            int output = 0;

            for (int i = Math.max(y-1, 0); i < Math.min(y+2, getHeight()); i++) {
                for (int j = Math.max(x-1, 0); j < Math.min(x+2, getWidth()); j++) {
                    if (!(i == 0 && j == 0) && isBomb(j, i)) {
                        output++;
                    }
                }
            }

            return output;
        }

        return -1;
    }
}