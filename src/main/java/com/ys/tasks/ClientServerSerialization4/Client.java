package com.ys.tasks.ClientServerSerialization4;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class Client {
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 1234);
        Data data = new Data();
        data.username = "Yarik";
        data.message = "Hi, how are you?";
        data.time = String.valueOf(new Date());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(data);
        out.flush();
        System.out.println("Message sent");
    }
}
