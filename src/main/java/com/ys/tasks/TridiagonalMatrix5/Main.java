package com.ys.tasks.TridiagonalMatrix5;


import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static List<Float[]> system = new ArrayList<>();
    private static Float[] a;
    private static Float[] b;
    private static Float[] c;
    private static Float[] d;
    private static Float[] x;
    static int M;

    /**
     * a c 0...0 d1
     * b a c 0.0 d2
     * 0 b a c 0 d3
     * 0...0 b a dn
     * a > c+b in every line
     * @throws IOException
     */

    private static void input() throws IOException {
        System.out.print("Enter system:\n");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String in = br.readLine();
        while (true) {
            int numberCount = 4;
            Float[] equation = new Float[numberCount];
            String[] items = in.split(" ");
            for (int i = 0; i < numberCount; i++) {
                if (system.size() == 0) {
                    if (i < numberCount - 1) {
                        equation[0] = 0f;
                        equation[i + 1] = new Float(items[i]);
                    }
                } else if (items[i].charAt(0) == '.') {
                    equation[i] = equation[i - 1];
                    equation[i - 1] = 0f;
                    equation[0]*=-1;
                    equation[2]*=-1;
                    system.add(equation);
                    return;
                } else equation[i] = new Float(items[i]);
            }
            equation[0]*=-1;
            equation[2]*=-1;
            system.add(equation);
            in = br.readLine();
        }
    }

    public static void main(String[] args) {
        try {
            input();
        } catch (IOException e) {
            System.out.print("Invalid input!");
            return;
        }

        M=system.size()/2;
        a = new Float[M+1];
        b = new Float[M+1];
        c = new Float[system.size()];
        d = new Float[system.size()];
        x = new Float[system.size()];

        Thread first = new Thread(new Right(system, a, b));
        first.start();
        Thread second = new Thread(new Left(system, c, d));
        second.start();
        try {
            first.join();
            second.join();
        }
        catch (InterruptedException e){
            System.out.print("Interrupted!");
            return;
        }

        x[M-1]=(b[M]+a[M]*d[M])/(1-c[M]*a[M]);
        x[M]=(d[M]+c[M]*b[M])/(1-c[M]*a[M]);

        Thread firstBack = new Thread(new RightBack(x, a, b));
        firstBack.start();
        Thread secondBack = new Thread(new LeftBack(x, c, d));
        secondBack.start();
        try {
            firstBack.join();
            secondBack.join();
        }
        catch (InterruptedException e){
            System.out.print("Interrupt!");
            return;
        }
        for(int i=0;i<x.length;i++){
            StringBuilder s=new StringBuilder("x");
            s.append(i);
            s.append(':');
            s.append(x[i]);
            System.out.println(s.toString());
        }
    }
}

class Right implements Runnable {

    private List<Float[]> system;
    private Float[] a;
    private Float[] b;

    Right(List<Float[]> s, Float[] a, Float[] b) {
        system = s;
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        a[1]=system.get(0)[2] / system.get(0)[1];
        b[1]=system.get(0)[3] / system.get(0)[1];
        for (int i = 1; i < a.length-1; i++) {
            a[i+1]=system.get(i)[2] / (system.get(i)[1] - system.get(i)[0] * a[i]);
            b[i+1]=(system.get(i)[0] * b[i] + system.get(i)[3]) /
                    (system.get(i)[1] - system.get(i)[0] * a[i]);
        }
    }
}

class RightBack implements Runnable {

    private Float[] x;
    private Float[] a;
    private Float[] b;

    RightBack(Float[] x, Float[] a, Float[] b) {
        this.x = x;
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        for (int i = a.length-3; i >= 0; i--) {
            x[i]=a[i+1]*x[i+1]+b[i+1];
        }
    }
}

class Left implements Runnable {

    private List<Float[]> system;
    private Float[] c;
    private Float[] d;

    Left(List<Float[]> s, Float[] c, Float[] d) {
        system = s;
        this.c = c;
        this.d = d;
    }

    @Override
    public void run() {
        c[c.length-1]=system.get(system.size()-1)[0] / system.get(system.size()-1)[1];
        d[c.length-1]=system.get(system.size()-1)[3] / system.get(system.size()-1)[1];
        for (int i = system.size()-2; i >= system.size() / 2; i--) {
            c[i]=system.get(i)[0] / (system.get(i)[1] - system.get(i)[2] * c[i + 1]);
            d[i]=(system.get(i)[2] * d[i + 1] + system.get(i)[3]) /
                    (system.get(i)[1] - system.get(i)[2] * c[i + 1]);
        }
    }
}

class LeftBack implements Runnable {

    private Float[] x;
    private Float[] c;
    private Float[] d;

    LeftBack(Float[] x, Float[] c, Float[] d) {
        this.x = x;
        this.c = c;
        this.d = d;
    }

    @Override
    public void run() {
        for (int i = c.length/2; i < c.length-1; i++) {
            x[i+1]=c[i+1]*x[i]+d[i+1];
        }
    }
}