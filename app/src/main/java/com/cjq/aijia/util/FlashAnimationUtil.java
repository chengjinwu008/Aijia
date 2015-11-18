package com.cjq.aijia.util;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.cjq.aijia.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJQ on 2015/11/17.
 */
public class FlashAnimationUtil {

    private static Map<Integer,ObjectAnimator> ANIMATOR_PAIR=new HashMap<>();

    public static ObjectAnimator getFlashAnimation(final View view){
        if(ANIMATOR_PAIR.get(view.getId())==null){
            final ObjectAnimator color =ObjectAnimator.ofInt(view, "backgroundColor", view.getContext().getResources().getColor(R.color.pure_white), view.getContext().getResources().getColor(R.color.theme_color));
            color.setEvaluator(new ArgbEvaluator());
            color.setRepeatMode(ValueAnimator.REVERSE);
            color.setRepeatCount(ValueAnimator.INFINITE);
            color.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ANIMATOR_PAIR.remove(view.getId());
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            ANIMATOR_PAIR.put(view.getId(), color);
        }
        return ANIMATOR_PAIR.get(view.getId());
    }

    public static void endAll(){
        List<ObjectAnimator> animators = new ArrayList<>();
        animators.addAll(ANIMATOR_PAIR.values());

        for (ObjectAnimator o:animators){
            o.end();
        }

        animators.clear();
    }

    public static void changeColorSmooth(View view,int colorToChange){
        ColorDrawable bcDrawable = (ColorDrawable) view.getBackground();

        ObjectAnimator color =ObjectAnimator.ofInt(view, "backgroundColor", bcDrawable.getColor(), colorToChange);
        color.setEvaluator(new ArgbEvaluator());
        color.start();
    }
}
