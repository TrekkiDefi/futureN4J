package com.github.ittalks.commons.example.jms.model;

import java.io.Serializable;

/**
 * Created by 刘春龙 on 2017/11/2.
 */
public class Msg implements Serializable {

    private String msg;

    public Msg() {
    }

    public Msg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
