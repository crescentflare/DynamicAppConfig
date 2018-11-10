package com.crescentflare.dynamicappconfig.view;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.R;
import com.crescentflare.dynamicappconfig.helper.AppConfigResourceHelper;
import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;

/**
 * Library view: a toolbar
 * Simulates a toolbar for all supported Android versions without using app compat
 */
public class AppConfigToolbar extends LinearLayout
{
    // ---
    // Members
    // ---

    private FrameLayout backButtonContainer;
    private ImageView backButtonImage;
    private TextView titleView;
    private TextView optionView;
    private int actionBarHeight;


    // ---
    // Initialization
    // ---

    public AppConfigToolbar(Context context)
    {
        super(context);
        init(context, null);
    }

    public AppConfigToolbar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigToolbar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        // Basic set up
        setOrientation(HORIZONTAL);
        setPadding(0, 0, dp(8), 0);

        // Determine and store action bar height
        TypedValue typedValue = new TypedValue();
        actionBarHeight = 0;
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
        }

        // Add back button container
        LayoutParams buttonContainerLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backButtonContainer = new FrameLayout(context);
        buttonContainerLayoutParams.setMargins(-dp(16), -dp(16), -dp(16), -dp(16));
        backButtonContainer.setLayoutParams(buttonContainerLayoutParams);
        backButtonContainer.setPadding(dp(16), dp(16), dp(16), dp(16));
        backButtonContainer.setBackgroundResource(R.drawable.app_config_toolbar_highlight_light);
        backButtonContainer.setVisibility(GONE);
        addView(backButtonContainer);

        // Add image to the button container
        LayoutParams imageLayoutParams = new LayoutParams(actionBarHeight, actionBarHeight);
        backButtonImage = new ImageView(context);
        backButtonImage.setLayoutParams(imageLayoutParams);
        backButtonImage.setImageResource(R.drawable.app_config_back_arrow);
        backButtonImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        backButtonContainer.addView(backButtonImage);

        // Add title view
        LayoutParams titleLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleView = new TextView(context);
        titleView.setId(R.id.app_config_toolbar_title);
        titleLayoutParams.leftMargin = dp(16);
        titleLayoutParams.weight = 1;
        titleLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        titleView.setLayoutParams(titleLayoutParams);
        titleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        titleView.setTextSize(20);
        titleView.setLines(1);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        addView(titleView);

        // Add option view
        LayoutParams optionLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        optionView = new TextView(context);
        optionLayoutParams.leftMargin = dp(8);
        optionLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        optionView.setLayoutParams(optionLayoutParams);
        optionView.setPadding(dp(8), dp(8), 0, dp(8));
        optionView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        optionView.setTextSize(14);
        optionView.setVisibility(GONE);
        addView(optionView);

        // Set default background color
        setBackgroundColor(AppConfigResourceHelper.getAccentColor(context));
    }


    // ---
    // Modify view
    // ---

    @Override
    public void setBackgroundColor(int color)
    {
        int lightColor = Color.WHITE;
        int darkColor = Color.BLACK;
        int foregroundColor = AppConfigResourceHelper.pickBestForegroundColor(color, lightColor, darkColor);
        super.setBackgroundColor(color);
        if (backButtonContainer != null)
        {
            backButtonContainer.setBackgroundResource(foregroundColor == lightColor ? R.drawable.app_config_toolbar_highlight_light : R.drawable.app_config_toolbar_highlight_dark);
        }
        if (backButtonImage != null)
        {
            backButtonImage.setColorFilter(new PorterDuffColorFilter(foregroundColor, PorterDuff.Mode.SRC_IN));
        }
        if (titleView != null)
        {
            titleView.setTextColor(foregroundColor);
        }
        if (optionView != null)
        {
            int[][] states = new int[][]
            {
                new int[] {  android.R.attr.state_focused }, // Focused
                new int[] {  android.R.attr.state_pressed }, // Pressed
                new int[] {  android.R.attr.state_enabled }, // Enabled
                new int[] { -android.R.attr.state_enabled }  // Disabled
            };
            int[] colors = new int[]
            {
                (foregroundColor & 0xFFFFFF) | 0x8F000000,
                (foregroundColor & 0xFFFFFF) | 0x8F000000,
                foregroundColor,
                (foregroundColor & 0xFFFFFF) | 0x8F000000
            };
            optionView.setTextColor(new ColorStateList(states, colors));
        }
    }

    public void setTitle(String title)
    {
        titleView.setText(title);
    }

    public void setOption(String option)
    {
        optionView.setText(option);
        optionView.setVisibility(TextUtils.isEmpty(option) ? GONE : VISIBLE);
    }

    public void setOptionEnabled(boolean enabled)
    {
        optionView.setEnabled(enabled);
    }


    // ---
    // Interaction
    // ---

    public void setBackOnClickListener(OnClickListener listener)
    {
        backButtonContainer.setOnClickListener(listener);
        backButtonContainer.setVisibility(listener != null ? VISIBLE : GONE);
    }

    public void setOptionOnClickListener(OnClickListener listener)
    {
        optionView.setOnClickListener(listener);
    }


    // ---
    // Helper
    // ---

    private int dp(int dp)
    {
        return AppConfigViewHelper.dp(dp);
    }


    // ---
    // Custom layout
    // ---

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (actionBarHeight > 0)
        {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(actionBarHeight, MeasureSpec.EXACTLY));
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
