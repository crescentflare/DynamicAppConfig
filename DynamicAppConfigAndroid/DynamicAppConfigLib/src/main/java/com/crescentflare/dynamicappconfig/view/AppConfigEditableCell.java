package com.crescentflare.dynamicappconfig.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;

/**
 * Library view: editable cell
 * Simulates a list view cell showing an editable value, clicking on it should open a dialog box with an edit text to change its value
 */
public class AppConfigEditableCell extends FrameLayout
{
    // ---
    // Members
    // ---

    private TextView labelView;
    private TextView valueView;
    private boolean isNumberLimit = false;


    // ---
    // Initialization
    // ---

    public AppConfigEditableCell(Context context)
    {
        super(context);
        init(context, null);
    }

    public AppConfigEditableCell(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigEditableCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigEditableCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        // Prepare container
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setPadding(dp(10), dp(12), dp(10), dp(12));
        addView(container);

        // Add label view
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.addView(labelView = new TextView(context));
        labelView.setLayoutParams(layoutParams);
        labelView.setPadding(dp(4), 0, dp(4), 0);

        // Add value view
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.addView(valueView = new TextView(context));
        valueView.setLayoutParams(layoutParams);
        valueView.setTextSize(18);
        valueView.setTextColor(Color.BLACK);
        valueView.setPadding(dp(4), dp(2), dp(4), 0);
    }


    // ---
    // Modify view
    // ---

    public void setDescription(String text)
    {
        labelView.setText(text);
    }

    public void setValue(String value)
    {
        valueView.setText(value);
    }

    public String getValue()
    {
        return valueView.getText().toString();
    }

    public void setNumberLimit(boolean numbersOnly)
    {
        isNumberLimit = numbersOnly;
    }

    public boolean isNumberLimit()
    {
        return isNumberLimit;
    }


    // ---
    // Helper
    // ---

    private int dp(int dp)
    {
        return AppConfigViewHelper.dp(dp);
    }
}
