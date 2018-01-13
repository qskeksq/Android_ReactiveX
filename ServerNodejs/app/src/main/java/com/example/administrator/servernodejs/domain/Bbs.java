package com.example.administrator.servernodejs.domain;

/**
 * Created by Administrator on 2017-07-25.
 */

public class Bbs {

    public int id;
    public String title;
    public String content;
    public String author;
    public String date;

    public Bbs() {
    }

    public Bbs(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
