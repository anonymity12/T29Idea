package com.paul.t29ideagarden2.util;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by paul on 2018/8/11
 * last modified at 19:59.
 * Desc: utils main use for custom view WaveProgress
 */

public class ViewUtil {
    public static int measure(int measureSpec, int defaultSize) {
        //defaultSize 实际上是我们人为在代码里定义的保障性大小限制，它（一定）不是xml里的那个指定尺寸.实际上他是在Constant里定义的一个常量
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (measureSpec == View.MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);//use the miner one.
        }
        return result;
    }

    public static int dipToPx(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public static String getPrecisionFormat(int precision) {
        return "%." + precision + "f";
    }

    /*reverse the array*/
    public static <T> T[] reverse (T[] arrays){
        if (arrays == null) {
            return null;
        }
        int length = arrays.length;
        for (int i = 0; i < length / 2; i++) {
            T t = arrays[i];
            arrays[i] = arrays[length - i - 1];
            arrays[length - i - 1] = t;
        }
        return arrays;
    }

    /*measure text height*/
    public static float measureTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics =paint.getFontMetrics();
        return (Math.abs(fontMetrics.ascent - fontMetrics.descent));
    }
}
