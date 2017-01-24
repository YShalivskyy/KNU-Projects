package com.ys.tasks.Applet7;

import java.awt.*;

public class ActionHandler extends java.applet.Applet {

    MainWindow mainWindow;

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public void init() {
        add(new Button("Start"));
    }

    public void begin() {
        mainWindow = new MainWindow("Balls Applet App", false);
        mainWindow.resize(840,680);
        mainWindow.show();
        mainWindow.startThreads();
    }

    public void stop() {
        if (mainWindow !=null) {
            mainWindow.terminateThreads();
        }
    }

    public static void main(String args[]) {
        new ActionHandler().begin();
    }

    public boolean action(Event e,Object arg) {
        if (e.target instanceof Button &&
                ((String)arg).equals("Start")) {
            mainWindow = new MainWindow("Balls Applet App", true);
            mainWindow.resize(840,680);
            mainWindow.show();
            mainWindow.startThreads();
        }
        return true;
    }
}