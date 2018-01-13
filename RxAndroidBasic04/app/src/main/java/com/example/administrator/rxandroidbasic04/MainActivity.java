package com.example.administrator.rxandroidbasic04;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.observable.ObservableZip;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    List<String> data = new ArrayList<>();
    RecyclerAdapter adapter;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        emitData();
        adapter = new RecyclerAdapter(this, data);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
    }


    Observable<Integer> observable;
    Observable<String> observableZip;
    String[] months;

    public void emitData() {
        DateFormatSymbols dfs = new DateFormatSymbols();
        months = dfs.getMonths();

        observable = Observable.create(emitter -> {
            for (int i = 0; i < 12; i++) {
                emitter.onNext(i);
                Thread.sleep(1000);
            }
            emitter.onComplete();
        });
        observableZip = Observable.zip(                                 // 옵저버가 어떤 행위를 하는 것이 아니라 만들 때부터 어떤 형식으로 출력될 것인지를 알아야 한다.
                Observable.just("JoNadan", "Bewhy"),
                Observable.just("Programmer", "Rapper"),
                (item1, item2) -> "name:" + item1 + ", job:" + item2
        );

    }

    // 어쨌든 옵저버블은 어디에서인가 데이터를 가져오는 역할을 한다. file 이든, ... 든 ... 든
    public void doMap(View view) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(item -> item.equals("May") ? false : true)  // filter    원하는 데이터만 지정할 수 있음
                .map(item -> "[" + item + "]")                          // mapping   하나의 데이터를 치장해서 출력해줌
                .subscribe(
                        item -> {
                            data.add(item);
                            adapter.notifyItemInserted(data.size() - 1);
                        },
                        e -> Log.e("Error", e.toString()),
                        () -> Log.i("complete", "Successfully combined")
                );
    }

    public void doFlat(View view) {                                 // flatMap 은 하나의 데이터를 여러개로 만들어서 출력해줌
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(item -> Observable.fromArray(new String[]{"name:" + months[item], "code:" + item}))                                      // flatMap
                .subscribe(
                        item -> {
                            data.add(item);
                            adapter.notifyItemInserted(data.size() - 1);
                        },
                        e -> Log.e("Error", e.toString()),
                        () -> Log.i("complete", "Successfully combined")
                );
    }

    public void doZip(View view) {
        observableZip
                .timeInterval(TimeUnit.SECONDS, Schedulers.computation() )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> {
                            data.add(item+"");
                            adapter.notifyItemInserted(data.size() - 1);
                        },
                        e -> Log.e("Error", e.toString()),
                        () -> Log.i("complete", "Successfully combined")
                );
    }

    private void initView() {
        list = findViewById(R.id.list);
    }
}


class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {
    LayoutInflater inflater = null;
    List<String> data = null;

    public RecyclerAdapter(Context context, List<String> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Log.i("Refresh", "~~~~~~~~~~~~~~~~~position=" + position);
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView textView;

        public Holder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

    }
}