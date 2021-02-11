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
        mines = new Mine[x][y];

        // initialize array
        for (int b = 0; b < getHeight(); b++) {
            for (int a = 0; a < getWidth(); a++)
                mines[a][b] = new Mine();
        }

        Random rand = new Random();
        int j, i;

        // places bombs and fills array
        for (int bombs = 0; bombs < bombCount; bombs++) {
            for (;;) {
                j = rand.nextInt(getWidth());
                i = rand.nextInt(getHeight());

                if (!mines[i][j].isBomb())
                    break;
            }

            mines[j][i].setBomb(true);
        }

        // make starting island
        for (;;) {
            j = rand.nextInt(getWidth());
            i = rand.nextInt(getHeight());

            mines[j][i].setDiscovered(true);

            if (getNeighbors(j, i) == 0 && !mines[j][i].isBomb())
                break;

            mines[j][i].setDiscovered(false);
        }

        uncoverSquare(j, i);
    }

    public int getWidth() { return mines[0].length; }
    public int getHeight() { return mines.length; }

    public boolean isBomb(int x, int y) {
        return mines[x][y].isBomb();
    }

    public boolean isFlagged(int x, int y) {
        return mines[x][y].isFlagged();
    }

    public boolean isDiscovered(int x, int y) {
        return mines[x][y].isDiscovered();
    }

    // returns true if you hit a bomb
    public boolean uncoverSquare(int x, int y) {
        if (mines[x][y].isBomb()) {
            // game over
            System.out.println("game over :sad:");
            return true;
        }

        mines[x][y].setDiscovered(true);
        
        if (getNeighbors(x, y) == 0) {
            for (int i = Math.max(y - 1, 0); i < Math.min(y + 2, getHeight()); i++) {
                for (int j = Math.max(x - 1, 0); j < Math.min(x + 2, getWidth()); j++) {
                    if (!mines[j][i].isDiscovered() && !mines[j][i].isBomb()) {
                        uncoverSquare(j, i);
                    }
                }
            }
        }

        return false;
    }
    
    public void printBoard() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (mines[x][y].isFlagged())
                    System.out.print("|F");
                else if (mines[x][y].isDiscovered()) {
                    System.out.print("|"+getNeighbors(x, y));
                } else
                    System.out.print("| ");
            }

            System.out.println("|");
        }
    }

    public void flagSquare(int x, int y) {
        if (mines[x][y].isFlagged())
            mines[x][y].setFlagged(false);
        else
            mines[x][y].setFlagged(true);
    }

    public int getNeighbors(int x, int y) {
        if (mines[x][y].isDiscovered()) {
            int output = 0;

            for (int i = Math.max(x - 1, 0); i < Math.min(x + 2, getWidth()); i++) {
                for (int j = Math.max(y - 1, 0); j < Math.min(y + 2, getHeight()); j++) {
                    if (!(i == 0 && j == 0)) {
                        if (mines[i][j].isBomb())
                            output++;
                    }
                }
            }

            return output;
        }

        return -1;
    }
}