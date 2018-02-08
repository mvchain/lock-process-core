package com.mvc.common.util;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author qyc
 */
public class Query extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private final static String PAGE_NO = "pageNo";
    private final static String PAGE_SIZE = "pageSize";
    private int page = 1;
    private int limit = 10;

    public Query(Map<String, Object> params) {
        this.putAll(params);
        //分页参数
        if (params.get(PAGE_NO) != null) {
            this.page = Integer.parseInt(params.get(PAGE_NO).toString());
        }
        if (params.get(PAGE_SIZE) != null) {
            this.limit = Integer.parseInt(params.get(PAGE_SIZE).toString());
        }
        this.remove(PAGE_NO);
        this.remove(PAGE_SIZE);
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
