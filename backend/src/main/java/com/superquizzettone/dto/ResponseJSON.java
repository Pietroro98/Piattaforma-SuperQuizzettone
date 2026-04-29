package com.superquizzettone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseJSON<T> {

    private Integer status;
    private String message;
    private T data;

    public ResponseJSON() {
    }

    public ResponseJSON(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
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
}
