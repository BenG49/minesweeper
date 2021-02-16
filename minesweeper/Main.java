package minesweeper;

import minesweeper.display.*;
import minesweeper.game.*;
import minesweeper.game.Game.Difficulty;
import minesweeper.solver.GameSolver;

public class Main {
    public static void main(String[] args) {
        Game g = new Game(Difficulty.MEDIUM);
        // DisplayGame gameDisplay = new DisplayGame(g, "Times New Roman");
        DisplayGame gameDisplay = new DisplayGame(g);
        GameSolver solver = new GameSolver(g, gameDisplay);

        solver.run(3, 5);
    }
}
