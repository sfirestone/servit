package com.sfirestone.tictactoe.server;

import com.sfirestone.tictactoe.Board;

/**
 * Created by firestone on 6/14/16.
 */
public class ServerBoard extends Board {
    private int cellsEmpty;

    public ServerBoard(int dimension) {
        super(dimension);
        this.cellsEmpty = dimension * dimension;
    }

    /**
     * @throws IllegalArgumentException if x/y are out of range or if cell is already filled
     * @return true if move wins game, false otherwise
     */
    public synchronized boolean set(char c, int x, int y) {
        if (x < 0 || x >= this.dimension
                || y < 0 || y >= this.dimension) {
            throw new IllegalArgumentException(String.format("Invalid coordinates (%d,%d)", x, y));
        }
        if (this.board[x][y] != null) {
            throw new IllegalArgumentException(
                    String.format("Coordinate (%d,%d) is already filled with %s", x, y, this.board[x][y]));
        }

        this.board[x][y] = c;
        this.cellsEmpty--;
        return checkForWin(c, x, y);
    }

    public boolean isFilled() {
        return this.cellsEmpty == 0;
    }

    private boolean checkForWin(char c, int x, int y) {
        // Check row for win
        boolean rowWin = true;
        for (int i = 0; i < this.dimension && rowWin; i++) {
            rowWin = boardMatches(c, i, y);
        }
        if (rowWin) {
            return true;
        }

        // Check column for win
        boolean columnWin = true;
        for (int j = 0; j < this.dimension && columnWin; j++) {
            columnWin = boardMatches(c, x, j);
        }
        if (columnWin) {
            return true;
        }

        // Check upper-left to lower-right diagonal for win
        boolean lrDiagonalWin = true;
        if (x == y) {
            for (int d1 = 0; d1 < this.dimension && lrDiagonalWin; d1++) {
                 lrDiagonalWin = boardMatches(c, d1, d1);
            }
            if (lrDiagonalWin) {
                return true;
            }
        }

        // Check upper-right to lower-left diagonal for win
        boolean rlDiagonalWin = true;
        if (x + y == this.dimension - 1) {
            for (int d2 = 0; d2 < this.dimension && rlDiagonalWin; d2++) {
                rlDiagonalWin = boardMatches(c, (this.dimension - 1) - d2, d2);
            }
            if (rlDiagonalWin) {
                return true;
            }
        }

        return false;
    }

    private boolean boardMatches(char c, int x, int y) {
        return this.board[x][y] != null && this.board[x][y] == c;
    }
}
