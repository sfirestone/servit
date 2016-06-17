package com.sfirestone.tictactoe.server;

import com.sfirestone.tictactoe.Communication;
import com.sfirestone.tictactoe.Move;

import java.io.*;
import java.net.Socket;

/**
 * Created by firestone on 6/14/16.
 */
public class Player {
    private char mark;
    private final Socket socket;

    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Player(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void setMark(char mark) {
        this.mark = mark;
    }

    public char getMark() {
        return this.mark;
    }

    public void send(Communication communication) throws IOException {
        this.out.reset();
        this.out.writeObject(communication);
        this.out.flush();
    }

    public Communication receive() throws IOException, ClassNotFoundException {
        return (Communication) this.in.readObject();
    }

    public boolean checkAlive() {
        try {
            send(new Communication(Communication.Status.PING));
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public Move readMove() throws IOException, ClassNotFoundException {
        return (Move) this.in.readObject();
    }

    public void close() {
        if (this.out != null) {
            try {
                this.out.close();
            }
            catch (IOException e) {
                // Do nothing
            }
        }

        if (this.in != null) {
            try {
                this.in.close();
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

    @Override
    public String toString() {
        return "Player{" +
                "socket=" + socket +
                '}';
    }
}
