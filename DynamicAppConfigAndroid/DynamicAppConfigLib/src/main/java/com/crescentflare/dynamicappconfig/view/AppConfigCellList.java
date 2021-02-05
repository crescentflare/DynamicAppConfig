package com.crescentflare.dynamicappconfig.view;


import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.R;
import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;

import static com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper.dp;

/**
 * Library view: a cell container
 * Simulates a list view with cells and dividers
 */
public class AppConfigCellList extends LinearLayout {

    // --
    // Members
    // --

    private View previousItemView = null;


    // --
    // Initialization
    // --

    public AppConfigCellList(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }


    // --
    // Add items
    // --

    public void startSection(String headerText) {
        // Add top divider if needed
        if (getChildCount() > 0) {
            View topLineView = new View(getContext());
            topLineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            topLineView.setBackgroundColor(AppConfigViewHelper.getColor(getContext(), R.color.app_config_section_divider_line));
            addView(topLineView);
        }

        // Add header label
        LinearLayout createdView = new LinearLayout(getContext());
        TextView labelView;
        createdView.setOrientation(LinearLayout.VERTICAL);
        createdView.setBackgroundColor(AppConfigViewHelper.getColor(getContext(), R.color.app_config_cell_background));
        createdView.addView(labelView = new TextView(getContext()));
        labelView.setPadding(dp(12), dp(12), dp(12), dp(12));
        labelView.setTypeface(Typeface.DEFAULT_BOLD);
        labelView.setTextColor(AppConfigViewHelper.getAccentColor(getContext()));
        labelView.setText(headerText);
        addView(createdView);
        previousItemView = null;
    }

    public void endSection() {
        // Top line divider (edge)
        View topLineView = new View(getContext());
        topLineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        topLineView.setBackgroundColor(AppConfigViewHelper.getColor(getContext(), R.color.app_config_section_divider_line));
        addView(topLineView);

        // Gradient divider
        View gradientView = new View(getContext());
        int[] colors = new int[] {
            AppConfigViewHelper.getColor(getContext(), R.color.app_config_section_gradient_start),
            AppConfigViewHelper.getColor(getContext(), R.color.app_config_section_gradient_end),
            AppConfigViewHelper.getColor(getContext(), R.color.app_config_background)
        };
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        gradientView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(8)));
        AppConfigViewHelper.setBackgroundDrawable(gradientView, drawable);
        addView(gradientView);
    }

    public void addSectionItem(View view) {
        // Add divider line
        if (previousItemView != null) {
            View topLineView = new View(getContext());
            topLineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            topLineView.setBackgroundColor(AppConfigViewHelper.getColor(getContext(), R.color.app_config_list_divider_line));
            addView(topLineView);
        }

        // Add view
        view.setBackgroundColor(AppConfigViewHelper.getColor(getContext(), R.color.app_config_cell_background));
        addView(view);
        previousItemView = view;
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        previousItemView = null;
    }
}
