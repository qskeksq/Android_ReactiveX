package com.example.administrator.a4th_rxandroid3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. editText에 입력되는 값을 체크해서 실시간으로 Log 를 뿌려준다
        RxTextView.textChangeEvents((EditText)findViewById(R.id.editText))
                .subscribe(ch->{
                    Random random = new Random();
                    Log.e("RxBinding", "ch");
                });

        // 2. 버튼을 클릭하면 editText에 Random 숫자를 입력
        RxView.clicks((Button)findViewById(R.id.button))
                .map(button->new Random().nextBoolean()+"")
                .subscribe(number->{
                    ((EditText)findViewById(R.id.editText)).setText(number);
                });

    }
}
