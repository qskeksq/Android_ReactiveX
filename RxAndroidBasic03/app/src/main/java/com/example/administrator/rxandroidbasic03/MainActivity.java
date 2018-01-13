package com.example.administrator.rxandroidbasic03;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    List<String> data = new ArrayList<>();
    //    ArrayAdapter<String> adapter;
    CustomAdapter adapter;
    Observable<String> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        adapter = new CustomAdapter(data, this);
        list.setAdapter(adapter);

        // 날짜를 문자로 리턴해주기
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

//        observable = Observable.just("월", "화", "수", "목", "금");
        // create 로 해주는 이유는 괄호 안에 함수를 넣어주기 위해서이고, defer 보다는 create 가 편하기 때문. 일단 Observable 구현 방법을 잘 알아야 한다.
        observable = Observable.create(emitter -> {
            for (String month : months) {
                emitter.onNext(month);
                Thread.sleep(1000);
            }
            emitter.onComplete();
        });
    }

    private void initView() {
        list = findViewById(R.id.list);
    }

    public void doAsync(View view) {

        observable.subscribeOn(Schedulers.io())    // Observable 에 스레드를 등록해 주는 것으로 데이터를 주고 받는 io 스레드에 등록해 준다.
                // 'io.reactivex.rxjava2:rxandroid:2.0.1' 로 그래들에 추가해 줘야 한다
                .observeOn(AndroidSchedulers.mainThread())        // UI 는 메인 스레드에서밖에 할 수 없기 때문에 옵저버를 메인 스레드에 등록시켜 준다.
                // 데이터를 받아오는 것은 서브스레드(Observable)에서 하고 기다리다가 데이터를 받아서 화면에 등록해 주는 것은 옵저드를이 메인에서 하는 것이다
                // retrofit 이 스레드 처리해주는 것과 비교해 보자
                // 네트워크와 연관해서 생각해보면 subscribeOn 에서 즉 서브스레드에서 네트워크 처리를 해 주고 등록된 옵저버들이 데이터를 받아서 뷰에 뿌려주는 것이겠다
                .subscribe(
                        str -> {
                            // 매번 데이터를 가져올 때마다 출력하기 위해 데이터를 받는 곳에서 받을 때마다 갱신해준다.
                            data.add(str);
                            adapter.notifyDataSetChanged();
                        },
                        e -> Log.e("Error", e.getMessage()),
                        () -> {
                            data.add("Complete");
                            adapter.notifyDataSetChanged();
                        }
                );
    }
}

class CustomAdapter extends BaseAdapter {

    List<String> data;
    LayoutInflater inflater;

    public CustomAdapter(List<String> data, Context context) {
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private int lastPosition = -1;


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 이 코드 잘 익혀두자
        if(i > lastPosition){
            view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(data.get(i));
        } else {
            return view;
        }
        return view;
    }
}
