package com.example.administrator.a4th_rxandroid2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    List<String> month = new ArrayList<>();

    String monthString[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        CustomAdapter adapter = new CustomAdapter();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // 1월부터 12월 가져오기
        DateFormatSymbols dfs = new DateFormatSymbols();
        monthString = dfs.getMonths();

        // 1. 발행자
        Observable<String> observable = Observable.create(e -> {
            try{
                for(String month1 : monthString){
                    e.onNext(month1);
                    Thread.sleep(1000);
                }
                e.onComplete();
            } catch (Exception ex){
                throw ex;
            }
        });

        // 2. 구독자 - onNext()가 호출될 때 넘겨준 값이 str로 넘어온다
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                str -> {
                    month.add(str);
                    adapter.setdata(month);
                }
        );
        observable.subscribe(str -> month.add(str));

    }

    private void initView() {
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

        List<String> data = new ArrayList<>();

        public void setdata(List<String> once) {
            this.data = once;
            notifyDataSetChanged();
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
