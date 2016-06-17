package com.sfirestone.tictactoe;

import java.io.Serializable;

/**
 * Created by firestone on 6/15/16.
 */
public class Move implements Serializable {
    private final int x;
    private final int y;

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("Move{%s,%s}", this.x, this.y);
    }
}
