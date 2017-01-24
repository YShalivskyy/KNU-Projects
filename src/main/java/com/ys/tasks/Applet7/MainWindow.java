package com.ys.tasks.Applet7;

import java.awt.*;

class MainWindow extends Frame
{

    private AnimationHandler animationHandler;	//object that draws on canvas
    private Thread animationThread;	//thread running AnimationHandler
    private boolean is_applet;

    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }

    //  This method creates layout and main objects.
    MainWindow(String title, boolean isapp) {
        super(title);
        is_applet = isapp;
        Panel p = new Panel();      // control panel

        Label label = new Label("Applet App");
        label.setFont(new Font("TimesRoman",Font.BOLD,18));
        p.add(label);

        p.setLayout(new GridLayout(0,1));   // vertical layout

        p.setFont(new Font("TimesRoman",Font.PLAIN,12));

        p.add(new Button("Span Ball"));
        p.add(new Button("Exit"));
        animationHandler = new AnimationHandler(p);
        setLayout(new BorderLayout(4,4));
        add("Center", animationHandler);
        add("East",p);
    }

    public void startThreads() {
        if (animationThread ==null) {
            animationThread = new Thread(animationHandler);
            animationThread.start();        // startThreads new thread
        }
    }

    public void terminateThreads() {
        if (animationThread !=null) {
            try {
                animationThread.join(100);
            } catch (InterruptedException e) {}
            animationThread = null;
        }
    }

    public boolean action(Event e, Object arg) {
        if (e.target instanceof Button) {
            switch ((String)arg) {
                case "Exit":
                    terminateThreads();
                    hide();
                    removeAll();
                    dispose();
                    if (!is_applet) System.exit(0);
                    break;
                case "Span Ball":
                    animationHandler.spanBall(new Ball(Math.random() * 10 + 7));
                    break;
            }
        }
        return true;
    }
}

