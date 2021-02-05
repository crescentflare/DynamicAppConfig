package com.crescentflare.dynamicappconfig.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.R;
import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;

import java.util.ArrayList;

import static com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper.dp;

/**
 * Library adapter: basic selection list view
 * List view adapter for making configuration editing selections (of a list of strings)
 */
public class AppConfigChoiceAdapter extends BaseAdapter implements ListAdapter {

    // --
    // Members
    // --

    private final Context context;
    private ArrayList<String> choices = new ArrayList<>();


    // --
    // Initialization
    // --

    public AppConfigChoiceAdapter(Context context) {
        this.context = context;
    }


    // --
    // Enabled check
    // --

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }


    // --
    // Item handling
    // --

    @Override
    public int getCount() {
        return choices.size();
    }

    @Override
    public Object getItem(int i) {
        return choices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    // --
    // View handling
    // --

    private Drawable generateSelectionBackgroundDrawable() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] {  android.R.attr.state_focused }, new ColorDrawable(AppConfigViewHelper.getColor(context, R.color.app_config_background)));
        drawable.addState(new int[] {  android.R.attr.state_pressed }, new ColorDrawable(AppConfigViewHelper.getColor(context, R.color.app_config_background)));
        drawable.addState(new int[] {  android.R.attr.state_enabled }, new ColorDrawable(AppConfigViewHelper.getColor(context, R.color.app_config_cell_background)));
        drawable.addState(new int[] { -android.R.attr.state_enabled }, new ColorDrawable(AppConfigViewHelper.getColor(context, R.color.app_config_cell_background)));
        return drawable;
    }

    private View generateView() {
        ViewHolder viewHolder = new ViewHolder();
        LinearLayout createdView = new LinearLayout(context);
        createdView.setOrientation(LinearLayout.VERTICAL);
        createdView.setBackgroundColor(AppConfigViewHelper.getColor(context, R.color.app_config_cell_background));
        createdView.addView(viewHolder.labelView = new TextView(context));
        createdView.addView(viewHolder.dividerView = new View(context));
        viewHolder.labelView.setGravity(Gravity.CENTER_VERTICAL);
        viewHolder.labelView.setMinimumHeight(dp(60));
        viewHolder.labelView.setPadding(dp(12), dp(12), dp(12), dp(12));
        viewHolder.labelView.setTextSize(18);
        viewHolder.labelView.setTextColor(AppConfigViewHelper.getColor(context, R.color.app_config_text));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            viewHolder.labelView.setBackgroundDrawable(generateSelectionBackgroundDrawable());
        } else {
            viewHolder.labelView.setBackground(generateSelectionBackgroundDrawable());
        }
        viewHolder.dividerView.setBackgroundColor(AppConfigViewHelper.getColor(context, R.color.app_config_list_divider_line));
        viewHolder.dividerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        ((LinearLayout.LayoutParams)viewHolder.dividerView.getLayoutParams()).setMargins(dp(12), 0, 0, 0);
        createdView.setTag(viewHolder);
        return createdView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String choice = (String)getItem(i);
        if (view == null) {
            view = generateView();
        }
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        if (viewHolder.labelView != null) {
            viewHolder.labelView.setText(choice);
        }
        if (viewHolder.dividerView != null) {
            String nextChoice = null;
            if (i < choices.size() - 1) {
                nextChoice = (String)getItem(i + 1);
            }
            viewHolder.dividerView.setVisibility(nextChoice != null ? View.VISIBLE : View.GONE);
        }
        return view;
    }


    // --
    // Update list and notify data change
    // --

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
        notifyDataSetChanged();
    }


    // --
    // List view tag to easily access subviews
    // --

    public static class ViewHolder {
        TextView labelView = null;
        View dividerView = null;
    }
}
