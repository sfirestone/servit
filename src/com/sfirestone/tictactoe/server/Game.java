package com.sfirestone.tictactoe.server;

import com.sfirestone.tictactoe.Communication;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;

import static com.sfirestone.tictactoe.Communication.Status.*;

/**
 * Created by firestone on 6/14/16.
 */
public class Game implements Callable<Game.Result> {

    private final Player playerX;
    private final Player playerO;

    private final ServerBoard board;

    private boolean playerXTurn = true;

    public Game(int dimension, Player player1, Player player2) throws IOException {
        this.board = new ServerBoard(dimension);

        int playerXNumber = new Random().nextInt(2);
        this.playerX = playerXNumber == 0 ? player1 : player2;
        this.playerX.setMark('X');
        this.playerO = playerXNumber == 1 ? player1 : player2;
        this.playerO.setMark('O');
    }

    @Override
    public Result call() throws Exception {
        try {
            sendStatus(YOUR_TURN, OPPONENTS_TURN);
            while (true) {
                try {
                    Communication communication = getCurrentPlayer().receive();
                    boolean winningMove = this.board.set(getCurrentPlayer().getMark(),
                            communication.getMove().getX(), communication.getMove().getY());

                    if (winningMove) {
                        sendStatus(WON, LOST);
                        return new Result(getCurrentPlayer());
                    }

                    if (this.board.isFilled()) {
                        sendStatus(TIE, TIE);
                        return new Result();
                    }

                    changePlayer();
                    sendStatus(YOUR_TURN, OPPONENTS_TURN);
                } catch (IllegalArgumentException e) {
                    getCurrentPlayer().send(new Communication(INVALID_MOVE, this.board, getCurrentPlayer().getMark()));
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        } finally {
            if (this.playerX != null) {
                this.playerX.close();
            }
            if (this.playerO != null) {
                this.playerO.close();
            }
        }
    }

    public class Result {
        private Player winner;

        private Result() {
            // No winner
        }

        private Result(Player winner) {
            this.winner = winner;
        }

        public Player getWinner() {
            return this.winner;
        }
    }

    public Player getCurrentPlayer() {
        return this.playerXTurn ? this.playerX : this.playerO;
    }

    public Player getNonCurrentPlayer() {
        return this.playerXTurn ? this.playerO : this.playerX;
    }

    public void changePlayer() {
        this.playerXTurn ^= true;
    }

    public void sendStatus(Communication.Status currentPlayerStatus, Communication.Status nonCurrentPlayerStatus)
            throws IOException {
        getCurrentPlayer().send(
                new Communication(currentPlayerStatus, this.board, getCurrentPlayer().getMark()));
        getNonCurrentPlayer().send(
                new Communication(nonCurrentPlayerStatus, this.board, getNonCurrentPlayer().getMark()));
    }
}
