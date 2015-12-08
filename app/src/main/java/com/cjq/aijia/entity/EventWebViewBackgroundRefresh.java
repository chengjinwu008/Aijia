package com.cjq.aijia.entity;

import com.cjq.aijia.CommonData;

/**
 * Created by CJQ on 2015/12/8.
 */
public class EventWebViewBackgroundRefresh {
    String url;

    public EventWebViewBackgroundRefresh(int no) {
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
