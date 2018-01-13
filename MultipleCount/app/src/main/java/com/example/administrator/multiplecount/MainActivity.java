package com.example.administrator.multiplecount;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int SET_COUNT = 99;

    TextView[] textViews = new TextView[4];

    // 서브 thread 로부터 메시지를 전달받을 Handler 를 생성한다... 메시지 통신
    Handler handler = new Handler() {
            // 서브 스레드에서 메시지를 전달하면 handleMessage 함수가 동작한다.
            // 사실 핸들러를 넘겨줘야 한다.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SET_COUNT:
                    textViews[msg.arg1].setText(msg.arg2+"");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i=0; i<4; i++){
            int id = getResources().getIdentifier("textView"+(i+1), "id" , getPackageName());
            textViews[i] = (TextView) findViewById(id);
        }


        // 넘겨줄 때 위젯을 넘겨주기 보다 아이디나 포지션을 넘겨줄 뿐이다
        Counter counter1 = new Counter(0, handler);
//        Counter counter2 = new Counter(textViews[1], this);
//        Counter counter3 = new Counter(textViews[2], this);
//        Counter counter4 = new Counter(textViews[3], this);

        counter1.start();
//        counter2.start();
//        counter3.start();
//        counter4.start();

    }
}

class Counter extends Thread {

    TextView textView;
    Handler handler;
    int count;
    int txt_index;


    public Counter(int txt_index, Handler handler){
//        this.textView = textView;
//        this.context = context;
        this.handler = handler;
        this.txt_index = txt_index;
    }

    // run 메소드 안에만 서브 스레드이다. 그리고 통신할 떄 굳이 핸들러 한 개 더 만들 필요 없고, 그냥 값을 인자, 메소드로 넘겨주면 되는 것이다.
    // 메소드로 보내주는 것이랑 루퍼 만들어서 핸들러 보내주는 것이랑 어떤 차이가 있는가 메모리 영역이 다르다. 핸들러는 서브 스레드의 지역 메모리로
    // 넘어가고, 메소드로 보내주면 메인 스레드 메모리 영역이기 때문이다.

//    @Override
//    public void run() {
//        for(int i=0; i<4; i++){
////            textView.setText(count+"");  여기다가 이렇게 하면 오류가 생김
////            count++;
//
//            count++;
//            context.runOnUiThread(new Runnable() {  // 들어온 러너블 인터페이스를 객체를 넘겨줌. 이 안쪽만 서브가 아닌 메인 스레드에서 실행됨
                                                        // 그리고 위젯
//                @Override
//                public void run() {
//
//                    textView.setText(count+"");
//
//                }
//            });
//
//            // 주의해야 할 것은 위의 count 와 이 밑의 count 가 서로 다른 스레드에서 작동되고 있기 때문에, 어느 것이 먼저 실행될
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    @Override
    public void run() {
        super.run();
//        Message msg = new Message();
//        while(count <= 40){
//            count++;
//            if(count >= 0 && count <10){
//                msg.what = 1;
//                msg.arg1 = count;
//                handler.sendMessage(msg);
//            }
//        }
        for(int i=0; i<10; i++){
            count++;
            Message msg = new Message();
            msg.what = MainActivity.SET_COUNT;
            msg.arg1 = txt_index;
            msg.arg2 = count;
//            msg.obj 인 경우는 String 경우 말고 거의 없군
            handler.sendMessage(msg);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    여기다가 쓰는 것이 아니로군!!!!
//    Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.what == 1){
//                activity.textViews[0].setText(msg.arg1);
//            }
//        }
//    };

}


