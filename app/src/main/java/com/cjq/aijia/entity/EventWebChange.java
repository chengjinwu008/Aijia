package com.cjq.aijia.entity;

import com.cjq.aijia.CommonData;

/**
 * Created by CJQ on 2015/11/26.
 */
public class EventWebChange {
    String url;
    String name;
    public EventWebChange(int no) {
        switch (no){
            case 0:
                url= CommonData.INDEX_URL;
                name = "爱家商城";
                break;
            case 1:
                url= CommonData.CATEGORY_URL;
                name = "分类导航";
                break;
            case 2:
                url= CommonData.CART_URL;
                name = "购物车";
                break;
        }
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
