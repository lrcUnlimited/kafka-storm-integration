package com.lrc.sports.bean;

/**
 * Created by li on 2016/11/29.
 */
public class Request<T> {
    private T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
