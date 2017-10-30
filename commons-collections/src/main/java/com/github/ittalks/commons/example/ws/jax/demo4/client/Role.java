package com.github.ittalks.commons.example.ws.jax.demo4.client;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
public class Role {

    private Integer id;
    private String roleName; //角色名称

    public Role(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}