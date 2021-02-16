package minesweeper.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import minesweeper.display.DisplayGame;
import minesweeper.game.Game;

public class GameSolver {
    private Game game;
    private DisplayGame d;
    private boolean[][] zeroes;
    private int sleepTimeMillis;
    private List<List<Integer>> badGuesses;

    public boolean won, lost;

    public GameSolver(Game game, DisplayGame d) {
        this.game = game;
        this.d = d;

        won = false;
        lost = false;
        sleepTimeMillis = -1;
        badGuesses = new ArrayList<List<Integer>>();

        zeroes = new boolean[game.getHeight()][game.getWidth()];
        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                if (game.isDiscovered(x, y)) {
                    if (game.getNeighbors(x, y) == 0)
                        zeroes[y][x] = true;
                }
            }
        }
    }

    public void run(int consecutiveNonMovement, int sleepTimeMillis) {
        boolean moved = true;
        int noMovementCount = 0;
        this.sleepTimeMillis = sleepTimeMillis;
        consecutiveNonMovement--;

        print();
        while(!won && !lost && moved) {
            if (!execute())
                noMovementCount++;
            else
                noMovementCount = 0;

            if (noMovementCount > consecutiveNonMovement) {
                moved = false;
                d.setStuck(true);
                print();
            }
        }
    }

    // RETURNS IF ANY MOVES WERE MADE
    public boolean execute() {
        boolean moved = false;

        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                /* IF:
                    value is zero
                    undiscovered
                    flagged
                    value is zero (add to zero list) */
                if (zeroes[y][x] || !game.isDiscovered(x, y) || game.isFlagged(x, y) || game.getNeighbors(x, y) == 0) {
                    if (game.getNeighbors(x, y) == 0)
                        zeroes[y][x] = true;
                    continue;
                }

                int value = game.getNeighbors(x, y);
                int undiscovered = SolverLib.getNeighborUndiscovered(x, y, game);

                if (!moved && mainAlg(x, y, SolverLib.getFlagsLeft(x, y, game), undiscovered))
                    moved = true;

                // ONES FLANKING TWO
                if (value == 2 && !moved && onesFlankingTwoAlg(x, y))
                    moved = true;
            }
        }

        // if stuck
        if (!moved && !game.checkGameWin()) {
            int targetX = 0, targetY = 0, undiscoveredCount = 0;
            boolean hasAdjacent = false;

            for (int y = 0; y < game.getHeight(); y++) {
                for (int x = 0; x < game.getWidth(); x++) {
                    /* IF:
                        value is zero
                        undiscovered
                        flagged
                        value is zero (add to zero list) */
                    if (zeroes[y][x] || !game.isDiscovered(x, y) || game.isFlagged(x, y) || game.getNeighbors(x, y) == 0) {
                        if (game.getNeighbors(x, y) == 0)
                            zeroes[y][x] = true;
                        if (!game.isDiscovered(x, y) && !game.isFlagged(x, y))
                            undiscoveredCount++;
                        if (!hasAdjacent) {
                            hasAdjacent:
                            for (int i = Math.max(y-1, 0); i < Math.min(y+2, game.getHeight()); i++) {
                                for (int j = Math.max(x-1, 0); j < Math.min(x+2, game.getWidth()); j++) {
                                    if (game.isDiscovered(j, i)) {
                                        hasAdjacent = true;
                                        break hasAdjacent;
                                    }
                                }
                            }
                        }
                        continue;
                    }

                    // if square isn't fulfilled and isn't in list of bad guesses
                    if (SolverLib.getFlagsLeft(x, y, game) > 0 && !badGuesses.contains(Arrays.asList(x, y))) {
                        targetX = x;
                        targetY = y;
                    }
                }
            }

            // if there are a few squares left, not adjacent to any others
            if (!hasAdjacent) {
                for (int y = 0; y < game.getHeight(); y++) {
                    for (int x = 0; x < game.getWidth(); x++) {
                        if (game.isDiscovered(x, y))
                            continue;
                        
                        uncover(x, y);
                    }
                }
            // don't call getCombination if board is full
            } else if (undiscoveredCount > 0) {

                List<List<Integer>> bombs = SolverLib.getCombination(targetX, targetY, game);
            
                for (int i = 0; i < bombs.size(); i++) {
                    if (flag(bombs.get(i).get(0), bombs.get(i).get(1)))
                        moved = true;
                }

                if (!moved)
                    badGuesses.add(Arrays.asList(targetX, targetY));
            } else
                // if mistake was made in guessing earlier and board is full but amount of bombs hasn't been reached
                return false;
        }

        return moved;
    }

    // MAIN ALGORITHM - RETURNS IF MOVE WAS MADE
    private boolean mainAlg(int x, int y, int minesLeft, int undiscovered) {
        boolean uncover = minesLeft == 0 && undiscovered > 0;
        boolean flag = minesLeft == undiscovered;
        boolean moved = false;

        if (uncover || flag) {
            for (int i = Math.max(y-1, 0); i < Math.min(y+2, game.getHeight()); i++) {
                for (int j = Math.max(x-1, 0); j < Math.min(x+2, game.getWidth()); j++) {
                    if (!game.isDiscovered(j, i) && !game.isFlagged(j, i)) {
                        if (uncover && uncover(j, i))
                            moved = true;
                        if (flag && flag(j, i))
                            moved = true;
                    }
                }
            }
        }

        return moved;
    }

    // TWO WITH ONES FLANKING PATTERN - RETURNS IF MOVE WAS MADE
    private boolean onesFlankingTwoAlg(int x, int y) {
        int orientation = 0;
        boolean moved = false;

        // horizontally flanking
        if (x > 0 && x < game.getWidth()-1 && game.getNeighbors(x-1, y) == 1 && game.getNeighbors(x+1, y) == 1)
            orientation = 1;

        // vertically flanking
        else if (y > 0 && y < game.getHeight()-1 && game.getNeighbors(x, y-1) == 1 && game.getNeighbors(x, y+1) == 1)
            orientation = 2;

        if (orientation != 0) {
            int var, maxVar, xOffset, yOffset;

            if (orientation == 1) {
                var = y;
                maxVar = game.getHeight()-1;
                xOffset = 0;
                yOffset = 1;
            } else {// orientation = 2
                var = x;
                maxVar = game.getWidth()-1;
                xOffset = 1;
                yOffset = 0;
            }

            // positive direction
            if (var < maxVar && !game.isDiscovered(x+xOffset, y+yOffset)) {
                if (!moved && uncover(x+xOffset, y+yOffset))
                    moved = true;
                if (!game.isFlagged(x+xOffset-yOffset, y-xOffset+yOffset)) {
                    if(flag(x+xOffset-yOffset, y-xOffset+yOffset))
                        moved = true;
                }
                if (!game.isFlagged(x+xOffset+yOffset, y+xOffset+yOffset)) {
                    if (flag(x+xOffset+yOffset, y+xOffset+yOffset))
                        moved = true;
                }
            }

            // negative direction
            if (var != 0 && !game.isDiscovered(x-xOffset, y-yOffset)) {
                if(uncover(x-xOffset, y-yOffset))
                    moved = true;
                if (!game.isFlagged(x-xOffset-yOffset, y-xOffset-yOffset)) {
                    if (flag(x-xOffset-yOffset, y-xOffset-yOffset))
                        moved = true;
                }
                if (!game.isFlagged(x-xOffset+yOffset, y+xOffset-yOffset)) {
                    if (flag(x-xOffset+yOffset, y+xOffset-yOffset))
                        moved = true;
                }
            }
        }

        return moved;
    }

    // UNCOVERING WRAPPER METHOD - RETURNS IF TILE WAS UNCOVERED
    private boolean uncover(int x, int y) {
        wonLost(game.uncoverTile(x, y, false));
        print();

        return true;
    }

    // FLAGGING WRAPPER METHOD
    private boolean flag(int x, int y) {
        boolean flag = true;

        for (int i = Math.max(y-1, 0); i < Math.min(y+2, game.getHeight()); i++) {
            for (int j = Math.max(x-1, 0); j < Math.min(x+2, game.getWidth()); j++) {
                if (game.isDiscovered(j, i) && SolverLib.getFlagsLeft(j, i, game) < 1) {
                    flag = false;
                    break;
                }
            }
        }

        if (flag) {
            game.flagTile(x, y);
            print();
        }

        return flag;
    }

    private void wonLost(int i) {
        if (i == 1) {
            lost = true;
            d.setLost(true);
        } else if (i == 2) {
            won = true;
            d.setWon(true);
        }
    }

    private void print() {
        if (sleepTimeMillis != -1) {
            d.drawGame();

            try {
                Thread.sleep(sleepTimeMillis);
            } catch(InterruptedException e) {}
        }
    }
}