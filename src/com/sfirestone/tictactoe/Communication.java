package com.sfirestone.tictactoe;

import java.io.Serializable;

/**
 * Created by firestone on 6/15/16.
 */
public class Communication implements Serializable {
    public enum Status {
        PING,
        YOUR_TURN,
        OPPONENTS_TURN,
        MOVE_REQUEST,
        INVALID_MOVE,
        WON,
        TIE,
        LOST
    }

    private final Status status;
    private Character mark;
    private Board board;
    private Move move;

    public Communication(Status status) {
        this.status = status;
    }

    public Communication(Status status, Board board, char mark) {
        this.status = status;
        this.board = board;
        this.mark = mark;
    }

    public Communication(Status status, Move move) {
        this.status = status;
        this.move = move;

    }

    public Status getStatus() {
        return status;
    }

    public Character getMark() {
        return mark;
    }

    public Board getBoard() {
        return board;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public String toString() {
        return "Communication{" +
                "status=" + status +
                ", mark=" + mark +
                ", board=" + board +
                ", move=" + move +
                '}';
    }
}
