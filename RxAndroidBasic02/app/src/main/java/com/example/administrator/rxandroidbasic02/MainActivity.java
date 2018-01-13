package com.example.administrator.rxandroidbasic02;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button just,from,defer;
    // 목록
    private RecyclerView listView;
    private CustomAdapter adapter;
    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initObservable();
    }

    private void initView() {
        textView = findViewById(R.id.textView);
        just = findViewById(R.id.just);
        from = findViewById(R.id.from);
        defer = findViewById(R.id.defer);
        listView = findViewById(R.id.listView);
        adapter = new CustomAdapter(data);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
    }

    Observable<String> forFrom ;
    Observable<Memo> forJust;       // 여기서 static 으로 생성해주고 다른 곳에서 옵저버에 더해주도록 할 수 있군
    Observable<String> forDefer;

    private void initObservable(){
        // forFrom 초기화
        String[] fromData = {"aaa", "bbb", "ccc", "ddd", "eee"};
        forFrom = Observable.fromArray(fromData);               // initObservable 을 호출하면 Observable 이 초기화 되고 데이터가 쌓인다.

        // forJust 초기화
        Memo memo1 = new Memo("Hello");
        Memo memo2 = new Memo("Android");
        Memo memo3 = new Memo("with");
        Memo memo4 = new Memo("Reactive X");
        forJust = Observable.just(memo1, memo2, memo3, memo4);  // 객체를 하나씩 던져준다.
                                                                // 참고로 뷰를 넘겨주는 방식으로 해서 뮤직플레이어 옵저버 패턴 사용할 수 있음

        // defer 초기화
        forDefer = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just("monday", "tuesday", "wednesday");
            }
        });
    }

    // 기존 옵저버 패턴과 어떤 점이 다를까. 함수 2 개가 추가된 것 밖에 없다. 기존의 옵저버 패턴도 스트림으로 데이터 처리가 가능하다.
    // 여러 액티비티에서 사용해야 할 경우 싱글턴이나 static 으로 해 주는 것이다.

    public void doFrom(View view){
        forFrom.subscribe(
                str -> data.add(str),                   // 옵저버블(발행자:emitter)로부터 데이터를 가져온다. 데이터를 쌓아놓는 것은 이곳에서 해 준다.
                                                        // 동영상 재생의 경우는 스트림을 받아오고 그 중간중간 재생해 주는 역할을 여기서 해 준다.
                                                        // 여기로 넘어오는 데이터는 데이터 셋으로 데이터 배열 전체가 넘어온다.
                                                        // 뮤직 플레이어 재생 버튼은 여기서 변경시켜줄 수 있군
                t   -> { /** */ },
                ()  -> adapter.notifyDataSetChanged()   // 완료되면 리스트에 알린다. 이게 없으면 데이터를 쌓아두기만 하고 하고 보이지 않는다
        );
    }


    public void doJust(View view){
        forJust.subscribe(
                obj -> data.add(obj.memo),              // obj 자체가 객체임. 넘겨줄 때 객체 하나를 넘겨준다. 참고로 memo 는 데이터임.
                t   -> { /** */ },
                ()  -> adapter.notifyDataSetChanged()
        );
    }

    // 디퍼는 메모리에 로드되는 순간이 다른 것 뿐이다. 위에 두 개는 초기화 될 때 메모리에 로드되고 defer 는 호출될 때 메모리에 올라간다.
    public void doDefer(View view){
        forDefer.subscribe(
                str -> data.add(str),
                t   -> { /** */ },
                ()  -> adapter.notifyDataSetChanged()
        );
    }
}

// just 생성자를 위한 클래스
class Memo {
    String memo;

    public Memo(String memo) {
        this.memo = memo;
    }
}

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    List<String> data;

    public CustomAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
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
            textView = itemView.findViewById(R.id.textView2);
        }
    }
}
