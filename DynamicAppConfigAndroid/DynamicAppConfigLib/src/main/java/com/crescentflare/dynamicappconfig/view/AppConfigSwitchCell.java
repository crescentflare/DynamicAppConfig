package com.crescentflare.dynamicappconfig.view;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import static com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper.dp;

/**
 * Library view: simple cell
 * Simulates a list view cell as a switch item
 */
public class AppConfigSwitchCell extends FrameLayout
{
    // ---
    // Members
    // ---

    private Switch switchView;


    // ---
    // Factory methods
    // ---

    public static AppConfigSwitchCell generateSwitchView(Context context, String label, boolean setting, CompoundButton.OnCheckedChangeListener changeListener)
    {
        AppConfigSwitchCell switchView = new AppConfigSwitchCell(context);
        LinearLayout.LayoutParams switchViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switchView.setLayoutParams(switchViewLayoutParams);
        switchView.setText(label);
        switchView.setChecked(setting);
        switchView.setTag(label);
        switchView.setOnCheckedChangeListener(changeListener);
        return switchView;
    }


    // ---
    // Initialization
    // ---

    public AppConfigSwitchCell(Context context)
    {
        // Prepare container
        super(context);
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setPadding(dp(12), 0, dp(12), 0);
        addView(container);

        // Add switch view
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        container.addView(switchView = new Switch(context));
        switchView.setLayoutParams(layoutParams);
        switchView.setMinimumHeight(dp(60));
        switchView.setPadding(0, dp(12), 0, dp(12));
        switchView.setTextSize(18);
        switchView.setTextColor(Color.DKGRAY);
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
}
