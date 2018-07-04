package com.along101.contract.controller.response;

import lombok.Data;

/**
 * backend response providing identical format of response code and message
 * <p>
 * Created by qiankai02 on 2017/11/8.
 */
@Data
public class Response<T> {
    private Integer code;
    private String message;
    private T detail;

    public static <S> Response<S> success(S t) {
        Response<S> response = new Response<>();
        response.setCode(0);
        response.setMessage("success");
        response.setDetail(t);
        return response;
    }

    public static <S> Response<S> error(String message) {
        Response<S> response = new Response<>();
        response.setCode(1);
        response.setMessage(message);
        response.setDetail(null);
        return response;
    }

    public static <S> Response<S> error(Throwable throwable) {
        Response<S> response = new Response<>();
        response.setCode(-1);
        response.setMessage(throwable.getMessage());
        response.setDetail(null);
        return response;
    }

}
