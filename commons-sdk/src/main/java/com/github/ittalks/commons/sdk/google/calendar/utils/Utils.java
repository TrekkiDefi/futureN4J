package com.github.ittalks.commons.sdk.google.calendar.utils;

import java.util.Date;

/**
 * Created by 刘春龙 on 2017/3/7.
 */
public class Utils {

    /**
     * 获取与当前日期和时间相对的新{@link java.util.Date}。
     *
     * @param field 要增加的{@link java.util.Calendar}的字段标识符
     * @param amount 要增加的字段的数量
     * @return 新日期
     */
    public static Date getRelativeDate(int field, int amount) {

        Date now = new Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(now);
        cal.add(field, amount);

        return cal.getTime();
    }
}
