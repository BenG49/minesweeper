package minesweeper;

import minesweeper.game.*;
import minesweeper.game.Game.Difficulty;
import minesweeper.solver.GameSolver;

public class Main {
    public static void main(String[] args) {
        Game g = new Game(Difficulty.DEFAULT);
        GameSolver solver = new GameSolver(g);

        solver.run(5, 5);
    }
}
