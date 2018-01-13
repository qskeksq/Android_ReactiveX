package com.example.administrator.servernodejs;

import com.example.administrator.servernodejs.domain.Bbs;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Administrator on 2017-07-25.
 */

public interface IBbs {

    String SERVER = "http://192.168.10.85/";

    @GET("bbs")
    Observable<ResponseBody> read();

    // 이거 주소 안 넣어주면 오류 생긴다!!!
    @POST("bbs")
    Observable<ResponseBody> write(@Body RequestBody bbs);
    // 이게 body 타입으로 넘어간다는 것을 명시해 줘야 함
    // GsonConverter 안 쓰기 때문에 String밖에 못 쓴다.
    // 참고로 여기에 어노테이션이 없으면 안 들어감

    @PUT
    Observable<ResponseBody> update(Bbs bbs);

    @DELETE
    Observable<ResponseBody> delete(Bbs bbs);
}
