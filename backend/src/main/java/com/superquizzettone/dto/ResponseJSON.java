package com.superquizzettone.dto;

public class ResponseJSON<T> {

    private Integer status;
    private String messaggio;
    private T data;

    public ResponseJSON() {
    }

    public ResponseJSON(Integer status, String messaggio, T data) {
        this.status = status;
        this.messaggio = messaggio;
        this.data = data;
    }

    public static <T> ResponseJSON<T> of(String messaggio, T data) {
        return new ResponseJSON<>(null, messaggio, data);
    }

    public static <T> ResponseJSON<T> success(int status, String messaggio, T data) {
        return new ResponseJSON<>(status, messaggio, data);
    }

    public static <T> ResponseJSON<T> error(int status, String messaggio) {
        return new ResponseJSON<>(status, messaggio, null);
    }

    public static <T> ResponseJSON<T> error(int status, String messaggio, T data) {
        return new ResponseJSON<>(status, messaggio, data);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
