# RxAndroid

    어려워 보여도 결국 하는 일은 단순하다. 마치 서버가 복잡해 보여도 요청과 응답을 이해하는 것이 전부인 것과 비슷하다. 
    결국 데이터를 갖다가 어떻게 전달할지 문제이고, 거기서 데이터를 받아오는 곳과 받아온 데이터를 사용할 곳을 정하고, 언제 받아올지 정하면 되는 것이다. 
    받아올 곳이 등록될 때(subscribe) 받아올지, 발행 후 혹은 데이터를 다 받아오면(onNext, onComplete) 보내줄지 정해주면 된다. 
    데이터를 받아오는 곳을 Observable이라고 하고, 받아 쓰는 곳을 Subscriber라고 한다. 기존 Observer 패턴과 다른 점은 
    Observable의 인터페이스에서 onNext()외에 onComplete(), onError()가 존재한다는 것(Subscriber가 구현해야 하기 때문에 
    Subcriber가 가지고 있다고 해도 무방하다)

## 1. Observable & Observer

### (1) Observable 이해

    기존 Observer 패턴에서 Subject의 역할을 Observable이 한다. 즉, 여러 observer들을로부터 구독받고(addObserver) 데이터를 발행하는 역할을 한다. 
    Observable에 subscribe 하는 존재들이 observer들이다. 즉, Observable이라는 이름은 observer들이 observe할 수 있기 때문이다. 
    한마디로 Observable는 subscribe하는 존재, 각 observer들이 지켜보는 존재라는 뜻이다. 그렇다면 기존 옵저버 패턴과 어떤 점이 다를까. 
    Subject에 함수 2 개가 추가된 것 밖에 없다. 기존의 옵저버 패턴도 스트림으로 데이터 처리가 가능하다.

- create : Observable 생성
- subscribe : Observer의 구독
- 오퍼레이터 : 어떻게 발행될 것인지 규칙을 정함
- onNext : 새로운 데이터를 전달
- onCompleted : 스트림의 종료
- onError : 에러 신호를 전달

### (2) Observable 생성, 발행, 발행 완료

```java
Observable<String> observable;
private void createObservable() {
    observable = Observable.create(emitter -> {     // 생성
        emitter.onNext("Hello Android");            // 발행1 
        emitter.onNext("Good to see you");          // 발행2
        emitter.onComplete();                       // 발행 완료
    });
}
```

### (3) 구독

- 방법1
    ```java
    Observer<String> observer = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(String value) {
            Log.e("onNext", value);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
            Log.e("onComplete", "완료");
        }
    };
    observable.subscribe(observer);
    ```
- 방법2
    ```java
    Consumer<String> onNext = new Consumer() {
        @Override
        public void accept(Object o) {

        }
    };

    Consumer<Throwable> onError = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) {

        }
    };

    observable.subscribe(onNext, onError);
    ```
- 방법3
    ```java
    observable.subscribe(
            str         -> Log.e("OnNext", "======" + str),
            throwable   -> Log.e("OnError", throwable.getMessage()),
            ()          -> Log.e("OnComplete", "OnComplete")
    );
    ```

### (4) 구독시점과 발행시점의 차이에 따른 데이터 전달 여부

    데이터가 발행되는 시점과 옵저버가 구독하는 시점이 항상 같을 수 없고, 미리 준비되어 있을 수만은 없다. 먼저 구독을 했고 그 후 발행이 될 수 있고, 
    발행이 먼저 진행되는 상황에서 구독이 일어날 수도 있다.


#### A. PublishSubject

    오직 발행 이후 이벤트만을 받는다

- 상황
    - observer1 will receive all onNext and onComplete events
    ```java
    PublishSubject<Object> subject = PublishSubject.create();
    subject.subscribe(observer1);
    subject.onNext("one");
    subject.onNext("two");
    ```

    - (이어짐) observer2 will only receive "three", "four" and onComplete
    ```java
    subject.subscribe(observer2);
    subject.onNext("three");
    subject.onNext("four");
    subject.onComplete();
    ```

#### B. BehaviorSubject

    구독 바로 이전의 한 개 이벤트와 그 이후 모든 이벤트를 받는다.

