package com.crescentflare.dynamicappconfig.helper;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;

/**
 * Library helper: resource access
 * A helper library to access app resources for skinning the user interface
 */
public class AppConfigResourceHelper
{
    static public int getAccentColor(Context context)
    {
        int identifier = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
        if (identifier == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            identifier = android.R.attr.colorAccent;
        }
        if (identifier > 0)
        {
            TypedValue typedValue = new TypedValue();
            TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{identifier});
            if (a != null)
            {
                int color = a.getColor(0, 0);
                a.recycle();
                return color;
            }
        }
        return Color.BLACK;
    }

    static public int getColor(Context context, String name)
    {
        int identifier = context.getResources().getIdentifier(name, "color", context.getPackageName());
        if (identifier > 0)
        {
            return context.getResources().getColor(identifier);
        }
        return Color.TRANSPARENT;
    }

    static public String getString(Context context, String name)
    {
        int identifier = context.getResources().getIdentifier(name, "string", context.getPackageName());
        if (identifier > 0)
        {
            return context.getResources().getString(identifier);
        }
        return "";
    }

    static public int getIdentifier(Context context, String name)
    {
        return context.getResources().getIdentifier(name, "id", context.getPackageName());
    }
}
