package com.crescentflare.dynamicappconfig.adapter;

import android.content.Context;
import android.graphics.Color;
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

import com.crescentflare.dynamicappconfig.helper.AppConfigResourceHelper;

import java.util.ArrayList;

/**
 * Library adapter: basic selection list view
 * List view adapter for making configuration editing selections (of a list of strings)
 */
public class AppConfigChoiceAdapter extends BaseAdapter implements ListAdapter
{
    // ---
    // Members
    // ---

    private Context context;
    private ArrayList<String> choices = new ArrayList<>();


    // ---
    // Initialization
    // ---

    public AppConfigChoiceAdapter(Context context)
    {
        this.context = context;
    }


    // ---
    // Enabled check
    // ---

    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }

    @Override
    public boolean isEnabled(int i)
    {
        return true;
    }


    // ---
    // Item handling
    // ---

    @Override
    public int getCount()
    {
        return choices.size();
    }

    @Override
    public Object getItem(int i)
    {
        return choices.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }


    // ---
    // View handling
    // ---

    private int dip(int pixels)
    {
        return (int)(context.getResources().getDisplayMetrics().density * pixels);
    }

    private Drawable generateSelectionBackgroundDrawable()
    {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] {  android.R.attr.state_focused }, new ColorDrawable(AppConfigResourceHelper.getColor(context, "app_config_background")));
        drawable.addState(new int[] {  android.R.attr.state_pressed }, new ColorDrawable(AppConfigResourceHelper.getColor(context, "app_config_background")));
        drawable.addState(new int[] {  android.R.attr.state_enabled }, new ColorDrawable(Color.WHITE));
        drawable.addState(new int[] { -android.R.attr.state_enabled }, new ColorDrawable(Color.WHITE));
        return drawable;
    }

    private View generateView()
    {
        ViewHolder viewHolder = new ViewHolder();
        LinearLayout createdView = new LinearLayout(context);
        createdView.setOrientation(LinearLayout.VERTICAL);
        createdView.setBackgroundColor(Color.WHITE);
        createdView.addView(viewHolder.labelView = new TextView(context));
        createdView.addView(viewHolder.dividerView = new View(context));
        viewHolder.labelView.setGravity(Gravity.CENTER_VERTICAL);
        viewHolder.labelView.setMinimumHeight(dip(60));
        viewHolder.labelView.setPadding(dip(12), dip(12), dip(12), dip(12));
        viewHolder.labelView.setTextSize(18);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
        {
            viewHolder.labelView.setBackgroundDrawable(generateSelectionBackgroundDrawable());
        }
        else
        {
            viewHolder.labelView.setBackground(generateSelectionBackgroundDrawable());
        }
        viewHolder.dividerView.setBackgroundColor(AppConfigResourceHelper.getColor(context, "app_config_list_divider_line"));
        viewHolder.dividerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        ((LinearLayout.LayoutParams)viewHolder.dividerView.getLayoutParams()).setMargins(dip(12), 0, 0, 0);
        createdView.setTag(viewHolder);
        return createdView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        String choice = (String)getItem(i);
        if (view == null)
        {
            view = generateView();
        }
        if (view != null)
        {
            ViewHolder viewHolder = (ViewHolder)view.getTag();
            if (viewHolder.labelView != null)
            {
                viewHolder.labelView.setText(choice);
            }
            if (viewHolder.dividerView != null)
            {
                String nextChoice = null;
                if (i < choices.size() - 1)
                {
                    nextChoice = (String)getItem(i + 1);
                }
                viewHolder.dividerView.setVisibility(nextChoice != null ? View.VISIBLE : View.GONE);
            }
        }
        return view;
    }


    // ---
    // Update list and notify data change
    // ---

    public void setChoices(ArrayList<String> choices)
    {
        this.choices = choices;
        notifyDataSetChanged();
    }


    // ---
    // Listview tag to easily access subviews
    // ---

    public static class ViewHolder
    {
        public TextView labelView = null;
        public View dividerView = null;
    }
}