- 상황

    - observer will receive all events
    ```java
    BehaviorSubject<Object> subject = BehaviorSubject.create("default");
    subject.subscribe(observer);
    subject.onNext("one");
    subject.onNext("two");
    subject.onNext("three");
    ```
    
    - observer will receive the "one", "two" and "three" events, but not "zero"
    ```java
    BehaviorSubject<Object> subject = BehaviorSubject.create("default");
    subject.onNext("zero");
    subject.onNext("one");
    subject.subscribe(observer);
    subject.onNext("two");
    subject.onNext("three");
    ```

    - observer will receive only onComplete
    ```java
    BehaviorSubject<Object> subject = BehaviorSubject.create("default");
    subject.onNext("zero");
    subject.onNext("one");
    subject.onComplete();
    subject.subscribe(observer);
    ```

    - observer will receive only onError
    ```java
    BehaviorSubject<Object> subject = BehaviorSubject.create("default");
    subject.onNext("zero");
    subject.onNext("one");
    subject.onError(new RuntimeException("error"));
    subject.subscribe(observer);
    ```

#### C. ReplaySubject

    구독을 나중에 하더라도 모든 발행된 이벤트를 받는다.

- 상황

    - both of the observers will get the onNext/onComplete calls from above
    ```java
    ReplaySubject<Object> subject = new ReplaySubject<>();
    subject.onNext("one");
    subject.onNext("two");
    subject.onNext("three");
    subject.onComplete();

    subject.subscribe(observer1);
    subject.subscribe(observer2);
    ```

#### D. AsyncSubject

    onComplete 이전 마지막 이벤트를 받는다.


#### Test

- 생성

    ```java
    PublishSubject<String> publishSubject = PublishSubject.create();
    BehaviorSubject<String> behaviorSubject = BehaviorSubject.create();
    ReplaySubject<String> replaySubject = ReplaySubject.create();
    AsyncSubject<String> asyncSubject = AsyncSubject.create();
    ```
- 발행

    ```java
    // publishSubject.subscribeOn(Schedulers.io()) 스레드가 안 되면 직접 스레드를 만들면 됨
    new Thread() {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Log.i("Publish", "A" + i);
                publishSubject.onNext("A" + i);
                behaviorSubject.onNext("B" + i);
                replaySubject.onNext("C" + i);
                asyncSubject.onNext("D" + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }.start();
    ```

- 구독

    ```java
    public void getPublish(View view) {
        publishSubject.observeOn(AndroidSchedulers.mainThread());
        publishSubject.subscribe(
                item -> Log.i("Publish", "item=" + item)
        );
    }
    ```
    ```java
    public void getBehavior(View view){
        behaviorSubject.subscribe(
                item -> Log.i("Behavior", "item="+item)
        );
    }
    ```
    ```java
    public void getReplay(View view){
        replaySubject.subscribe(
                item -> Log.i("Replay", "item="+item)
        );
    }
    ```
    ```java
    public void getAsync(View view){
        asyncSubject.subscribe(
                item -> Log.i("Async", "item="+item)
        );
    }
    ```

### (5) 오퍼레이터

#### A. Just

- 넘겨줄 데이터 객체
    ```java
    class Memo {
        String memo;

        public Memo(String memo) {
            this.memo = memo;
        }
    }
    ```

- Observable 초기화
    ```java
    List<String> data = new ArrayList<>();
    Memo memo1 = new Memo("Hello");
    Memo memo2 = new Memo("Android");
    Memo memo3 = new Memo("with");
    Memo memo4 = new Memo("Reactive X");
    Observable<Memo> forJust = Observable.just(memo1, memo2, memo3, memo4); 
    ```
    ```java
    Observable<String> observableJust = Observable.just("JAN", "FEB", "MARCH");
    ```

- subscribe
    ```java
    public void doJust(View view){
        forJust.subscribe(
                obj -> data.add(obj.memo),             // obj로 메모 객체가 각각 넘어온다.
                                                       // 뷰를 넘겨주는 방식으로 해서 뮤직플레이어 옵저버 패턴 사용할 수 있음
                t   -> { },
                ()  -> adapter.notifyDataSetChanged()
        );
    }
    ```

#### B. From

- Observable 초기화
    ```java
    String[] fromData = {"aaa", "bbb", "ccc", "ddd", "eee"};
    Observable<String> forFrom = Observable.fromArray(fromData);
    ```

