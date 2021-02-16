package minesweeper;

import minesweeper.display.*;
import minesweeper.game.*;
import minesweeper.game.Game.Difficulty;
import minesweeper.solver.GameSolver;

public class Main {
    public static void main(String[] args) {
        boolean cont = true;

        while(cont) {
            cont = false;

            Game g = new Game(Difficulty.MEDIUM);
            // DisplayGame gameDisplay = new DisplayGame(g, "Times New Roman");
            DisplayGame gameDisplay = new DisplayGame(g);
            GameSolver solver = new GameSolver(g, gameDisplay);

            solver.run(3, 100);

            while(!cont) {
                if (gameDisplay.hasKey("Enter"))
                    cont = true;
                else if (gameDisplay.hasKey("q") || gameDisplay.hasKey("Q"))
                    break;
            }

            gameDisplay.dispose();
        }
    }
}