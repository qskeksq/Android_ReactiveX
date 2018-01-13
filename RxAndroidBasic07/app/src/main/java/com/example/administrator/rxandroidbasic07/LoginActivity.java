package com.example.administrator.rxandroidbasic07;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableLastMaybe;


public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private Button btnSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        btnSign.setEnabled(false);


        // 뷰로 발행자를 등록
        Observable<TextViewTextChangeEvent> idEmitter = RxTextView.textChangeEvents(editEmail);
        Observable<TextViewTextChangeEvent> pwEmitter = RxTextView.textChangeEvents(editPassword);

        // zip 처럼 두개의 이벤트, 값을 묶어줌 -- RxBinding 에서 반응형, Binding 은 여러 뷰를 묶어서 동시에 이벤트를 실행해주는 것
        Observable.combineLatest(idEmitter, pwEmitter,
                (idEvent, pwEvent) -> {
                    boolean checkId = Patterns.EMAIL_ADDRESS.matcher(idEvent.text()).matches() && idEvent.text().length() >= 5;
                    boolean checkPw = pwEvent.text().length() >= 8;
                    return checkId&&checkPw;
                }
                // 글자 수에 따라서 자동으로 버튼의 잠김이 풀리게 할 수 있다. 이런 것을 반응형이라고 하는 것임
        ).subscribe(
                flag -> btnSign.setEnabled(flag)
        );


        // 직접 등록
        Observable<View> observable = Observable.just(editEmail, editPassword);
        observable.subscribe(
                item ->
        );

        EditText[] editTexts = {editEmail, editPassword};
        Observable<EditText> observable1 = Observable.fromArray(editTexts);
        observable1.subscribe(
                item ->
        );

        Observable<View> observable1 = Observable.zip(
                Observable.just(editEmail),
                Observable.just(editPassword),
                (item1, item2) -> item1 + "" + item2
        );

        Observable<String> observableZip = Observable.zip(                                 // 옵저버가 어떤 행위를 하는 것이 아니라 만들 때부터 어떤 형식으로 출력될 것인지를 알아야 한다.
                Observable.just("JoNadan", "Bewhy"),
                Observable.just("Programmer", "Rapper"),
                (item1, item2) -> "name:" + item1 + ", job:" + item2
        );
    }

    private void initView() {
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnSign = findViewById(R.id.btnSign);
    }
}