- subscribe
    ```java
    forFrom.subscribe(
            str -> data.add(str),                   // 옵저버블(발행자:emitter)로부터 데이터를 가져온다. 데이터를 쌓아놓는 것은 이곳에서 해 준다.
                                                    // 동영상 재생의 경우는 스트림을 받아오고 그 중간중간 재생해 주는 역할을 여기서 해 준다.
                                                    // 여기로 넘어오는 데이터는 데이터 셋으로 데이터 배열 전체가 넘어온다.
                                                    // 뮤직 플레이어 재생 버튼은 여기서 변경시켜줄 수 있군
            t   -> { },
            ()  -> adapter.notifyDataSetChanged()   // 완료되면 리스트에 알린다. onComlete()가 없으면 데이터를 쌓아두기만 하고 하고 보이지 않는다
    );
    ```


#### C. Defer

    퍼는 메모리에 로드되는 순간이 다른 것 뿐이다. 위에 두 개는 초기화 될 때 메모리에 로드되고 defer 는 호출될 때 메모리에 올라간다.

- Observable 초기화
    ```java
    String[] fromData = {"aaa", "bbb", "ccc", "ddd", "eee"};
    Observable<String> forDefer = Observable.defer(new Callable<ObservableSource<? extends String>>() {
        @Override
        public ObservableSource<? extends String> call() throws Exception {
            return Observable.just("monday", "tuesday", "wednesday");
        }
    });
    ```

- subscribe
    ```java
    forDefer.subscribe(
            str -> data.add(str),
            t   -> { },
            ()  -> adapter.notifyDataSetChanged()
    );
    ```

#### D. Map

- Observable 초기화
    ```java
    DateFormatSymbols dfs = new DateFormatSymbols();
    months = dfs.getMonths();

    Observable<Integer> observable = Observable.create(emitter -> {
        for (int i = 0; i < 12; i++) {
            emitter.onNext(i);
            Thread.sleep(1000);
        }
        emitter.onComplete();
    });
    ```

- subscribe
    ```java
    observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(item -> item.equals("May") ? false : true)      // filter    원하는 데이터만 지정할 수 있음
            .map(item -> "[" + item + "]")                          // mapping   하나의 데이터를 치장해서 출력해줌
            .subscribe(
                    item -> {
                        data.add(item);
                        adapter.notifyItemInserted(data.size() - 1);
                    },
                    e -> Log.e("Error", e.toString()),
                    () -> Log.i("complete", "Successfully combined")
            );
    ```

#### E. FlatMap

    하나의 데이터를 여러개로 만들어서 출력해줌

- Observable 초기화
    ```java
    DateFormatSymbols dfs = new DateFormatSymbols();
    months = dfs.getMonths();

    Observable<Integer> observable = Observable.create(emitter -> {
        for (int i = 0; i < 12; i++) {
            emitter.onNext(i);
            Thread.sleep(1000);
        }
        emitter.onComplete();
    });
    ```

- subscribe
    ```java
    observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap(item -> Observable.fromArray(new String[]{"name:" + months[item], "code:" + item})) 
            .subscribe(
                    item -> {
                        data.add(item);
                        adapter.notifyItemInserted(data.size() - 1);
                    },
                    e -> Log.e("Error", e.toString()),
                    () -> Log.i("complete", "Successfully combined")
            );
    ```

#### F. zip

    옵저버가 어떤 행위를 하는 것이 아니라 만들 때부터 어떤 형식으로 출력될 것인지를 알아야 한다.

- Observable 초기화
    ```java
    DateFormatSymbols dfs = new DateFormatSymbols();
    months = dfs.getMonths();

    Observable<String> observableZip = Observable.zip(              
            Observable.just("JoNadan", "Bewhy"),
            Observable.just("Programmer", "Rapper"),
            (item1, item2) -> "name:" + item1 + ", job:" + item2
    );
    ```

- subscribe
    ```java
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
    ```

## 2. Rx 스레드 적용

- Observable 초기화
    ```java
    DateFormatSymbols dfs = new DateFormatSymbols();
    String[] months = dfs.getMonths();

    Observable<String> observable = Observable.create(emitter -> {
        for (String month : months) {
            emitter.onNext(month);
            Thread.sleep(1000);
        }
        emitter.onComplete();
    });
    ```

