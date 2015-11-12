package com.cjq.aijia.entity;

/**
 * Created by CJQ on 2015/11/11.
 */
public class BottomButton {

    private int imageOnId;
    private int imageOffId;
    private int backgroundOn;
    private int backgroundOff;
    private int No;

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public interface OnCheckedInterface{
        public void onChecked();
    }

    OnCheckedInterface checkedInterface;


    public BottomButton(int imageOnId, int imageOffId, int backgroundOn, int backgroundOff, OnCheckedInterface checkedInterface) {
        this.imageOnId = imageOnId;
        this.imageOffId = imageOffId;
        this.backgroundOn = backgroundOn;
        this.backgroundOff = backgroundOff;
        this.checkedInterface = checkedInterface;
    }

    public int getImageOnId() {
        return imageOnId;
    }

    public int getImageOffId() {
        return imageOffId;
    }

    public int getBackgroundOn() {
        return backgroundOn;
    }

    public int getBackgroundOff() {
        return backgroundOff;
    }

    public OnCheckedInterface getCheckedInterface() {
        return checkedInterface;
    }
}
