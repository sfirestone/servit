package com.sfirestone.tictactoe.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by firestone on 6/14/16.
 */
public class Server {
    private static final int PORT = 9090;

    private static final int MAX_THREADS = 10;
    private static final int DIMENSION = 3;

    public static void main(String[] args) {
        try {
            start(PORT, MAX_THREADS, DIMENSION);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start(int port, int maxThreads, int dimension) throws IOException {
        if (maxThreads < 2) {
            throw new IllegalArgumentException("Max threads must be greater than two");
        }

        ExecutorService executorService = null;
        System.out.format("Starting server on port %d with max threads of %d\n", port, maxThreads);
        try (ServerSocket listener = new ServerSocket(port)) {
            executorService = Executors.newFixedThreadPool(maxThreads);
            List<Player> players = new ArrayList<>();
            while (true) {
                Player player = new Player(listener.accept());
                System.out.format("Player joined (%s)\n", player);
                players.add(player);

                initializeGame(players, dimension, executorService);
             }
        }
        finally {
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
    }

    /**
     * Starts a game if there is more than one player. Ensures both players are still connected first, then removes
     * the players from the list of waiting players.
     */
    private static void initializeGame(List<Player> players, int dimension, ExecutorService executorService)
            throws IOException {
        if (players.size() > 1) {
            Player player1 = players.get(0);
            Player player2 = players.get(1);

            // Ensure both players are still connected
            boolean player1Alive = player1.checkAlive();
            boolean player2Alive = player2.checkAlive();

            if (player1Alive && player2Alive) {
                System.out.format("Starting new game between %s and %s\n", player1, player2);
                executorService.submit(new Game(dimension, player1, player2));
                players.remove(player1);
                players.remove(player2);
            }
            else {
                if (!player1Alive) {
                    System.out.format("Dropping disconnected player %s\n", player1);
                    players.remove(player1);
                    player1.close();
                }
                if (!player2Alive) {
                    System.out.format("Dropping disconnected player %s\n", player2);
                    players.remove(player2);
                    player2.close();
                }
            }
        }
    }

}