- subscribe
    ```java
    observable.subscribeOn(Schedulers.io())                 // Observable에 스레드를 등록해 주는 것으로 데이터를 주고 받는 io 스레드에 등록해 준다.
            .observeOn(AndroidSchedulers.mainThread())      // UI 는 메인 스레드에서밖에 할 수 없기 때문에 옵저버를 메인 스레드에 등록시켜 준다.
                                                            // 데이터를 받아오는 것은 서브스레드(Observable)에서 하고 기다리다가 데이터를 받아서 화면에 등록해 주는 것은 메인에서 한다
                                                            // 네트워크와 연관해서 생각해보면 subscribeOn 에서 네트워크 처리를 해 주고 등록된 옵저버들이 데이터를 받아서 뷰에 세팅한다
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
    ```


## 3. RxBinding

- Gradle : implementation 'com.jakewharton.rxbinding2:rxbinding-appcompat-v7:2.+'

- 단일 반응형

```java
RxView.clicks(findViewById(R.id.btnRandom))
                .map(event -> new Random().nextInt())
                .subscribe(
                        number -> ((TextView)findViewById(R.id.textView)).setText("random number="+number)
                );

// 뷰에다가 RxJava 를 바인딩해서 쓰는 방법    --  항상 뭔가 새롭게 사용하는 것들은 안에 들어가고, 내부적 동작 방법을 알아봐라
RxTextView.textChangeEvents(findViewById(R.id.editText))
        .subscribe(                                         // 실제로 텍스트 꺼내기
                item -> Log.i("Word", "String="+item.text().toString())
        );
```

- 다중 반응형

```java
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
```

- Observable로 직접 구현

```java

```


## 4. Observable 네트워크 통신

### (1) Retrofit

    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0' 

- 레트로핏 설정 & 생성 
    
    ```java
    // 1. 레트로핏 생성
    Retrofit client = new Retrofit.Builder()
            .baseUrl(SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // 리턴 타입을 Observable로 사용할 수 있게 해준다
            .build();
    // 2. 서비스 생성
    IWeather service = client.create(IWeather.class);
    ```

- GET : Observable로 응답 받기

    - addConverterFactory 사용하는 경우
        ```java
        Observable<Data> observable = service.getData(SERVER_KEY, 1, 10, region);
        observable.subscribeOn(Schedulers.io())             // 서브스레드로 지정하지 않으면 오류가 생기는 것으로 보아 subscribeOn 부분이 네트워크로부터 데이터를 받아오는 역할을 하는 듯 하다
                .observeOn(AndroidSchedulers.mainThread())  // 로그 찍을 때는 Schedulers.newThread() 로 해줘도 됨
                .subscribe(
                        data -> {
                            List<String> content = new ArrayList<>();
                            Row[] rows = data.getRealtimeWeatherStation().getRow();
                            for (Row row : rows) {content.add(row.getSAWS_OBS_TM());}
                            adapter.setDat(content);
                            adapter.notifyDataSetChanged();
                        }
                );
        ```

    - addConverterFactory 사용하지 않는 경우 

        ```java
        @GET("bbs")
        Observable<ResponseBody> read();

        @POST("bbs")
        Observable<ResponseBody> write(@Body RequestBody bbs);

        @PUT
        Observable<ResponseBody> update(Bbs bbs);

        @DELETE
        Observable<ResponseBody> delete(Bbs bbs);
        ```

        ```java
        Observable<ResponseBody> observable = myBbs.read();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBody -> {
                            String jsonString = responseBody.string();  
                            // Log.e("Retrofit", jsonString);  스트림이기 때문에 한 번 꺼내면 소멸된다.
                            
                            // Gson 타입 지정1
                            // Type type = new TypeToken<List<Bbs>>(){}.getType()
                            // Bbs datas[] = new Gson().fromJson(jsonString, type);

                            // Gson 타입 지정2
                            // Bbs[] datas = new Gson().fromJson(jsonString, Bbs[].class);

                            adapter.notifyDataSetChanged();
                        }
                );
        ```

- POST : Observable로 응답 받기

    ```java
    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bbs));
    Observable<ResponseBody> observable = myBbs.write(requestBody);
    observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    responseBody -> {
                        String reuslt = responseBody.string();
                    }
            );
    ```
