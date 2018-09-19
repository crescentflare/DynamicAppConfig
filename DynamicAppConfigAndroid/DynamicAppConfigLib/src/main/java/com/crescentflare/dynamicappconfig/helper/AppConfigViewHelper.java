package com.crescentflare.dynamicappconfig.helper;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Library helper: view utilities
 * A helper library to easily modify views
 */
public class AppConfigViewHelper
{
    static public void setBackgroundDrawable(View view, Drawable drawable)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
        {
            view.setBackgroundDrawable(drawable);
        }
        else
        {
            view.setBackground(drawable);
        }
    }

    static public int dp(int dp)
    {
        return (int)(Resources.getSystem().getDisplayMetrics().density * dp);
    }
}
