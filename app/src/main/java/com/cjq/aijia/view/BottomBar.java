package com.cjq.aijia.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cjq.aijia.entity.BottomButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJQ on 2015/11/11.
 */
public class BottomBar extends LinearLayout implements View.OnClickListener {

    private int dp;
    private List<BottomButton> buttons = new ArrayList<>();
    private int buttonOn = 0;
    private OnButtonCheckedListener checkedListener;

    public int getButtonActivated() {
        return buttonOn;
    }

    public OnButtonCheckedListener getCheckedListener() {
        return checkedListener;
    }

    public void setCheckedListener(OnButtonCheckedListener checkedListener) {
        this.checkedListener = checkedListener;
    }

    public interface OnButtonCheckedListener{
        public void onButtonChecked(int No);
        public void onButtonCheckedChanged(int No);
    }

    public BottomBar(Context context) {
        this(context,null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
    }

    public void addButton(BottomButton button){

        ImageView button1 = new ImageView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight=1;
        button1.setScaleType(ImageView.ScaleType.CENTER);
        button1.setLayoutParams(params);



        button1.setPadding(dp,dp,dp,dp);
        if(buttonOn == 0 && buttons.size()==0){
            button1.setImageResource(button.getImageOnId());
            button1.setBackgroundResource(button.getBackgroundOn());
            button.setNo(0);
        }else{
            button1.setImageResource(button.getImageOffId());
            button1.setBackgroundResource(button.getBackgroundOff());
            button.setNo(buttons.size());
        }

        buttons.add(button);

        button1.setTag(button.getNo());
        button1.setOnClickListener(this);
        addView(button1);
        invalidate();
    }

    @Override
    public void onClick(View v) {
        int No = (int) v.getTag();
        if(checkedListener!=null){
            checkedListener.onButtonChecked(No);
        }

        if(buttonOn!=No){
            BottomButton b = buttons.get(buttonOn);
            ImageView bi = (ImageView) getChildAt(buttonOn);
            bi.setImageResource(b.getImageOffId());
            bi.setBackgroundResource(b.getBackgroundOff());
            buttonOn=No;
            ImageView nb = (ImageView) v;
            BottomButton bx = buttons.get(buttonOn);

            nb.setImageResource(bx.getImageOnId());
            nb.setBackgroundResource(bx.getBackgroundOn());
            if(checkedListener!=null)
            checkedListener.onButtonCheckedChanged(No);
            if(bx.getCheckedInterface()!=null)
            bx.getCheckedInterface().onChecked();
        }
    }
}
