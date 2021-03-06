package minesweeper.solver;

import minesweeper.game.Game;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class SolverLib {

    /**
     * Determines amount of flags neighboring the given tile
     * 
     * @param x x value of tile
     * @param y y value of tile
     * @param game game to reference
     * @return number of flags neighboring given tile
     */
    protected static int getNeighborFlags(int x, int y, Game game) {
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
    protected static int getNeighborUndiscovered(int x, int y, Game game) {
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
    protected static int getFlagsLeft(int x, int y, Game game) {
        return game.getNeighbors(x, y)-getNeighborFlags(x, y, game);
    }

    /**
     * 
     * @param targetX x value of target tile to find last flag
     * @param targetY y value of target tile to find last flag
     * @param game game to solve for
     * @return position of flag for target square
     */
    protected static List<List<Integer>> getCombination(int targetX, int targetY, Game game) {
        if (targetX < 0 || targetX > game.getWidth() || targetY < 0 || targetY > game.getHeight())
            return new ArrayList<List<Integer>>();
        
        final boolean CALC_MULT_RIGHT_COMBINATIONS = true;
        int validCombinationCount = 0;

        // list of tileGroups and uncoveredGroups
        List<List<List<List<Integer>>>> temp = getGroups(game);

        List<List<List<Integer>>> tileGroups = temp.get(0);
        List<List<List<Integer>>> uncoveredGroups = temp.get(1);

        List<List<Integer>> outputBombs = new ArrayList<List<Integer>>();

        int targetGroup = 0;
        for (int i = 0; i < tileGroups.size(); i++) {
            if (tileGroups.get(i).contains(Arrays.asList(targetX, targetY))) {
                targetGroup = i;
                break;
            }
        }

        int uncoveredCount = uncoveredGroups.get(targetGroup).size();

        // TODO: implement optimizations with min and max
        int minBombs = 1;
        int maxBombs = uncoveredCount;

        // find all bomb combinations
        for (int i = 0; i < Math.pow(2, uncoveredCount); i++) {
            List<List<Integer>> bombArray = new ArrayList<List<Integer>>();
            String bin = Integer.toBinaryString(i);

            if (!withinMinMax(minBombs, maxBombs, bin))
                continue;

            // loops through every uncovered tile
            for (int coord = 0; coord < uncoveredCount; coord++) {
                if (coord < bin.length() && bin.charAt(bin.length()-1-coord) == '1')
                    bombArray.add(uncoveredGroups.get(targetGroup).get(coord));
            }

            // combination of bombs is valid
            if (validBombArrangement(tileGroups.get(targetGroup), bombArray, game)) {
                if (CALC_MULT_RIGHT_COMBINATIONS)
                    validCombinationCount++;

                for (int coord = 0; coord < bombArray.size(); coord++) {
                    List<Integer> tempCoord = bombArray.get(coord);

                    // within target x and y, add to output
                    if (Math.abs(targetX-tempCoord.get(0)) < 2 && Math.abs(targetY-tempCoord.get(1)) < 2)
                        outputBombs.add(tempCoord);
                }

                if (!CALC_MULT_RIGHT_COMBINATIONS || validCombinationCount > 0)
                    break;
            }
        }

        if (CALC_MULT_RIGHT_COMBINATIONS && validCombinationCount > 0) {
            outputBombs = new ArrayList<List<Integer>>();
            System.out.println("oof");
        }

        return outputBombs;
    }

    // TODO: can be optimized by using pre computed lookup table
    private static boolean withinMinMax(int min, int max, String binCombination) {
        int output = 0;

        for (int i = 0; i < binCombination.length(); i++) {
            if (binCombination.charAt(i) == '1')
                output++;
            
            if (output > max)
                return false;
        }

        if (output < min)
            return false;
        
        return true;
    }

    private static boolean validBombArrangement(List<List<Integer>> tileGroup, List<List<Integer>> flags, Game game) {
        // tileGroup is fine
        for (int coord = 0; coord < tileGroup.size(); coord++) {
            int tempX = tileGroup.get(coord).get(0);
            int tempY = tileGroup.get(coord).get(1);

            // find neighboring flags
            int neighborFlags = 0;

            for (int y = Math.max(tempY-1, 0); y < Math.min(tempY+2, game.getHeight()); y++) {
                for (int x = Math.max(tempX-1, 0); x < Math.min(tempX+2, game.getWidth()); x++) {
                    if (game.isFlagged(x, y) || flags.contains(Arrays.asList(x, y)))
                        neighborFlags++;
                }
            }

            // if tile isn't satisfied
            if (game.getNeighbors(tempX, tempY)-neighborFlags != 0) {
                return false;
            }
        }

        return true;
    }

    protected static List<List<List<List<Integer>>>> getGroups(Game game) {
        List<List<List<Integer>>> tiles = new ArrayList<List<List<Integer>>>();
        List<List<List<Integer>>> undiscovered = new ArrayList<List<List<Integer>>>();
        /*  2 lists of groups containing list of cells containing list of coordinates
        
            go through whole board
        
            if square isnt fulfilled and not next to any others, instantiate new group
            continue */

        for (int y = 0; y < game.getHeight(); y++) {
            continueLoop:
            for (int x = 0; x < game.getWidth(); x++) {
                // if tile is discovered and unfulfilled
                if (game.isDiscovered(x, y) && getFlagsLeft(x, y, game) > 0) {
                    // loop through all groups and coords within those groups
                    for (int group = 0; group < tiles.size(); group++) {
                        for (int coord = 0; coord < tiles.get(group).size(); coord++) {
                            if (commonUndiscovered(x, y, 
                                    tiles.get(group).get(coord).get(0),
                                    tiles.get(group).get(coord).get(1),
                                    game)) {
                                // add coordinate to current group
                                tiles.get(group).add(Arrays.asList(x, y));
                                undiscovered = checkUndiscovered(x, y, group, undiscovered, game);

                                continue continueLoop;
                            }
                        }
                    }

                    // create new group
                    tiles.add(new ArrayList<List<Integer>>());
                    undiscovered.add(new ArrayList<List<Integer>>());
                    // add coordinate to last group
                    tiles.get(tiles.size()-1).add(Arrays.asList(x, y));
                    undiscovered = checkUndiscovered(x, y, tiles.size()-1, undiscovered, game);

                }
            }
        }

        // yes this is a sane amount of nested lists
        List<List<List<List<Integer>>>> output = new ArrayList<List<List<List<Integer>>>>();
        output.add(tiles);
        output.add(undiscovered);

        return output;
    }

    private static List<List<List<Integer>>> checkUndiscovered(int x, int y, int group, List<List<List<Integer>>> current, Game game) {
        for (int i = Math.max(y-1, 0); i < Math.min(y+2, game.getHeight()); i++) {
            for (int j = Math.max(x-1, 0); j < Math.min(x+2, game.getWidth()); j++) {
                // if tile is undiscovered and not in array already
                if (!game.isDiscovered(j, i) && !game.isFlagged(j, i) && !current.get(group).contains(Arrays.asList(j, i)))
                    current.get(group).add(Arrays.asList(j, i));
            }
        }

        return current;
    }

    private static boolean commonUndiscovered(int x1, int y1, int x2, int y2, Game game) {
        /* IF
        two points are the same or
        two points are farther than 3 squares apart */
        if ((x1 == x2 && y1 == y2) || (Math.abs(x1-x2) > 3 || Math.abs(y1-y2) > 3))
            return false;
        
        int direction = findDirection(x1, y1, x2, y2);
        int xInterval = 1, yInterval = 1;

        // ADJACENT TILES
        // vertical
        if (direction == 9)
            xInterval = 2;
        // horizontal
        else if (direction == 10);
            yInterval = 2;
        
        for (int y = Math.max(y1-1, 0); y < Math.min(y1+2, game.getHeight()); y+=yInterval) {
            for (int x = Math.max(x1-1, 0); x < Math.min(x1+2, game.getWidth()); x+=xInterval) {
                /* IF
                current point is within one tile of coord2 and
                current point isn't discovered and
                current point isn't flagged*/
                if (!game.isDiscovered(x, y) && !game.isFlagged(x, y) && Math.abs(x2-x) < 2 && Math.abs(y2-y) < 2)
                    return true;
            }
        }

        return false;
    }

    private static int findDirection(int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2)
            return -1;

        // vertically adjacent
        if (x1 == x2 && Math.abs(y1-y2) == 1)
            return 9;

        // horizontally adjacent
        if (y1 == y2 && Math.abs(x1-x2) == 1)
            return 10;

        // 0 1 2
        // 3 4 5
        // 6 7 8
        int direction = -1;

        // determine direction
        int xDelta = x1-x2;
        int yDelta = y1-y2;

        // top left or bottom right
        if (xDelta == yDelta) {
            if (xDelta < 0)
                direction = 8;
            else
                direction = 0;
        // top right or bottom left
        } else if (xDelta*-1 == yDelta) {
            if (xDelta < 0)
                direction = 2;
            else
                direction = 6;
        // top or bottom
        } else if (xDelta == 0) {
            if (yDelta > 0)
                direction = 1;
            else
                direction = 7;
        // left or right
        } else if (yDelta == 0) {
            if (xDelta > 0)
                direction = 3;
            else
                direction = 5;
        }

        return direction;
    }
}
