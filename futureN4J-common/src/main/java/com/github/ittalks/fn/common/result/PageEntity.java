package com.github.ittalks.fn.common.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 刘春龙 on 2017/3/9.
 */
public class PageEntity<T> {
    private static final long defaultLimit = 10L;

    private long start;
    private long limit;
    private long total;
    private List<T> list;
    private Map<String, Object> params;

    public PageEntity() {
        this.start = 0L;
        this.limit = defaultLimit;
        this.total = 0L;
        this.list = new ArrayList();
        this.params = new HashMap();
        this.params.put("start", Long.valueOf(this.start));
        this.params.put("limit", Long.valueOf(this.limit));
    }

    public PageEntity(long start, long limit) {
        this.start = start;
        this.limit = limit > 500L ? 500L : limit;
        this.total = 0L;
        this.list = new ArrayList();
        this.params = new HashMap();
        this.params.put("start", Long.valueOf(this.start));
        this.params.put("limit", Long.valueOf(this.limit));
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = (list == null ? this.list : list);
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
        this.params.put("start", Long.valueOf(start));
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
        this.params.put("limit", Long.valueOf(limit));
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Map<String, Object> getQuery() {
        return params;
    }

    public Object getQuery(String key) {
        return params.get(key);
    }

    public Map<String, Object> query(String key, Object value) {
        this.params.put(key, value);
        return this.params;
    }
}
