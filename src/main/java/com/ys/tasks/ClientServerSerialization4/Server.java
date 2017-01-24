package com.ys.tasks.ClientServerSerialization4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static Data data;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket=new ServerSocket(1234);
        System.out.println("Server started on port " +
                serverSocket.getLocalPort() + "...");
        Socket server = serverSocket.accept();
        System.out.println("Client connected");
        ObjectInputStream in = new ObjectInputStream(server.getInputStream());
        data=(Data)in.readObject();
        data.print();
    }
}