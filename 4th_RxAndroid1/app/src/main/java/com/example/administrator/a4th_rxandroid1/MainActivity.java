package com.example.administrator.a4th_rxandroid1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * ReactivtX에 대하여
 * 어려워 보여도 결국 하는 일은 단순하다. 마치 서버가 복잡해 보여도 request, response 요청과 응답이 전부인 것과 비슷하다
 *
 * 결국 데이터를 갖다가 어떻게 전달할지 문제이고, 거기서 데이터를 받아오는 곳과 받아온 데이터를 갖다가 사용할 곳을 정하고,
 * 언제 받아올지 정하면 되는 것이다. 받아올 곳이 등록될 때 받아올지, 데이터를 다 받아오면 보내줄지 정해주면 된다.
 * 데이터를 받아오는 곳을 Observable이라고 하고, 받아 쓰는 곳을 Subscriber라고 한다. 기존 Observer 패턴과 다른 점은
 * Observable의 인터페이스에서 onNext()외에 onComplete(), onError()가 존재한다는 것(Subscriber가 구현해야 하기 때문에
 * Subcriber가 가지고 있다고 해도 무방하다)
 *
 * ##### Realm.io 문서 설명 #####
 * Observable과 Subscriber를 주목하세요. 데이터의 강을 만드는 옵저버블(Observable)과 강에서 데이터를 하나씩 건지는
 * 서브스크라이버(Subscriber)가 리액티브 프로그래밍의 가장 핵심적인 요소입니다.
 * 옵저버블은 데이터를 제공하는 생산자로 세가지 유형의 행동을 합니다.
 * - onNext - 새로운 데이터를 전달한다.
 * - onCompleted - 스트림의 종료.
 * - onError - 에러 신호를 전달한다
 *
 *  Observable<String> simpleObservable =
        Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("Hello RxAndroid !!");
            subscriber.onCompleted();
        }
    });

    simpleObservable.subscribe(new Subscriber<String>() {
        @Override
        public void onCompleted() {
            Log.d(TAG, "complete!");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "error: " + e.getMessage());
        }

        @Override
        public void onNext(String text) {
            ((TextView) findViewById(R.id.textView)).setText(text);
        }
    });
 */

public class MainActivity extends AppCompatActivity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 발행자 생성 1. Observable, Subject
        Subject subject = new Subject();
        subject.start();

        findViewById(R.id.button).setOnClickListener(view -> {
            count++;
            String myName = "Observer" + count + ":";
            Subject.Observer subject1 = phrase -> {
                System.out.println(myName+phrase);
            };

            subject.addObserver(subject1);
        });
    }
}
