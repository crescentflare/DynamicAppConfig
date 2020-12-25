package com.crescentflare.dynamicappconfig.view;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.R;
import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;

import static com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper.dp;

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
    // Factory methods
    // ---

    public static AppConfigSimpleCell generateInfoView(Context context, String infoLabel, String infoValue)
    {
        AppConfigSimpleCell cellView = new AppConfigSimpleCell(context);
        cellView.setText(infoLabel + ": " + infoValue);
        return cellView;
    }


    // ---
    // Initialization
    // ---

    public AppConfigSimpleCell(Context context)
    {
        // Prepare container
        super(context);
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
        labelView.setTextColor(AppConfigViewHelper.getColor(getContext(), R.color.app_config_text));
    }


    // ---
    // Modify view
    // ---

    public void setText(String text)
    {
        labelView.setText(text);
    }
}
