package com.example.administrator.rxandroidbasic01;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-18.
 */

public class Subject extends Thread {

    boolean runFlag = true;
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void run() {
        while(runFlag) {
            try {
                say("Hello");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addObserver(Observer observer){
        observers.add(observer);
    }

    public void say(String phrase){
        for(Observer observer : observers){
            observer.notification(phrase);
        }
    }

    public  interface Observer {
        void notification(String phrase);
    }

}
