package com.example.administrator.servernodejs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.example.administrator.servernodejs.domain.Bbs;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class MainActivity extends AppCompatActivity {

    // php, spring(톰캣), dotnet 형태의 서버를 개발해 보아야 한다.

    private RecyclerView recycler;
    List<Bbs> data;
    RecyclerAdapter adapter;
    private Button write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 처음에는 savedInstanceState 가 null 이고 그 다음부터는 null 이 아닌 bundle 이 넘어온다
        setContentView(R.layout.activity_main);
//        야메이긴 한데 이렇게 해 주면 된다.
//        if(savedInstanceState != null)
//            return;
//        이렇게 야메로 하지 않으려면 onSavedInstanceState 와 restoreSavedInstanceState 로 해 주면 된다.
        lambdaTest();
        initView();
        data = new ArrayList<>();
        adapter = new RecyclerAdapter(data, this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        loader();
    }

    private void lambdaTest() {
        new Thread(() -> Log.i("Lambda", "running =============== ok")).start();
    }

    private void initView() {
        recycler = (RecyclerView) findViewById(R.id.recycler);
        write = (Button) findViewById(R.id.write);
        write.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WriteActivity.class);
            startActivityForResult(intent, 999);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // 이런식으로 좀 생각을 하면서 하자. 하나밖에 없으니까 RESULT_OK 만 확인하면 된다.
            this.data.clear();  // 중복되는 데이터가 있다면 갱신하지 않는 방법으로 생각해본다.
            loader();
        }
    }

    private void loader() {
        // 0. 퍼미션

        // 1. 레트로핏 생성
        Retrofit client = new Retrofit.Builder()
                .baseUrl(IBbs.SERVER)
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 2. 서비스 연결
        IBbs myBbs = client.create(IBbs.class);

        // 3. 서비스의 특정 함수 호출
        Observable<ResponseBody> observable = myBbs.read();

        // 4. subscribe 등록
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBody -> {
                            //  데이터를 꺼내고
                            String jsonString = responseBody.string();
                            Log.e("Retrofit", jsonString);  // 한 번 쓰면 다시 못 쓰게 닫히게 되어 있다. 그리고 습관이 잘못 되어 있는게, 로그를 아래쪽에 찍고 재사용 했어야지 갖다 쓰면 어떡하냐.
                            //Gson gson = new Gson();
//                            Type type = new TypeToken<List<Bbs>>(){}.getType(); // 컨버팅 하기 위한 타입 지정
//                            Bbs datas[] = gson.fromJson(jsonString, type);
                            //Bbs[] datas = gson.fromJson(jsonString, Bbs[].class); // GsonConverter 해주면 responsebody 안 하고 자동으로 다 해서 넘겨준다. 즉 이 1-2줄 정도를 gsonconverter 가 해 주는 것임
                            // 그리고 원래는 bbs list 를 가진 클래스를 만들어야 하지만 이렇게 할 수 있다는 것을 알 수 있다
                            Bbs[] datas = new Gson().fromJson(jsonString, Bbs[].class);

                            if (datas == null) {
                                Log.e("NADAN", "null 입니다.");
                            } else {
                                Log.e("NADAN", datas.length + "");
                            }
                            // 어댑터를 세팅하고
                            for (Bbs bbs : datas) {
                                this.data.add(bbs);
                            }
                            // 어댑터 갱신
                            adapter.notifyDataSetChanged();
                        }
                );
    }

    interface IUser {

    }
}
