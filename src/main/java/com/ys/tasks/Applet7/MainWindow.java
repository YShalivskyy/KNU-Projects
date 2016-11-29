package com.ys.tasks.Applet7;

import java.awt.*;

public class MainWindow extends Frame
{
    private Animator anim;		//object that draws on canvas
    private Thread anim_thread;	//thread running Animator
    private boolean is_applet;

    //  This method creates layout and main objects.
    MainWindow(String title, boolean isapp) {
        super(title);
        is_applet = isapp;
        Panel p = new Panel();      // control panel
        Label n = new Label("Balls applet");
        p.setLayout(new GridLayout(0,1));   // vertical layout
        p.setFont(new Font("Helvetica",Font.PLAIN,10));
        n.setFont(new Font("TimesRoman",Font.BOLD,16));
        p.add(n);
        p.add(new Button("add"));
        p.add(new Button("delete ball"));
        p.add(new Button("quit"));
        anim = new Animator(p);
        setLayout(new BorderLayout(2,2));
        add("Center",anim);
        add("West",p);
    }

    public void start() {
        if (anim_thread==null) {
            anim_thread = new Thread(anim);
            anim_thread.start();        // start new thread
        }
    }

    public void stop() {
        if (anim_thread!=null) {
            try {
                anim_thread.join(100);
            } catch (InterruptedException e) {}
            anim_thread = null;
        }
    }

    public boolean action(Event e, Object arg) {
        if (e.target instanceof Button) {
            switch ((String)arg) {
                case "delete ball":
                    anim.delBall();
                    break;
                case "quit":
                    stop();
                    hide();
                    removeAll();
                    dispose();
                    if (!is_applet) System.exit(0);
                    break;
                case "add":
                    anim.addBall(new Ball(Math.random() * 10 + 5));
                    break;
            }
        }
        return true;
    }
}

