package com.em_projects.shift_timer.model;

public class DataWrapper<T> {

    private Throwable throwable;
    private T data;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
