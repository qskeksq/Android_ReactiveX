package com.example.administrator.rxandroidbasic06;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰에다가 RxJava 를 바인딩해서 쓰는 방법    --  항상 뭔가 새롭게 사용하는 것들은 안에 들어가고, 내부적 동작 방법을 알아봐라
        RxTextView.textChangeEvents(findViewById(R.id.editText))
                .subscribe(                                         // 실제로 텍스트 꺼내기
                        item -> Log.i("Word", "String="+item.text().toString())
                );

        RxView.clicks(findViewById(R.id.btnRandom))
                .map(event -> new Random().nextInt())
                .subscribe(
                        number -> ((TextView)findViewById(R.id.textView)).setText("random number="+number)
                );
    }
}
