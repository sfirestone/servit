package com.sfirestone.tictactoe;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Created by firestone on 6/14/16.
 */
public class Board implements Serializable {
    protected static final String FIELD_SEPARATOR = ":";
    protected static final Pattern VALID_BOARD = Pattern.compile("\\d+" + FIELD_SEPARATOR + "(\\w| )+");

    protected final Character[][] board;
    protected final int dimension;

    protected Board(int dimension) {
        this.dimension = checkDimension(dimension);
        this.board = new Character[dimension][dimension];
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(this.dimension + ":");
        for (int y = 0; y < this.dimension; y++) {
            for (int x = 0; x < this.dimension; x++) {
                Character c = this.board[x][y];
                sb.append(c == null ? " " : c);
            }
        }
        return sb.toString();
    }

    public String toVisualBoard() {
        StringBuffer sb = new StringBuffer();
        for (int y = 0; y < 2 * this.dimension - 1; y++) {
            for (int x = 0; x < 2 * this.dimension - 1; x++) {
                // If y is odd, print horizontal separator
                if (y % 2 == 1) {
                    sb.append('-');
                    continue;
                }

                if (x % 2 == 1) {
                    // If x is odd, print vertical separator
                    sb.append('|');
                }
                else {
                    // Otherwise, print the character (or an empty space if it is null)
                    Character c = this.board[x/2][y/2];
                    sb.append(c == null ? " " : c);
                }

            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private int checkDimension(int dimension) {
        if (this.dimension < 3) {
            throw new IllegalArgumentException("Board dimension must be at least three");
        }
        if (this.dimension % 2 != 1) {
            throw new IllegalArgumentException("Board dimension must be odd");
        }
        return dimension;
    }
}
