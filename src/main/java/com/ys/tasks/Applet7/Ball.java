package com.ys.tasks.Applet7;

import java.awt.*;

class Ball
{
    double x,y;            // location
    double z;              // radius
    double vx,vy;          // velocity
    double m;              // mass
    boolean hit;           // scratch field
    double ox,oy;          // old location (for smooth redraw)
    final double vmin = 1e-20;      // a weak force to prevent overlapping
    Color c;               // color

    Ball(double mass, double radius,
         double px, double py,
         double sx, double sy, Color color) {

        z = Math.max(radius, 0.5);
        m = Math.abs(mass);
        x = px;
        y = py;
        vx = sx;
        vy = sy;
        c = color;
    }

    Ball(double radius) {
        z = Math.max(radius, 0.5);
        m = Math.PI * Math.pow(z, 2);
        x = 0;
        y = 0;
        vx = 0;
        vy = 0;
        float r = (float)Math.random();
        float g = (float)Math.random();
        float b = (float)Math.random();
        c = new Color(r, g, b);
    }

    public void update(AnimationHandler a) {
        x += vx;
        if (x+z > a.xsize) {
            if (vx > 0) vx *= a.r;          // restitution
            vx = -Math.abs(vx) - vmin;        // reverse velocity
            hit = true;
            //  Check if location is completely off screen
            if (x - z > a.xsize) x = a.xsize + z;
        }
        if (x-z < 0) {
            if (vx < 0) vx *= a.r;
            vx = Math.abs(vx) + vmin;
            hit = true;
            System.out.println();
            if (x + z < 0) x = -z;
        }
        y += vy;
        if (y+z > a.ysize) {
            if (vy > 0) vy *= a.r;
            vy = -Math.abs(vy) - vmin;
            hit = true;
            if (y - z > a.ysize) y = a.ysize + z;
        }
        if (y-z < 0) {
            if (vy < 0) vy *= a.r;
            vy = Math.abs(vy) + vmin;
            hit = true;
            if (y + z < 0) y = -z;
        }
        if (a.f > 0 && m != 0)            // viscosity
        {
            double t = 100/(100 + a.f*hypot(vx,vy)*z*z/m);
            vx *= t; vy *= t;
        }
        if (!hit) vy += a.g;        // if not hit, exert gravity
        hit = false;                // reset flag
    }

    //  This computes the interaction of two balls, either collision
    //  or gravitational force.
    //  Returns TRUE if ball mainWindow should be deleted.

    public boolean collisionInteract(Ball b, AnimationHandler a)
    {
        double p = b.x - x;
        double q = b.y - y;
        double h2 = p*p + q*q;
        double h = Math.sqrt(h2);
        if (h < z+b.z)                  // HIT
        {
            hit = b.hit = true;
            if (h > 1e-10) {

                //  Compute the elastic collision of two balls.
                double v1, v2, r1, r2, s, t, v;
                p /= h;
                q /= h;              // normalized impact direction
                v1 = vx * p + vy * q;
                v2 = b.vx * p + b.vy * q;         // impact velocity
                r1 = vx * q - vy * p;
                r2 = b.vx * q - b.vy * p;         // remainder velocity
                if (v1 < v2) return false;
                s = m + b.m;                  // total mass
                if (s == 0) return false;

                t = (v1 * m + v2 * b.m) / s;
                v = t + a.r * (v2 - v1) * b.m / s;
                vx = v * p + r1 * q;
                vy = v * q - r1 * p;
                v = t + a.r * (v1 - v2) * m / s;
                b.vx = v * p + r2 * q;
                b.vy = v * q - r2 * p;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        g.setColor(c);
        g.drawOval((int)(x-z),(int)(y-z),(int)(2*z),(int)(2*z));
        ox = x; oy = y;           // save new location
    }

    static double hypot(double x,double y) {
        return Math.sqrt(x*x + y*y);
    }
}
