package com.ys.tasks.DependencyInjection6;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

interface MessageService {
    boolean sendMessage(String msg, String recipient);
}

class EmailService implements MessageService {
    @Override
    public boolean sendMessage(String msg, String recipient) {
        System.out.println("Message sent via GMail to " + recipient + ", message: " + msg);
        return true;
    }
}

class Telegram implements MessageService {

    public boolean sendMessage(String msg, String recipient) {
        //some complex code to send Facebook message
        System.out.println("Message sent via Telegram to " + recipient + ", message: " + msg);
        return true;
    }

}

class Sender {
    MessageService messenger;
    public String textM;
    public String recipient;

    public void send(String _text, String _recipient) {
        messenger.sendMessage(_text, _recipient);
        textM = _text;
        recipient = _recipient;
    }
}

public class SocialMessenger {
    public static Sender sender;
    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        File f = new File("DISettings.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = br.readLine();
        String[] parsed = line.split(",");

        Class senderClass = Class.forName("com.ys.tasks.DependencyInjection6." + parsed[0]);
        sender = (Sender)senderClass.newInstance();
        Field messangerField = senderClass.getDeclaredField(parsed[1]);
        Class messengerClass = Class.forName("com.ys.tasks.DependencyInjection6." + parsed[2]);
        messangerField.set(sender, messengerClass.newInstance());
        sender.send("Hi Man", "Yarik");
    }
}
