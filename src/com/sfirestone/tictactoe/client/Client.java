package com.sfirestone.tictactoe.client;

import com.sfirestone.tictactoe.Communication;
import com.sfirestone.tictactoe.Move;

import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

/**
 * Created by firestone on 6/14/16.
 */
public class Client implements AutoCloseable {
    private static final String HOST = "localhost";
    private static final int PORT = 9090;

    private final Socket socket;
    private final BufferedReader stdIn;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    private static final Pattern VALID_MOVE = Pattern.compile("\\d+,\\d+");

    private boolean connectionOpen = true;
    private boolean gameOver = false;

    public static void main(String[] args) {
        try (Client client = new Client()) {
            client.playGame();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Client() throws IOException {
        System.out.format("Initiating connection to %s:%d...\n", HOST, PORT);
        this.socket = new Socket(HOST, PORT);
        final Socket socketToClose = this.socket;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (socketToClose != null) {
                    try {
                        socketToClose.close();
                    }
                    catch (IOException e) {
                        // Do nothing
                    }
                }
            }
        });

        System.out.format("Connected! Waiting for game...\n");
        this.stdIn = new BufferedReader(new InputStreamReader(System.in));
        this.in = new ObjectInputStream(this.socket.getInputStream());
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
    }

    private void playGame() throws IOException, ClassNotFoundException {
        while (this.connectionOpen) {
            try {
                handleInput();
            }
            catch (EOFException e) {
                System.err.println("Unspecified server error");
                return;
            }
            if (this.gameOver) {
                System.out.println("Thanks for playing!");
                return;
            }
        }
        System.err.println("Lost connection to server, terminating.");
    }

    private void handleInput() throws IOException, ClassNotFoundException {
        Communication input = (Communication) this.in.readObject();
        if (input == null) {
            this.connectionOpen = false;
            return;
        }
        switch (input.getStatus()) {
            case YOUR_TURN:
                System.out.println(input.getBoard().toVisualBoard());
                System.out.format("You are %s: ", input.getMark());
                gatherAndSendMove();
                break;
            case OPPONENTS_TURN:
                System.out.println(input.getBoard().toVisualBoard());
                System.out.format("You are %s: Waiting for opponent...\n", input.getMark());
                break;
            case INVALID_MOVE:
                System.out.println("Invalid move, please try again.");
                gatherAndSendMove();
                break;
            case WON:
                System.out.println(input.getBoard().toVisualBoard());
                System.out.println("Congratulations, you won!");
                this.gameOver = true;
                break;
            case LOST:
                System.out.println(input.getBoard().toVisualBoard());
                System.out.println("Sorry, you lost. Better luck next time.");
                this.gameOver = true;
                break;
            case TIE:
                System.out.println(input.getBoard().toVisualBoard());
                System.out.println("It's a tie.");
                this.gameOver = true;
                break;
            case PING:
                // Keepalive...ignore
                break;
            default:
                System.out.format("Unrecognized command %s\n", input.getStatus());
                break;
        }
    }

    private void gatherAndSendMove() throws IOException {
        String input = "";
        while (!VALID_MOVE.matcher(input).matches()) {
            System.out.format("Please enter your move in the form 'x,y'): ");
            input = this.stdIn.readLine();
        }
        String[] coordinates = input.split(",");
        this.out.writeObject(new Communication(Communication.Status.MOVE_REQUEST,
                new Move(Integer.parseInt(coordinates[0]) - 1, Integer.parseInt(coordinates[1]) - 1)));
        this.out.flush();
    }

    @Override
    public void close() {
        if (this.in != null) {
            try {
                this.in.close();
            }
            catch (IOException e) {
                // Do nothing
            }
        }

        if (this.out != null) {
            try {
                this.out.close();
            }
            catch (IOException e) {
                // Do nothing
            }
        }

        if (this.socket != null) {
            try {
                this.socket.close();
            }
            catch (IOException e) {
                // Do nothing
            }
        }
    }
}
