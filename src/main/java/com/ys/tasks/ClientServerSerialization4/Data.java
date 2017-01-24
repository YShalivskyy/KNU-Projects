package com.ys.tasks.ClientServerSerialization4;

import java.io.Serializable;

public class Data implements Serializable {
    public String username;
    public String message;
    public String time;

    void print(){
        System.out.println("Time: " + time);
        System.out.println("Username: "+ username);
        System.out.println("Message: "+ message);
    }
}