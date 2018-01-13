package com.example.administrator.servernodejs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.servernodejs.domain.Bbs;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class WriteActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editAuthor;
    private EditText editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        initView();
    }

    private void initView() {
        editTitle = (EditText) findViewById(R.id.editTitle);
        editAuthor = (EditText) findViewById(R.id.editAuthor);
        editContent = (EditText) findViewById(R.id.editContent);
    }

    public void post(View view){
        String title = editTitle.getText().toString();
        String author = editAuthor.getText().toString();
        String content = editContent.getText().toString();
        postData(title, author, content);
    }

    private void postData(String title, String author, String content) {
        // 입력할 객체 생성
        Bbs bbs = new Bbs(title, author, content);

        // 1. 레트로핏 생성
        Retrofit client = new Retrofit.Builder()
                .baseUrl(IBbs.SERVER)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 2. 서비스 연결
        IBbs myBbs = client.create(IBbs.class);

        // 3. 서비스의 특정 함수 호출
        Gson gson = new Gson();
        // bbs 객체를 수동으로 전송하기 위히서는
        // bbs 객체 -> json String 변환
        // RequestBody 에 미디어타입과, String  으로 변환된 데이터를 담아서 전송
                                                                // 만약 음악을 보내고 싶으면 audio/mpeg 로 하면 됨, 사실 모든 데이터가 byteArray 로 만들어져서 넘어가는 것임. 받는 측에서는 형식에 맞춰서 바이트를 읽어내는 것이다
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bbs));
        Observable<ResponseBody> observable = myBbs.write(requestBody);

        // 4. subscribe 등록
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBody -> {
                            String reuslt = responseBody.string();
//                            Intent intent = getIntent();
                            // 여기서 인텐트를 담아서 setResult 를 하면 onActivityResult 에서 Intent data 에서 꺼내 쓰기 위해 해 주는 것인데
                            // 만약 전달할 것이 없다면 그냥 result code 만 전달해 주면 되는 것이다.
//                            setResult(RESULT_OK, intent);
                            setResult(RESULT_OK);
                            finish();   // finish 는 생명주기 onDestroy 처리를 해 준다
                            // ***** 참고로 setResult 할 때 onActivityResult 가 호출되는 게 아니라 finish 가 되어야 onActivityResult 가 호출된다.*****
                            // setResult 는 intent 에 값만 넣어두는 것임.
                        }
                );
    }
}
