package minesweeper.game;

public class GameSolver {
    private Game board;
    private boolean[][] zeroes;
    public boolean won, lost;

    private final int sleepTimeMillis;

    public GameSolver(Game board, int sleepTimeMillis) {
        this.board = board;
        this.sleepTimeMillis = sleepTimeMillis;

        won = false;
        lost = false;

        zeroes = new boolean[board.getHeight()][board.getWidth()];
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if (board.isDiscovered(x, y)) {
                    if (board.getNeighbors(x, y) == 0)
                        zeroes[y][x] = true;
                }
            }
        }
    }

    public void run(int consecutiveNonMovement) {
        boolean moved = true;
        int noMovementCount = 0;

        while(!won && !lost && moved) {
            if (!execute())
                noMovementCount++;
            else
                noMovementCount = 0;

            if (noMovementCount > consecutiveNonMovement) {
                System.out.println("\nStuck :(\n");
                moved = false;
            }

            if (won)
                System.out.println("\nWon!\n");
        }
    }

    // RETURNS IF ANY MOVES WERE MADE
    public boolean execute() {
        boolean moved = false;

        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                /* IF:
                    value is zero
                    undiscovered
                    flagged
                    value is zero (add to zero list) */
                if (zeroes[y][x] || !board.isDiscovered(x, y) || board.isFlagged(x, y) || board.getNeighbors(x, y) == 0) {
                    if (board.getNeighbors(x, y) == 0)
                        zeroes[y][x] = true;
                    continue;
                }

                int value = board.getNeighbors(x, y);
                int undiscovered = getNeighborUndiscovered(x, y);

                if (!moved && mainAlg(x, y, getMinesLeft(x, y), undiscovered))
                    moved = true;

                // ONES FLANKING TWO
                if (value == 2 && !moved && onesFlankingTwoAlg(x, y))
                    moved = true;
            }
        }

        if (!moved) {
        }

        return moved;
    }

    // RETURNS NEIGHBORING FLAGS
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

    // RETURNS UNDISCOVERED AND UNFLAGGED SQUARES NEIGHBORING
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

    // RETURNS SIGNED VALUE OF SQUARES FLAGGED VS VALUE
    private int getMinesLeft(int x, int y) {
        return board.getNeighbors(x, y)-getNeighborFlags(x, y);
    }


    // TWO WITH ONES FLANKING PATTERN - RETURNS IF MOVE WAS MADE
    private boolean onesFlankingTwoAlg(int x, int y) {
        int orientation = 0;
        boolean moved = false;

        // horizontally flanking
        if (x > 0 && x < board.getWidth()-1 && board.getNeighbors(x-1, y) == 1 && board.getNeighbors(x+1, y) == 1)
            orientation = 1;

        // vertically flanking
        else if (y > 0 && y < board.getHeight()-1 && board.getNeighbors(x, y-1) == 1 && board.getNeighbors(x, y+1) == 1)
            orientation = 2;

        if (orientation != 0) {
            int var, maxVar, xOffset, yOffset;

            if (orientation == 1) {
                var = y;
                maxVar = board.getHeight()-1;
                xOffset = 0;
                yOffset = 1;
            } else {// orientation = 2
                var = x;
                maxVar = board.getWidth()-1;
                xOffset = 1;
                yOffset = 0;
            }

            // positive direction
            if (var < maxVar && !board.isDiscovered(x+xOffset, y+yOffset)) {
                if (!moved && uncover(x+xOffset, y+yOffset))
                    moved = true;
                if (!board.isFlagged(x+xOffset-yOffset, y-xOffset+yOffset)) {
                    if(flag(x+xOffset-yOffset, y-xOffset+yOffset))
                        moved = true;
                }
                if (!board.isFlagged(x+xOffset+yOffset, y+xOffset+yOffset)) {
                    if (flag(x+xOffset+yOffset, y+xOffset+yOffset))
                        moved = true;
                }
            }

            // negative direction
            if (var != 0 && !board.isDiscovered(x-xOffset, y-yOffset)) {
                if(uncover(x-xOffset, y-yOffset))
                    moved = true;
                if (!board.isFlagged(x-xOffset-yOffset, y-xOffset-yOffset)) {
                    if (flag(x-xOffset-yOffset, y-xOffset-yOffset))
                        moved = true;
                }
                if (!board.isFlagged(x-xOffset+yOffset, y+xOffset-yOffset)) {
                    if (flag(x-xOffset+yOffset, y+xOffset-yOffset))
                        moved = true;
                }
            }
        }

        return moved;
    }

    // MAIN ALGORITHM - RETURNS IF MOVE WAS MADE
    private boolean mainAlg(int x, int y, int minesLeft, int undiscovered) {
        boolean uncover = minesLeft == 0 && undiscovered > 0;
        boolean flag = minesLeft == undiscovered;
        boolean moved = false;

        if (uncover || flag) {
            for (int i = Math.max(y-1, 0); i < Math.min(y+2, board.getHeight()); i++) {
                for (int j = Math.max(x-1, 0); j < Math.min(x+2, board.getWidth()); j++) {
                    if (!board.isDiscovered(j, i) && !board.isFlagged(j, i)) {
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

    // UNCOVERING WRAPPER METHOD - RETURNS IF SQUARE WAS UNCOVERED
    private boolean uncover(int x, int y) {
        wonLost(board.uncoverSquare(x, y, false));
        System.out.println();
        board.printBoard();
        sleep();

        return true;
    }

    // FLAGGING WRAPPER METHOD
    private boolean flag(int x, int y) {
        boolean flag = true;

        for (int i = Math.max(y-1, 0); i < Math.min(y+2, board.getHeight()); i++) {
            for (int j = Math.max(x-1, 0); j < Math.min(x+2, board.getWidth()); j++) {
                if (board.isDiscovered(j, i) && getMinesLeft(j, i) < 1) {
                    flag = false;
                    break;
                }
            }
        }

        if (flag) {
            board.flagSquare(x, y);
            System.out.println();
            board.printBoard();
            sleep();
        }

        return flag;
    }

    private void wonLost(int i) {
        if (i == 1)
            lost = true;
        else if (i == 2)
            won = true;
    }

    private void sleep() {
        try {
            Thread.sleep(sleepTimeMillis);
        } catch(InterruptedException e) {
            System.out.println("got interrupted!");
        }
    }
}