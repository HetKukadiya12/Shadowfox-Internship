/*
 * RealTimeChatClient v2
 * Last modified: 2025-06-26
 * Minor tweaks: connects to port 6500, prompts for nickname, window title updated.
 */
package com.example.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class RealTimeChatClient {

    private static final String HOST = "localhost";
    private static final int PORT = 6500;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RealTimeChatClient::new);
    }

    private BufferedReader reader;
    private PrintWriter writer;
    private String nickname;

    public RealTimeChatClient() {
        nickname = JOptionPane.showInputDialog(null, "Enter your nickname:", "Nickname",
                JOptionPane.PLAIN_MESSAGE);
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = "Guest" + System.currentTimeMillis()%1000;
        }
        setupNetworking();
        createGUI();
        new Thread(new IncomingReader()).start();
    }

    private JTextArea incoming;
    private JTextField outgoing;

    private void createGUI() {
        JFrame frame = new JFrame("Realtime Chat App v2");
        JPanel mainPanel = new JPanel(new BorderLayout());

        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);

        JScrollPane qScroller = new JScrollPane(incoming);
        mainPanel.add(qScroller, BorderLayout.CENTER);

        outgoing = new JTextField();
        outgoing.addActionListener(e -> sendMessage());
        mainPanel.add(outgoing, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void setupNetworking() {
        try {
            Socket socket = new Socket(HOST, PORT);
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(nickname); // send nickname first
            System.out.println("Networking established");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot connect to server");
            System.exit(1);
        }
    }

    private void sendMessage() {
        String message = outgoing.getText().trim();
        if (!message.isEmpty()) {
            writer.println(message);
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    incoming.append(message + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
