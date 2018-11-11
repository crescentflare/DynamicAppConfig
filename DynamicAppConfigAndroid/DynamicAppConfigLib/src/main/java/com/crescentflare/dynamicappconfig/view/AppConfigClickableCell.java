package com.crescentflare.dynamicappconfig.view;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.R;
import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;

import static com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper.dp;

/**
 * Library view: clickable cell
 * Simulates a simple list view cell as a clickable item
 */
public class AppConfigClickableCell extends FrameLayout
{
    // ---
    // Members
    // ---

    private TextView labelView;
    private TextView valueView;


    // ---
    // Factory methods
    // ---

    public static AppConfigClickableCell generateButtonView(Context context, String action)
    {
        return generateButtonView(context, null, action, false);
    }

    public static AppConfigClickableCell generateButtonView(Context context, String label, String setting)
    {
        return generateButtonView(context, label, setting, false);
    }

    public static AppConfigClickableCell generateButtonView(Context context, String action, boolean edited)
    {
        return generateButtonView(context, null, action, edited);
    }

    public static AppConfigClickableCell generateButtonView(Context context, String label, String setting, boolean edited)
    {
        AppConfigClickableCell cellView = new AppConfigClickableCell(context);
        cellView.setTag(label);
        cellView.setText(setting);
        if (edited)
        {
            cellView.setValue(context.getString(R.string.app_config_item_edited));
        }
        return cellView;
    }


    // ---
    // Initialization
    // ---

    public AppConfigClickableCell(Context context)
    {
        // Prepare container
        super(context);
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setMinimumHeight(dp(60));
        container.setPadding(dp(12), dp(12), dp(12), dp(12));
        AppConfigViewHelper.setBackgroundDrawable(container, generateSelectionBackgroundDrawable());
        addView(container);

        // Add label view
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        container.addView(labelView = new TextView(context));
        labelView.setLayoutParams(layoutParams);
        labelView.setTextSize(18);
        labelView.setTextColor(Color.DKGRAY);

        // Add value view
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        container.addView(valueView = new TextView(context));
        valueView.setLayoutParams(layoutParams);
        valueView.setPadding(dp(8), 0, 0, 0);
        valueView.setTextSize(14);
        valueView.setTextColor(Color.LTGRAY);
        valueView.setVisibility(GONE);
    }


    // ---
    // Modify view
    // ---

    public void setText(String text)
    {
        labelView.setText(text);
    }

    public String getText()
    {
        return labelView.getText().toString();
    }

    public void setValue(String value)
    {
        valueView.setText(value);
        valueView.setVisibility(TextUtils.isEmpty(value) ? GONE : VISIBLE);
    }


    // ---
    // Helper
    // ---

    private Drawable generateSelectionBackgroundDrawable()
    {
        // Create ripple drawable if available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            // Set up color state list
            int[][] states = new int[][]
            {
                new int[] {  android.R.attr.state_focused }, // Focused
                new int[] {  android.R.attr.state_pressed }, // Pressed
                new int[] {  android.R.attr.state_enabled }, // Enabled
                new int[] { -android.R.attr.state_enabled }  // Disabled
            };
            int[] colors = new int[]
            {
                AppConfigViewHelper.getColor(getContext(), R.color.app_config_background),
                AppConfigViewHelper.getColor(getContext(), R.color.app_config_background),
                Color.WHITE,
                Color.WHITE
            };

            // And create ripple drawable effect
            return new RippleDrawable(new ColorStateList(states, colors), null, null);
        }

        // Create generic state drawable otherwise
        StateListDrawable stateDrawable = new StateListDrawable();
        stateDrawable.addState(new int[]{  android.R.attr.state_focused }, new ColorDrawable(AppConfigViewHelper.getColor(getContext(), R.color.app_config_background)));
        stateDrawable.addState(new int[]{  android.R.attr.state_pressed }, new ColorDrawable(AppConfigViewHelper.getColor(getContext(), R.color.app_config_background)));
        stateDrawable.addState(new int[]{  android.R.attr.state_enabled }, new ColorDrawable(Color.WHITE));
        stateDrawable.addState(new int[]{ -android.R.attr.state_enabled }, new ColorDrawable(Color.WHITE));
        return stateDrawable;
    }
}
