package com.example.administrator.rxandroidbasic01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Subscriber1 sub1 = new Subscriber1();
//        Subscriber2 sub2 = new Subscriber2();

        Subject subject = new Subject();
        subject.start();

        // 이렇게 하는 게 아니라
//        findViewById(R.id.button).setOnClickListener(view->new Subscriber());

        findViewById(R.id.button).setOnClickListener(view -> {
            count++;
            subject.addObserver(new Subject.Observer() {
                String myName = "Observer" + count + ":";
                @Override
                public void notification(String phrase) {
                    // 당연하게도 여기 안으로 count 값이 들어가면 모든 노티피케이션이 바뀔 수 밖에 없다.
                    System.out.println(myName+phrase);
                }
            });
        });
    }
}
