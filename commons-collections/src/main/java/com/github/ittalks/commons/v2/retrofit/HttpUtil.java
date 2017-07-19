package com.github.ittalks.commons.v2.retrofit;

import retrofit2.Call;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 刘春龙 on 2017/7/19.
 */
public class HttpUtil {

    // 请求
    private static Map<String, Call> CALL_MAP = new HashMap<>();

    /**
     * 添加某个请求
     *
     * @param tag  请求标记
     * @param url  请求url
     * @param call 请求Call
     */
    public static synchronized void putCall(Object tag, String url, Call call) {
        if (tag == null)
            return;
        synchronized (CALL_MAP) {
            CALL_MAP.put(tag.toString() + url, call);
        }
    }

    /**
     * 取消指定tag下所有请求。如果要取消某个tag单独请求，tag需要传入tag + url.
     *
     * @param tag 请求标记
     */
    public static synchronized void cancel(Object tag) {
        if (tag == null)
            return;
        synchronized (CALL_MAP) {
            Iterator<Map.Entry<String, Call>> iterator = CALL_MAP.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Call> curEntry = iterator.next();
                if (curEntry.getKey().startsWith(tag.toString())) {
                    curEntry.getValue().cancel();
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 移除某个请求
     * @param url 请求url
     */
    public static synchronized void removeCall(String url) {
        synchronized (CALL_MAP) {
            Iterator<Map.Entry<String, Call>> iterator = CALL_MAP.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Call> curEntry = iterator.next();
                if (curEntry.getKey().contains(url)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }
}
