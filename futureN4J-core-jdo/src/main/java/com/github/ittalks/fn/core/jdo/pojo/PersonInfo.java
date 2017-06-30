package com.github.ittalks.fn.core.jdo.pojo;

import javax.jdo.annotations.*;

/**
 * Created by 刘春龙 on 2017/2/22.
 */
@PersistenceCapable
public class PersonInfo {

    //    @Column(name="P_ID")
    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)//ID自增长
    private Integer id;

//    @Column(name="P_NAME")
    @Persistent
    private String name;

//    @Column(name="P_AGE")
    @Persistent
    private int age;

//    @Column(name="P_EMAIL")
    @Persistent
    private String email;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return "PersonInfo [id=" + id + ", name=" + name + ", age=" + age
                + ", email=" + email + "]";
    }
}