package minesweeper.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

public class Game {
    public enum Difficulty {
        EASY(10, 10, 10),
        MEDIUM(16, 16, 40),
        HARD(30, 16, 99),
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
    private int bombCount;
    private int lostX, lostY;

    public Game() { this(Difficulty.DEFAULT); }
    public Game(Difficulty d) { this(d.getX(), d.getY(), d.getBombs()); }
    public Game(int x, int y, int bombCount) {
        mines = new Mine[y][x];
        lostX = -1;
        lostY = -1;
        this.bombCount = bombCount;

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

            if (getNeighborsPrivate(j, i) == 0 && !isBomb(j, i))
                break;
        }

        uncoverTile(j, i, false);
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

    // returns 1 if lose, 2 if win, 0 otherwise
    private List<List<Integer>> toUncover = new ArrayList<List<Integer>>();
    public int uncoverTile(int x, int y, boolean throughRecursion) {
        // game over
        if (isBomb(x, y)) {
            lostX = x;
            lostY = y;
            return 1;
        }

        // game win
        if (checkGameWin())
            return 2;

        // adds all squares to list to be added later
        if (throughRecursion)
            toUncover.add(Arrays.asList(x, y));
        else
            setDiscovered(x, y, true);
        
        if (getNeighborsPrivate(x, y) == 0) {
            setDiscovered(x, y, false);
            for (int i = Math.max(y - 1, 0); i < Math.min(y + 2, getHeight()); i++) {
                for (int j = Math.max(x - 1, 0); j < Math.min(x + 2, getWidth()); j++) {
                    if (!isDiscovered(j, i) && !isBomb(j, i) && !toUncover.contains(Arrays.asList(j, i))) {
                        uncoverTile(j, i, true);
                    }
                }
            }
        }

        if (throughRecursion) {
            for (int i = 0; i < toUncover.size(); i++)
                setDiscovered(toUncover.get(i).get(0), toUncover.get(i).get(1), true);
            
            toUncover = new ArrayList<List<Integer>>();
        }

        return 0;
    }
    
    public void printBoard() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (isFlagged(x, y))
                    System.out.print("|⚑");
                else if (isDiscovered(x, y)) {
                    if (getNeighbors(x, y) > 0)
                        System.out.print("|"+getNeighbors(x, y));
                    else
                        System.out.print("|•");
                } else
                    System.out.print("| ");
            }

            System.out.println("|");
        }
    }

    public void flagTile(int x, int y) {
        if (isFlagged(x, y))
            mines[y][x].setFlagged(false);
        else
            mines[y][x].setFlagged(true);
    }

    public boolean checkGameWin() {
        int numUndiscovered = 0;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (!isDiscovered(x, y))
                    numUndiscovered++;
            }
        }

        return numUndiscovered-1 == bombCount;
    }

    public int getNeighbors(int x, int y) {
        if (isDiscovered(x, y)) {
            return getNeighborsPrivate(x, y);
        }

        return -1;
    }

    private int getNeighborsPrivate(int x, int y) {
        // optimization probably
        int temp = mines[y][x].getValue();
        if (temp < 10)
            return temp;
        else {
            int output = 0;

            for (int i = Math.max(y-1, 0); i < Math.min(y+2, getHeight()); i++) {
                for (int j = Math.max(x-1, 0); j < Math.min(x+2, getWidth()); j++) {
                    if (isBomb(j, i)) {
                        output++;
                    }
                }
            }

            mines[y][x].setValue(output);
            return output;
        }
    }

    public int[] getLostPos() {
        return new int[] {lostX, lostY};
    }
}