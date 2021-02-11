package minesweeper.game;

import javax.annotation.processing.RoundEnvironment;

public class GameSolver {
    private Game board;

    private boolean[][] zeroes;

    public GameSolver(Game board) {
        this.board = board;

        zeroes = new boolean[board.getWidth()][board.getHeight()];
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if (board.isDiscovered(x, y)) {
                    if (board.getNeighbors(x, y) == 0)
                        zeroes[x][y] = true;
                }
            }
        }
    }

    public void execute() {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                // if discovered and not flagged and not a zero
                if (!board.isDiscovered(x, y) || board.isFlagged(x, y) || zeroes[x][y] || board.getNeighbors(x, y) == 0) {
                    // if within the 'islands' and has no neighbors, add to list
                    if (board.getNeighbors(x, y) == 0)
                        zeroes[x][y] = true;
                    continue;
                }

                int value = board.getNeighbors(x, y);
                int flags = getNeighborFlags(x, y);
                int undiscovered = getNeighborUndiscovered(x, y);

                // FLAG ALGORITHMS
                // NO CHOICE FLAGGING
                if (value-flags == undiscovered) {
                    noChoiceAlg(x, y);
                    // update values for next if statement
                    flags = getNeighborFlags(x, y);
                }

                // ONES FLANKING TWO
                if (value == 2)
                    onesFlankingTwoAlg(x, y);

                // UNCOVER ALGORITHMS - this could be in a separate loop, but i decided not to
                if (value == flags) {
                    if (undiscovered > 0)
                        valueFulfilled(x, y);
                }
            }
        }
    }

    private int getNeighborFlags(int x, int y) {
        int output = 0;

        for (int i = Math.max(y-1, 0); i < Math.min(y+2, board.getHeight()); i++) {
            for (int j = Math.max(x-1, 0); j < Math.min(x+2, board.getWidth()); j++) {
                if (board.isFlagged(j, i))
                    output++;
            }
        }

        return output;
    }

    private int getNeighborUndiscovered(int x, int y) {
        int output = 0;

        for (int i = Math.max(y-1, 0); i < Math.min(y+2, board.getHeight()); i++) {
            for (int j = Math.max(x-1, 0); j < Math.min(x+2, board.getWidth()); j++) {
                if (!board.isDiscovered(j, i) && !board.isFlagged(j, i))
                    output++;
            }
        }

        return output;
    }


    // FLAG PLACING PATTERN RECOGNITION ALGS
    private void noChoiceAlg(int x, int y) {
        for (int i = Math.max(y-1, 0); i < Math.min(y+2, board.getHeight()); i+=2) {
            for (int j = Math.max(x-1, 0); j < Math.min(x+2, board.getWidth()); j+=2) {
                if (!board.isDiscovered(j, i) && !board.isFlagged(j, i)) {
                    board.flagSquare(j, i);
                    break;
                }
            }
        }
    }

    private void onesFlankingTwoAlg(int x, int y) {
        int orientation = 0;

        // vertically flanking
        if (x > 0 && x < board.getWidth()-1 && board.getNeighbors(x-1, y) == 1 && board.getNeighbors(x+1, y) == 1)
            orientation = 1;

        // horizontally flanking
        else if (y > 0 && y < board.getHeight()-1 && board.getNeighbors(x, y-1) == 1 && board.getNeighbors(x, y+1) == 1)
            orientation = 2;

        if (orientation != 0) {
            int var, maxVar, xOffset, yOffset;

            if (orientation == 1) {
                var = x;
                maxVar = board.getWidth()-1;
                xOffset = 1;
                yOffset = 0;
            } else /*if (orientation == 2)*/ {
                var = y;
                maxVar = board.getHeight()-1;
                xOffset = 0;
                yOffset = 1;
            }

            System.out.println(x+", "+y);

            if (var != 0 && board.isDiscovered((x+xOffset), (y+yOffset))) {
                board.uncoverSquare(x+xOffset, y+yOffset);
                if (!board.isFlagged(x-xOffset-yOffset, y-xOffset-yOffset)) {
                    board.flagSquare(x-xOffset-yOffset, y-xOffset-yOffset);
                }
                if (!board.isFlagged(x-xOffset+yOffset, y+xOffset-yOffset)) {
                    board.flagSquare(x-xOffset-yOffset, y-xOffset-yOffset);
                }
            }

            if (var < maxVar && board.isDiscovered(x-xOffset, y-yOffset)) {
                board.uncoverSquare(x+xOffset, y+yOffset);
                if (!board.isFlagged(x+xOffset-yOffset, y-xOffset+yOffset)) {
                    board.flagSquare(x+xOffset-yOffset, y-xOffset+yOffset);
                }
                if (!board.isFlagged(x+xOffset+yOffset, y+xOffset+yOffset)) {
                    board.flagSquare(x+xOffset-yOffset, y-xOffset+yOffset);
                }
            }
        }
    }

    // UNCOVERING ALGORITHMS
    private void valueFulfilled(int x, int y) {
        for (int i = Math.max(y-1, 0); i < Math.min(y+2, board.getHeight()); i++) {
            for (int j = Math.max(x-1, 0); j < Math.min(x+2, board.getWidth()); j++) {
                if (!board.isDiscovered(j, i) && !board.isFlagged(j, i))
                    board.uncoverSquare(j, i);
            }
        }
    }
}