package com.sfirestone.servit.server;

import com.sfirestone.servit.comm.Communication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by firestone on 6/16/16.
 */
public class Server {

    private static ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public static final void main(String[] args) throws IOException {
        System.out.println("The server is running.");
        try (ServerSocket listener = new ServerSocket(9091)){
            while (true) {
                new Thread(new Handler(listener.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Handler implements Runnable {
        private final Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                System.out.println("CacheClient connected");
                ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
                while (true) {
                    Communication communication = (Communication) in.readObject();
                    handleCommuncation(communication);

                }
            } catch (EOFException e) {
                System.out.println("CacheClient disconnected.");
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    this.socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void handleCommuncation(Communication communication) {
            System.out.format("Received: %s\n", communication);
        }
    }
}
