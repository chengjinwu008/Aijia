package com.cjq.aijia.entity;

import com.cjq.aijia.CommonData;

/**
 * Created by CJQ on 2015/11/26.
 */
public class EventWebChange {
    String url;

    public EventWebChange(int no) {
        switch (no){
            case 0:
                url= CommonData.INDEX_URL;
                break;
            case 1:
                url= CommonData.CATEGORY_URL;
                break;
            case 2:
                url= CommonData.CART_URL;
                break;
        }
    }

    public String getUrl() {
        return url;
    }
}
