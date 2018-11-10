package com.crescentflare.dynamicappconfig.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.R;
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
    private boolean isEmpty = true;


    // ---
    // Initialization
    // ---

    public AppConfigEditableCell(Context context)
    {
        super(context);
        init(context, null);
    }

    public AppConfigEditableCell(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AppConfigEditableCell(Context context, AttributeSet attrs, int defStyleAttr)
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
        labelView.setTextColor(Color.GRAY);

        // Add value view
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.addView(valueView = new TextView(context));
        valueView.setLayoutParams(layoutParams);
        valueView.setTextSize(18);
        valueView.setTextColor(Color.LTGRAY);
        valueView.setPadding(dp(4), dp(2), dp(4), 0);
        valueView.setText(getContext().getString(R.string.app_config_edited_value_empty));
        valueView.setTypeface(null, Typeface.ITALIC);
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
        isEmpty = TextUtils.isEmpty(value);
        valueView.setText(isEmpty ? getContext().getString(R.string.app_config_edited_value_empty) : value);
        valueView.setTypeface(null, isEmpty ? Typeface.ITALIC : Typeface.NORMAL);
        valueView.setTextColor(isEmpty ? Color.LTGRAY : Color.DKGRAY);
    }

    public String getValue()
    {
        if (isEmpty)
        {
            return "";
        }
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
