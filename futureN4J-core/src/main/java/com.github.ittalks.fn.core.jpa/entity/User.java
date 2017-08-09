package com.github.ittalks.fn.core.jpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

/**
 * Created by 刘春龙 on 2017/8/9.
 */
@Data
@NoArgsConstructor
public class User {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 账户类型
     */
    private String accountType;

    /**
     * 创建时间
     */
    private DateTime created;

    /**
     * 更新时间
     */
    private DateTime updated;

    /**
     * 用户状态，0：正常，1：禁用
     */
    private Integer status;
}
