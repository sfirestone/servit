package com.sfirestone.servit.client;

import com.sfirestone.servit.comm.CacheCommunication;

import java.io.*;
import java.net.Socket;

/**
 * Created by firestone on 6/16/16.
 */
public class CacheClient {
    private static final String HOST = "localhost";
    private static final int PORT = 9091;

    private final String username;
    private ObjectOutputStream out;
    private BufferedReader in;

    public CacheClient(String username) {
        this.username = username;
    }

    public void connect(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.format("Options:\n  (G)et value from cache\n  (P)ut value into cache\n  (V)iew cache\n" +
                        "  (Q)uit\nPlease choose one: ");
                String input = this.in.readLine();
                System.out.println("Received input: " + input);
                switch (input.toUpperCase()) {
                    case "G":
                        getFromCache();
                        break;
                    case "P":
                        putIntoCache();
                        break;
                    case "Q":
                        System.out.println("Have a nice day!");
                        return;
                    case "V":
                        viewCache();
                        break;
                    default:
                        System.out.println("Invalid input.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFromCache() throws IOException {
        System.out.format("  Enter cache key: ");
        String key = this.in.readLine().trim();
        CacheCommunication cacheCommunication = CacheCommunication.newBuilder()
                .withUsername(this.username)
                .withKey(key)
                .build();
        this.out.writeObject(cacheCommunication);
    }

    private void putIntoCache() throws IOException {
        System.out.format("  Enter cache key: ");
        String key = this.in.readLine().trim();
        System.out.format("  Enter cache value: ");
        String value = this.in.readLine().trim();
        CacheCommunication cacheCommunication = CacheCommunication.newBuilder()
                .withUsername(this.username)
                .withKey(key)
                .withValue(value)
                .build();
        this.out.writeObject(cacheCommunication);
    }

    private void viewCache() throws IOException {
        CacheCommunication cacheCommunication = CacheCommunication.newBuilder()
                .withUsername(this.username)
                .build();
        this.out.writeObject(cacheCommunication);
    }

    public static final void main(String[] args) {
        CacheClient cacheClient = new CacheClient("spencer");
        cacheClient.connect(HOST, PORT);
    }
}
