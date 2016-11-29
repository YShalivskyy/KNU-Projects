package com.ys.tasks.Applet7;

import java.awt.*;

public class Animator extends Canvas implements Runnable
{
    final int max = 200;
    int num;                      // number of balls
    int cur,mx,my;                // current ball and mouse x y
    Ball[] ball = new Ball[max];  // array of balls
    long startingTime;

    //  The following are some "physical" properties.  Each property
    //  has a value and a control.  The values are updated once per
    //  animation loop (this is for efficiency).

    public double g,f,r;
    public boolean col;
    public int xsize,ysize;
    Scrollbar fric,rest;

    //  The ctor method creates initial objects.
    Animator(Panel p) {
        setBackground(Color.black);
        fric = new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,20);
        p.add(new Label("viscosity"));
        p.add(fric);
        rest = new Scrollbar( Scrollbar.HORIZONTAL,17,1,0,20);
        p.add(new Label("restitution"));
        p.add(rest);
        //  Add two balls
        addBall(new Ball(100,10, 100,100,0.48,0, Color.blue));
        addBall(new Ball(16,4,100,50,-3,0,Color.cyan));
        my = -17;       // mouse up
        startingTime = System.currentTimeMillis();
    }

    // The run method updates the locations of the balls.
    // from Runnable
    public void run() {
        while (true) {
            if (System.currentTimeMillis() > startingTime + 16) //1000 / 16 = 62 fps
            {
                startingTime = System.currentTimeMillis();
                readControls();
                for (int i = 0; i < num - 1; ++i)
                    for (int j = i + 1; j < num; ++j)
                        ball[i].interact(ball[j], this);
                for (int i = 0; i < num; ++i)
                    ball[i].update(this);
                if (my != -17)              // mouse is dragging
                {
                    ball[cur].vx = (mx - ball[cur].x) / 10;
                    ball[cur].vy = (my - ball[cur].y) / 10;
                }
                //Repaints this component, and causes a call to the paint method.
                repaint();
            }
            else {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {}
            }
        }
    }

    //  Read user input and cache in local vars (for efficiency)

    private void readControls()
    {
        f = fric.getValue()/20.0;
        r = rest.getValue()/20.0;
        col = true;
        xsize = size().width;
        ysize = size().height;
    }

    //  The paint method displays objects.
    public void paint(Graphics g)
    {
        for (int i=0; i<num; ++i)
            ball[i].draw(g);
    }

    public void update(Graphics g)
    {
        g.clearRect(0,0,xsize,ysize);
        paint(g);
    }

    //  These add and delete balls.

    public void addBall(Ball b)
    {
        if (num<max) ball[num++] = b;
    }

    public void delBall() {
        if (num>0) {
            if (cur<num) ball[cur] = ball[num-1];
            ball[num--] = null;
        }
    }

    //  The following mouse methods allow you to drag balls.
    public boolean mouseDown(Event e, int x, int y)
    {
        cur = nearestBall(x,y,num);
        mx = x; my = y;
        return true;
    }
    public boolean mouseDrag(Event e, int x, int y)
    {
        mx = x; my = y;
        return true;
    }
    public boolean mouseUp(Event e, int x, int y)
    {
        my = -17;         // this magic number means that the mouse is up
        return true;
    }

    //  This returns the index of the ball nearest to x,y

    int nearestBall(int x, int y, int ex)
    {
        double d=1e20,t; int j=0;
        for (int i=0; i<num; ++i)
        {
            t = Ball.hypot(x - ball[i].x, y - ball[i].y);
            if (t < d && i!=ex)
            {
                d = t; j = i;
            }
        }
        return j;
    }
}