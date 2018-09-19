package com.crescentflare.dynamicappconfig.view;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;

/**
 * Library view: simple cell
 * Simulates a list view cell as a switch item
 */
public class AppConfigSwitchCell extends FrameLayout
{
    // ---
    // Members
    // ---

    private SwitchCompat switchView;


    // ---
    // Initialization
    // ---

    public AppConfigSwitchCell(Context context)
    {
        super(context);
        init(context, null);
    }

    public AppConfigSwitchCell(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigSwitchCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigSwitchCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
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
        container.setPadding(dp(12), 0, dp(12), 0);
        addView(container);

        // Add switch view
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        container.addView(switchView = new SwitchCompat(context));
        switchView.setLayoutParams(layoutParams);
        switchView.setMinimumHeight(dp(60));
        switchView.setPadding(0, dp(12), 0, dp(12));
        switchView.setTextSize(18);
    }


    // ---
    // Modify view
    // ---

    public void setText(String text)
    {
        switchView.setText(text);
    }

    public void setChecked(boolean checked)
    {
        switchView.setChecked(checked);
    }

    public boolean isChecked()
    {
        return switchView.isChecked();
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener)
    {
        switchView.setOnCheckedChangeListener(listener);
    }


    // ---
    // Helper
    // ---

    private int dp(int dp)
    {
        return AppConfigViewHelper.dp(dp);
    }
}
