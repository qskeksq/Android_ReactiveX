package com.example.administrator.rxandroidbasic01;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.functions.Consumer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 사용하기 전에 gradle 에 dependency 추가
 */
public class ObservableActivity extends AppCompatActivity {

    Observable<String> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observable);

    }

    // 옵저버블 생성 -- 발행해 주는 존재(Subject 와 비슷한 존재, 어떠한 함수가 돌아가는 곳
    private void createObservable(){
//        observable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext("Hello Android");
//                e.onNext("Good to see you");
//                e.onComplete();
//            }
//        });
        // 람다에서 예외처리를 할 수 없다고?
        observable = Observable.create(emitter -> {
            // 이 안에서 무한 루프를 돌면서 emitter onNext 호출해 준다.
            emitter.onNext("Hello Android");
            emitter.onNext("Good to see you");
            emitter.onComplete();
        });
    }

    // 옵저버 등록
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void bindObserver(){
//        1. 방법1
//       Subscriber<String> subscriber = new Subscriber<String>() {
//            @Override
//            public void onSubscribe(Subscription s) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };
//       observable.subscribe();
//       observable.
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                Log.e("onNext", value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.e("onComplete", "완료");
            }
        };
        observable.subscribe(observer);

// 2. 방법2 2번 형태 람다식을 사용하기 전단계로 옵저버 내에 있는 함수들을 하나씩 분리한다.
//        Consumer<String> onNext = new Consumer() {
//            @Override
//            public void accept(Object o) {
//
//            }
//        };
//
//        Consumer<Throwable> onError = new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) {
//
//            }
//        };
//
//        observable.subscribe(onNext, onError);
//

//        3. 방법 3
//        위 코드와 동일하다
//        observable.subscribe(
//                str       ->Log.e("Onext", "======"+str),
//                throwable ->Log.e("OnError", "xxxxxxxx"+throwable.getMessage()),
//                ()        ->Log.e("OnComplete", "oooooooo complete")
//        );

    }
}
