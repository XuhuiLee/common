package com.createarttechnology.common;

/**
 * Created by lixuhui on 2018/9/13.
 */
public class BaseResp<T> {
    private int code = ErrorInfo.ERROR.getCode();
    private String msg = ErrorInfo.ERROR.getMsg();
    private T data;
    private String locateAnchor;

    public BaseResp() {}

    public BaseResp(ErrorInfo errorInfo) {
        this.code = errorInfo.getCode();
        this.msg = errorInfo.getMsg();
    }

    public BaseResp<T> setErrorInfo(ErrorInfo errorInfo) {
        this.code = errorInfo.getCode();
        this.msg = errorInfo.getMsg();
        return this;
    }

    public boolean success() {
        return this.code == ErrorInfo.SUCCESS.getCode();
    }

    public int getCode() {
        return code;
    }

    public BaseResp<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResp<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public BaseResp<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getLocateAnchor() {
        return locateAnchor;
    }

    public void setLocateAnchor(String locateAnchor) {
        this.locateAnchor = locateAnchor;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", locateAnchor='" + locateAnchor + '\'' +
                '}';
    }
}
