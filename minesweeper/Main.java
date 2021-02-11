package minesweeper;

import minesweeper.game.Game;
import minesweeper.game.GameSolver;
import minesweeper.game.Game.Difficulty;

public class Main {
    public static void main(String[] args) {
        Game g = new Game(Difficulty.DEFAULT);
        GameSolver solver = new GameSolver(g);

        g.printBoard();
        solver.execute();
        System.out.println();
        g.printBoard();
    }
}
