package minesweeper.solver;

import minesweeper.game.Game;

public class SolverLib {

    /**
     * Determines amount of flags neighboring the given tile
     * 
     * @param x x value of tile
     * @param y y value of tile
     * @param game game to reference
     * @return number of flags neighboring given tile
     */
    static protected int getNeighborFlags(int x, int y, Game game) {
        int output = 0;

        for (int i = Math.max(y-1, 0); i < Math.min(y+2, game.getHeight()); i++) {
            for (int j = Math.max(x-1, 0); j < Math.min(x+2, game.getWidth()); j++) {
                if (game.isFlagged(j, i))
                    output++;
            }
        }

        return output;
    }

    /**
     * Determines amount of non discovered tiles neighboring the given tile
     * 
     * @param x x value of tile
     * @param y y value of tile
     * @param game game to reference
     * @return number of non discovered tiles neighboring given tile
     */
    static protected int getNeighborUndiscovered(int x, int y, Game game) {
        int output = 0;

        for (int i = Math.max(y-1, 0); i < Math.min(y+2, game.getHeight()); i++) {
            for (int j = Math.max(x-1, 0); j < Math.min(x+2, game.getWidth()); j++) {
                if (!game.isDiscovered(j, i) && !game.isFlagged(j, i))
                    output++;
            }
        }

        return output;
    }

    /**
     * Method to determine amount of flags needed to 'complete'
     * a tile, or to determine amount of extra flags
     * 
     * @param x x value of tile
     * @param y y value of tile
     * @param game game to reference
     * @return flags left to uncover (positive) or extra flags placed (negative)
     */
    static protected int getFlagsLeft(int x, int y, Game game) {
        return game.getNeighbors(x, y)-getNeighborFlags(x, y, game);
    }

    /**
     * 
     * @param targetX x value of target tile to find last flag
     * @param targetY y value of target tile to find last flag
     * @param game game to solve for
     * @return position of flag for target square
     */
    static protected int[] getCombination(int targetX, int targetY, Game game) {
        // will have code soontm
        return new int[] {0, 0};
    }
}
