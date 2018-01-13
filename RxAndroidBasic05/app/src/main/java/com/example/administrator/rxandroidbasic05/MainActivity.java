package com.example.administrator.rxandroidbasic05;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    PublishSubject<String> publishSubject = PublishSubject.create();

    // 발행 -- 즉, 데이터를 가져오는 쪽
    public void doPublish(View view) {
//        // 발행
//        publishSubject.onNext("A");
//        publishSubject.onNext("B");
//        publishSubject.onNext("C");
//
//        // 구독
//        publishSubject.subscribe(
//                item -> Log.i("Publish", "item="+item)
//        );
//
//        // 당연하지만 구독한 다음부터 데이터를 가져온다. A,B,C 는 안 찍힘
//        publishSubject.onNext("D");
//        publishSubject.onNext("E");
//        publishSubject.onNext("F");
//        publishSubject.onNext("G");
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    //이렇듯 매뉴얼대로 하다가 보면 안 되는 경우가 있다. 이럴 경우부터 개발자가 필요한 것이다. 스레드가 안 되면 직접 스레드를 만들면 되는 것이다.
                    //publishSubject.subscribeOn(Schedulers.io());
                    Log.i("Publish", "A" + i);
                    publishSubject.onNext("A" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    // 구독   -- 구독하는 시점이 각 다르군
    public void getPublish(View view) {
        publishSubject.observeOn(AndroidSchedulers.mainThread());
        publishSubject.subscribe(
                item -> Log.i("Publish", "item=" + item)
        );
    }

    BehaviorSubject<String> behaviorSubject = BehaviorSubject.create();
    public void doBehavior(View view) {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    //이렇듯 매뉴얼대로 하다가 보면 안 되는 경우가 있다. 이럴 경우부터 개발자가 필요한 것이다. 스레드가 안 되면 직접 스레드를 만들면 되는 것이다.
                    //publishSubject.subscribeOn(Schedulers.io());
                    Log.i("Behavior", "B" + i);
                    behaviorSubject.onNext("B" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void getBehavior(View view){
        behaviorSubject.subscribe(
                item -> Log.i("Behavior", "item="+item)
        );
    }

    ReplaySubject<String> replaySubject = ReplaySubject.create();
    public void doReplay(View view) {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    //이렇듯 매뉴얼대로 하다가 보면 안 되는 경우가 있다. 이럴 경우부터 개발자가 필요한 것이다. 스레드가 안 되면 직접 스레드를 만들면 되는 것이다.
                    //publishSubject.subscribeOn(Schedulers.io());
                    Log.i("Behavior", "C" + i);
                    replaySubject.onNext("C" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void getReplay(View view){
        replaySubject.subscribe(
                item -> Log.i("Replay", "item="+item)
        );
    }

    // 시점과 상관 없이 완료된 후 마지막 항목 출력
    AsyncSubject<String> asyncSubject = AsyncSubject.create();
    public void doAsync(View view) {
        new Thread ( () ->{
                for (int i = 0; i < 10; i++) {
                    //이렇듯 매뉴얼대로 하다가 보면 안 되는 경우가 있다. 이럴 경우부터 개발자가 필요한 것이다. 스레드가 안 되면 직접 스레드를 만들면 되는 것이다.
                    //publishSubject.subscribeOn(Schedulers.io());
                    Log.i("Behavior", "D" + i);
                    asyncSubject.onNext("D" + i);
                    try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                asyncSubject.onComplete();
            }
        ).start();
    }

    public void getAsync(View view){
        asyncSubject.subscribe(
                item -> Log.i("Async", "item="+item)
        );
    }

}
