package com.cjq.aijia.entity;

/**
 * Created by CJQ on 2015/12/1.
 */
public class SettingItem {

    public enum TYPE{
        MENU,BUTTON
    }

    private TYPE type=TYPE.MENU;
    private String mainMSG;
    private String subMSG;
    private boolean showArrow;

    public SettingItem(String mainMSG, String subMSG, boolean showArrow) {
        this.mainMSG = mainMSG;
        this.subMSG = subMSG;
        this.showArrow = showArrow;
    }

    public SettingItem(TYPE type, String mainMSG, String subMSG, boolean showArrow) {
        this.type = type;
        this.mainMSG = mainMSG;
        this.subMSG = subMSG;
        this.showArrow = showArrow;
    }

    public SettingItem(TYPE type, String mainMSG) {
        this.type = type;
        this.mainMSG = mainMSG;
    }

    public SettingItem(String mainMSG, String subMSG) {
        this.mainMSG = mainMSG;
        this.subMSG = subMSG;
        showArrow=false;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getMainMSG() {
        return mainMSG;
    }

    public void setMainMSG(String mainMSG) {
        this.mainMSG = mainMSG;
    }

    public String getSubMSG() {
        return subMSG;
    }

    public void setSubMSG(String subMSG) {
        this.subMSG = subMSG;
    }

    public boolean isShowArrow() {
        return showArrow;
    }

    public void setShowArrow(boolean showArrow) {
        this.showArrow = showArrow;
    }
}
