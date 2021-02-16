package minesweeper.input;

import minesweeper.display.Display;

public class InputDisplay extends Display {
    private KeyTracker keyboard;

    public InputDisplay() { this(500, 500); }
    public InputDisplay(int width, int height) {
        super(width, height);

        keyboard = new KeyTracker();
        addKeyListener(keyboard);
    }

    public boolean hasKey(String key) {
        return keyboard.hasKey(key);
    }
}