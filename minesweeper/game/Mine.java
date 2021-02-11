package minesweeper.game;

public class Mine {
    private boolean bomb, flagged, discovered;

    public Mine() {
        bomb = false;
        flagged = false;
        discovered = false;
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

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }
}
