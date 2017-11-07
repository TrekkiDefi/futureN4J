package com.github.ittalks.commons.example.webservice.jaxws.mapadapter.server;

import java.util.List;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
public class MyRole {

    private String key;
    private List<Role> value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Role> getValue() {
        return value;
    }

    public void setValue(List<Role> value) {
        this.value = value;
    }
}
