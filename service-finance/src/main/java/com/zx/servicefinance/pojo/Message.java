package com.zx.servicefinance.pojo;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Message {

    private Integer code; // false 代表失败 true 代表成功
    private String msg; // 错误信息
    private Object data;

    public Message(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Message createErr(Integer code ,String msg ) {
        return new Message(code, msg, null );
    }

    public static Message createSuc( Object o ) {

        return new Message( 200, null, o );
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
