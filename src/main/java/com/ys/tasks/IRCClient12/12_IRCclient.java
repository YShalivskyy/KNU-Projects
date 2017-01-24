package com.ys.tasks.IRCClient12;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class IRCClient {

    private static String line;
    private static Scanner scanner = new Scanner(System.in);
    static boolean sendDataFlag = false;

    public static void main(String[] args) throws IOException, InterruptedException {

        // The server to connect to and our details. https://webchat.freenode.net/
        String server = "irc.freenode.net";
        System.out.println("Enter your nickname: ");
        String login = scanner.nextLine();

        String channel = "#ipctest";

        // Connect directly to the IRC server.
        Socket socket = new Socket(server, 6667);
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        // Log on to the server.
        writer.write("NICK " + login + "\r\n");
        writer.write("USER " + login + " 8 * : Java IRC Test\r\n");
        writer.flush();

        // Read lines from the server until it tells us we have connected.
        while ((line = reader.readLine()) != null) {
            if (line.contains("004")) {
                // We are now logged in.
                break;
            } else if (line.contains("433")) {
                System.out.println("Nickname is already in use.");
                return;
            }
        }

        // Join the channel
        writer.write("JOIN " + channel + "\r\n");
        writer.flush();

        Thread getData = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Keep reading lines from the server
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        if (!sendDataFlag && line.contains("366")) {
                            sendDataFlag = true;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(IRCClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Thread sendData = new Thread(new Runnable() {
            String msg = new String();

            @Override
            public void run() {
                // Sending messages
                System.err.println("You can now send messages! Type \"/exit\" to leave.");
                while (true) {
                    try {
                        msg = scanner.nextLine();
                        if (msg.equals("/exit")) {
                            System.exit(0);
                        }
                        writer.write("PRIVMSG " + channel + " : " + msg + "\r\n");
                        writer.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(IRCClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        getData.start();
        while (!sendDataFlag) {
            Thread.sleep(20);
        }
        sendData.start();
    }
}
