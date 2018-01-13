package com.example.administrator.a4th_rxandroid1;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject : 발행자 클래스
 */
public class Subject extends Thread {

    // 옵저버 목록
    int count = 0;
    boolean runFlag = true;
    private List<Observer> observers = new ArrayList<>();

    // 실행코드
    @Override
    public void run() {
        while(runFlag) {
            try {
                // 이곳은 서버에서 데이터를 받아오는 곳
                say("Hello"+(count++));
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


    // 옵저버 인터페이스
    public  interface Observer {
        void notification(String phrase);
    }


}
