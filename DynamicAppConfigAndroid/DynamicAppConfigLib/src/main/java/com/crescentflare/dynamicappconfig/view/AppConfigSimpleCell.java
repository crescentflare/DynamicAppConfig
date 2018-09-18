package com.crescentflare.dynamicappconfig.view;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;

/**
 * Library view: simple cell
 * Simulates a simple list view cell as a simple info item
 */
public class AppConfigSimpleCell extends FrameLayout
{
    // ---
    // Members
    // ---

    private TextView labelView;


    // ---
    // Initialization
    // ---

    public AppConfigSimpleCell(Context context)
    {
        super(context);
        init(context, null);
    }

    public AppConfigSimpleCell(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigSimpleCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigSimpleCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        // Prepare container
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setMinimumHeight(dp(48));
        container.setPadding(dp(12), dp(12), dp(12), dp(12));
        addView(container);

        // Add label view
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        container.addView(labelView = new TextView(context));
        labelView.setLayoutParams(layoutParams);
    }


    // ---
    // Modify view
    // ---

    public void setText(String text)
    {
        labelView.setText(text);
    }


    // ---
    // Helper
    // ---

    private int dp(int dp)
    {
        return AppConfigViewHelper.dp(dp);
    }
}
