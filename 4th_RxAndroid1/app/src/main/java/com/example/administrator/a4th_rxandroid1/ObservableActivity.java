package com.example.administrator.a4th_rxandroid1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ObservableActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter adapter;
    private RecyclerView recycler;
    List<String> temp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observable);


        recycler = (RecyclerView) findViewById(R.id.recycler);
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] data = {"JAN", "FEB", "MAR", "APRIL", "MAY", "JUNE", "JULY", "AUGUST"};

        // 1. 발행자 생성
        Observable<String> observable = Observable.fromArray(data);

        // 2. 구독자
        observable.subscribe(
                // onNext() 데이터 있으면 호출
                new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        temp.add(s);
                    }
                },
                // onError() 호출
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                },
                // onComplete() 호출
                new Action() {
                    @Override
                    public void run() throws Exception {
                        adapter.setdata(temp);
                    }
                }
        );

        // 2. 구독자 - 람다버전
        observable.subscribe(
                s -> temp.add(s),
                e -> { },
                () -> adapter.setdata(temp)
        );

        // Just
        Observable<String> observableJust = Observable.just("JAN", "FEB", "MARCH");
        observableJust.subscribe(str -> temp.add(str));

        // Defer
        Observable<String> observableDefer = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just("JAN", "FEB", "MARCH");
            }
        });
        observableDefer.subscribe(str -> temp.add(str));
    }


    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

        List<String> data = new ArrayList<>();

        public void setdata(List<String> once){
            this.data = once;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView textView;

            public Holder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }

}


