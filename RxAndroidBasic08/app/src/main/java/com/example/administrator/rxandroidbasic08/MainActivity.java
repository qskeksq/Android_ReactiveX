package com.example.administrator.rxandroidbasic08;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.rxandroidbasic08.domain.Data;
import com.example.administrator.rxandroidbasic08.domain.Row;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    // http://openAPI.seoul.go.kr:8088/(인증키)/xml/RealtimeWeatherStation/1/5/중구
    // 414478706c71736b39384775654e67

    public static final String SERVER = "http://openAPI.seoul.go.kr:8088/";
    public static final String SERVER_KEY = "414478706c71736b39384775654e67";
    Retrofit client;
    IWeather service;
    Observable<Data> observable;
    private RecyclerView recyclerView;
    WeatherAdapter adapter;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. 레트로핏 생성
        client = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 2. 서비스 생성 -- 서비스로부터 여러 길을 선택하는 것이다. 여러개의 기능을 포함해서 여기서 기능을 꺼내 사용한다.
        service = client.create(IWeather.class);

//        // 3. 옵저버블 생성 -- addCallAdapterFactory 을 넣어줬기 때문에 Observable 을 사용할 수 있다. 참고로 현재 Observable 는 인터넷 주소를 가지고 있다.
////                          -- addConverterFactory 을 넣어줘서 데이터가 Data 클래스로 변환되고, 밑에 구독자 subscribeOn 에 Data 객체로 넘어간다.
//        Observable<Data> observable = service.getData(SERVER_KEY, 1, 10, "서초");
//
//        // 4. 발행 시작
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        data -> {
//                            Row rows[] = data.getRealtimeWeatherStation().getRow();
////                            for(Row row : rows){
////                                Log.i("Weather", "지역명="+row.getSTN_NM());
////                                Log.i("Weather", "온도="+row.getSAWS_TA_AVG());
////                                Log.i("Weather", "습도="+row.getSAWS_HD());
////                            }
//                            list = new ArrayList<>();
//                            Log.i("Weather", "개수="+data.getRealtimeWeatherStation().getList_total_count());
//                        }
//
//                );
        initView();
        adapter = new WeatherAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        editText = (EditText) findViewById(R.id.editText);
    }

    /**
     * 옵저버블 생성
     */
    public void setObservable(View view) {
        String region = editText.getText().toString();
        observable = service.getData(SERVER_KEY, 1, 10, region);
    }

    /**
     * 구독 시작
     */
    public void getObservable(View view) {
        new Thread() {
            @Override
            public void run() {
                observable.subscribeOn(Schedulers.io())     // 여기서 서브스레드로 빼주지 않으면 네크워크 오류 생긴다.
                        .observeOn(AndroidSchedulers.mainThread())  // 참고로 로그 찍을 때는 Schedulers.newThread() 로 해줘도 됨
                        .subscribe(
                                data -> {
                                    // 원래는 data.getRealtimeWeatherStation().getRESULT.getCODE 로 하려고 했는데, 이게 구조가 잘못 짜여져서
                                    // data 에서 null 값 처리 해줘야 하는데, data 자체가 null 값으로 반환되지 않고, data.getRealtimeWeatherStation 에서
                                    // null 값이 리턴된다.
                                    if(data.getRealtimeWeatherStation() == null){
                                        Toast.makeText(MainActivity.this, "해당하는 데이터가 없습니다", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, data.getRealtimeWeatherStation().getRESULT().getMESSAGE(), Toast.LENGTH_SHORT).show();
                                        List<String> content = new ArrayList<>();
                                        Row[] rows = data.getRealtimeWeatherStation().getRow();
                                        for (Row row : rows) {
                                            content.add(row.getSAWS_OBS_TM());
                                            content.add(row.getSTN_NM());
                                            content.add(row.getSTN_ID());
                                            content.add(row.getSAWS_TA_AVG());
                                            content.add(row.getSAWS_HD());
                                            content.add(row.getCODE());
                                            content.add(row.getNAME());
                                            content.add(row.getSAWS_WS_AVG());
                                            content.add(row.getSAWS_RN_SUM());
                                            content.add(row.getSAWS_SOLAR());
                                            content.add(row.getSAWS_SHINE());
                                        }
                                        adapter.setDat(content);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                        );
            }
        }.start();

    }
}

interface IWeather {    // 복사할 때 xml 을 json 으로 바꿔주자 // 만약 뒤에서 지역명을 뺴면 앞에서 10개만 보여준다.
    @GET("{key}/json/RealtimeWeatherStation/{start}/{count}/{name}")
        // 여기와 SERVER 가 합쳐져셔 온전한 주소가 된다.
    Observable<Data> getData(   // 여기로 들어온 인자가 위의 {} 로 들어간다, 어노테이션 Path 로 맵핑 시켜주는 것이다.
                                @Path("key") String server_key
            , @Path("start") int begin_index
            , @Path("count") int offset
            , @Path("name") String gu);
}