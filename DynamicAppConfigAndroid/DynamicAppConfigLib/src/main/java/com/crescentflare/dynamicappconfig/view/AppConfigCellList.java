package com.crescentflare.dynamicappconfig.view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.helper.AppConfigResourceHelper;
import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;

/**
 * Library view: a cell container
 * Simulates a list view with cells and dividers
 */
public class AppConfigCellList extends LinearLayout
{
    // ---
    // Members
    // ---

    private View previousItemView = null;


    // ---
    // Initialization
    // ---

    public AppConfigCellList(Context context)
    {
        super(context);
        init(context, null);
    }

    public AppConfigCellList(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigCellList(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigCellList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        setOrientation(VERTICAL);
    }


    // ---
    // Add items
    // ---

    public void startSection(String headerText)
    {
        // Add top divider if needed
        if (getChildCount() > 0)
        {
            View topLineView = new View(getContext());
            topLineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            topLineView.setBackgroundColor(AppConfigResourceHelper.getColor(getContext(), "app_config_section_divider_line"));
            addView(topLineView);
        }

        // Add header label
        LinearLayout createdView = new LinearLayout(getContext());
        TextView labelView;
        createdView.setOrientation(LinearLayout.VERTICAL);
        createdView.setBackgroundColor(Color.WHITE);
        createdView.addView(labelView = new TextView(getContext()));
        labelView.setPadding(dp(12), dp(12), dp(12), dp(12));
        labelView.setTypeface(Typeface.DEFAULT_BOLD);
        labelView.setTextColor(AppConfigResourceHelper.getAccentColor(getContext()));
        labelView.setText(headerText);
        addView(createdView);
        previousItemView = null;
    }

    public void endSection()
    {
        // Top line divider (edge)
        View topLineView = new View(getContext());
        topLineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        topLineView.setBackgroundColor(AppConfigResourceHelper.getColor(getContext(), "app_config_section_divider_line"));
        addView(topLineView);

        // Gradient divider
        View gradientView = new View(getContext());
        int colors[] = new int[]
        {
            AppConfigResourceHelper.getColor(getContext(), "app_config_section_gradient_start"),
            AppConfigResourceHelper.getColor(getContext(), "app_config_section_gradient_end"),
            AppConfigResourceHelper.getColor(getContext(), "app_config_background")
        };
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        gradientView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(8)));
        AppConfigViewHelper.setBackgroundDrawable(gradientView, drawable);
        addView(gradientView);
    }

    public void addSectionItem(View view)
    {
        // Add divider line
        if (previousItemView != null && (!(previousItemView instanceof AppConfigEditableOldCell) || !(view instanceof AppConfigEditableOldCell)))
        {
            View topLineView = new View(getContext());
            topLineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            topLineView.setBackgroundColor(AppConfigResourceHelper.getColor(getContext(), "app_config_section_divider_line"));
            addView(topLineView);
        }

        // Add view
        view.setBackgroundColor(Color.WHITE);
        addView(view);
        previousItemView = view;
    }

    @Override
    public void removeAllViews()
    {
        super.removeAllViews();
        previousItemView = null;
    }


    // ---
    // Helper
    // ---

    private int dp(int dp)
    {
        return AppConfigViewHelper.dp(dp);
    }
}
