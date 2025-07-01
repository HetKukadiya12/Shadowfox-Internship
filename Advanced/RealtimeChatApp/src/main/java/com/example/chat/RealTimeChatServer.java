/*
 * RealTimeChatServer v2
 * Last modified: 2025-06-26
 * Minor tweaks: port 6500, timestamped broadcast messages.
 */
package com.example.chat;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class RealTimeChatServer {

    private static final int PORT = 6500;
    private static final Set<ClientHandler> CLIENTS = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("Server starting on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                CLIENTS.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcast(String message) {
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String formatted = "[" + timeStamp + "] " + message;
        synchronized (CLIENTS) {
            for (ClientHandler client : CLIENTS) {
                client.send(formatted);
            }
        }
        System.out.println(formatted);
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;
        private String nickname = "Anonymous";

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                out = new PrintWriter(socket.getOutputStream(), true);
                nickname = in.readLine(); // first line is nickname
                broadcast(nickname + " joined the chat!");
                String line;
                while ((line = in.readLine()) != null) {
                    if ("\quit".equalsIgnoreCase(line.trim())) {
                        break;
                    }
                    broadcast(nickname + ": " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                CLIENTS.remove(this);
                broadcast(nickname + " left the chat.");
            }
        }

        void send(String msg) {
            if (out != null) {
                out.println(msg);
            }
        }
    }
}
