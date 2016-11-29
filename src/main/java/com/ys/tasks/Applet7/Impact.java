package com.ys.tasks.Applet7;

import java.awt.*;

public class Impact extends java.applet.Applet {
    MainWindow mainWindow;

    public void init() {
        add(new Button("Start"));
    }

    public boolean action(Event e,Object arg) {
        if (e.target instanceof Button &&
            ((String)arg).equals("Start")) {
            mainWindow = new MainWindow("Impact Simulator", true);
            mainWindow.resize(640,480);
            mainWindow.show();
            mainWindow.start();
        }
        return true;
    }

    public void stop() {
        if (mainWindow !=null) {
            mainWindow.stop();
        }
    }

    //  These methods allow the applet to also run as an application.
    public static void main(String args[]) {
        new Impact().begin();
    }

    private void begin() {
        mainWindow = new MainWindow("Impact Simulator", false);
        mainWindow.resize(640,480);
        mainWindow.show();
        mainWindow.start();
    }
}