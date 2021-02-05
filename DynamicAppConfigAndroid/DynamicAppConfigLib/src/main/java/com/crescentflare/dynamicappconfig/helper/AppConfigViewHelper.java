package com.crescentflare.dynamicappconfig.helper;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;

/**
 * Library helper: view utilities
 * A helper library to easily modify views
 */
public class AppConfigViewHelper {

    // --
    // Obtain colors
    // --

    static public int getAccentColor(Context context) {
        int identifier = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
        if (identifier == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            identifier = android.R.attr.colorAccent;
        }
        if (identifier > 0) {
            TypedValue typedValue = new TypedValue();
            TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{identifier});
            if (a != null) {
                int color = a.getColor(0, 0);
                a.recycle();
                return color;
            }
        }
        return Color.BLACK;
    }

    static public int getColor(Context context, int resourceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(resourceId, null);
        }
        return context.getResources().getColor(resourceId);
    }

    static public int pickBestForegroundColor(int backgroundColor, int lightForegroundColor, int darkForegroundColor) {
        double[] colorComponents = {
                (double)(backgroundColor & 0xFF) / 255.0,
                (double)((backgroundColor & 0xFF00) >> 8) / 255.0,
                (double)((backgroundColor & 0xFF0000) >> 16) / 255.0
        };
        for (int i = 0; i < colorComponents.length; i++) {
            if (colorComponents[i] <= 0.03928) {
                colorComponents[i] /= 12.92;
            }
            colorComponents[i] = Math.pow((colorComponents[i] + 0.055) / 1.055, 2.4);
        }
        double intensity = (0.2126 * colorComponents[0]) + (0.7152 * colorComponents[1]) + (0.0722 * colorComponents[2]);
        return intensity > 0.179 ? darkForegroundColor : lightForegroundColor;
    }


    // --
    // Set drawable
    // --

    static public void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }


    // --
    // Convert dimension pixels
    // --

    static public int dp(int dp) {
        return (int)(Resources.getSystem().getDisplayMetrics().density * dp);
    }
}
