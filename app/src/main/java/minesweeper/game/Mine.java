package minesweeper.game;

public class Mine {
    private boolean bomb, flagged, discovered;
    private int value;

    public Mine() {
        bomb = false;
        flagged = false;
        discovered = false;
        value = 10;
    }

    public boolean isBomb() {
        return bomb;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public int getValue() {
        return value;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
