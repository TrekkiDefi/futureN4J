package com.github.ittalks.commons.webservice.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 刘春龙 on 2017/4/28.
 */
@XmlRootElement(name="Users")
@XmlAccessorType(XmlAccessType.FIELD)
public class Users implements Serializable {

    private List<User> users;
    private List<String> strList;
    private User[] userArr;
    private HashMap<String, User> maps;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<String> getStrList() {
        return strList;
    }

    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    public User[] getUserArr() {
        return userArr;
    }

    public void setUserArr(User[] userArr) {
        this.userArr = userArr;
    }

    public HashMap<String, User> getMaps() {
        return maps;
    }

    public void setMaps(HashMap<String, User> maps) {
        this.maps = maps;
    }
}
