package com.crescentflare.dynamicappconfig.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.R;
import com.crescentflare.dynamicappconfig.adapter.AppConfigChoiceAdapter;
import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;
import com.crescentflare.dynamicappconfig.view.AppConfigToolbar;

import java.util.ArrayList;

import static com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper.dp;

/**
 * Library activity: selection activity
 * Select an item from a given set of strings, used when needed to make a choice out of a limited set of options
 */
public class AppConfigStringChoiceActivity extends Activity
{
    // ---
    // Constants
    // ---

    public static final String ARG_INTENT_RESULT_SELECTED_INDEX = "ARG_INTENT_RESULT_SELECTED_INDEX";
    public static final String ARG_INTENT_RESULT_SELECTED_STRING = "ARG_INTENT_RESULT_SELECTED_STRING";
    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_SELECTION_TITLE = "ARG_SELECTION_TITLE";
    private static final String ARG_CHOICES = "ARG_CHOICES";


    // ---
    // Initialization
    // ---

    public static Intent newInstance(Context context, String title, String selectionTitle, ArrayList<String> options)
    {
        Intent intent = new Intent(context, AppConfigStringChoiceActivity.class);
        intent.putExtra(ARG_TITLE, title);
        intent.putExtra(ARG_SELECTION_TITLE, selectionTitle);
        intent.putExtra(ARG_CHOICES, options);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Create main layout
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);
        setTitle(getIntent().getStringExtra(ARG_TITLE));

        // Add a toolbar on top
        AppConfigToolbar toolbar = new AppConfigToolbar(this);
        toolbar.setBackOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
        toolbar.setTitle(getTitle().toString());
        layout.addView(toolbar);

        // Add list view
        ListView listView = new ListView(this);
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setBackgroundColor(AppConfigViewHelper.getColor(this, R.color.app_config_background));
        listView.setDivider(null);
        listView.setDividerHeight(0);
        layout.addView(listView);

        // Add header and footer
        listView.setHeaderDividersEnabled(false);
        listView.setFooterDividersEnabled(false);
        listView.addHeaderView(generateHeader());
        listView.addFooterView(generateFooter());

        // Set adapter
        AppConfigChoiceAdapter adapter = new AppConfigChoiceAdapter(this);
        listView.setAdapter(adapter);
        adapter.setChoices(getIntent().getStringArrayListExtra(ARG_CHOICES));

        // Listview click handler
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (parent.getItemAtPosition(position) != null)
                {
                    Intent intent = AppConfigStringChoiceActivity.this.getIntent();
                    intent.putExtra(ARG_INTENT_RESULT_SELECTED_INDEX, position);
                    intent.putExtra(ARG_INTENT_RESULT_SELECTED_STRING, (String) parent.getItemAtPosition(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public static void startWithResult(Activity fromActivity, String title, String selectionTitle, ArrayList<String> options, int resultCode)
    {
        fromActivity.startActivityForResult(newInstance(fromActivity, title, selectionTitle, options), resultCode);
    }


    // ---
    // View handling
    // ---

    private View generateHeader()
    {
        LinearLayout createdView = new LinearLayout(this);
        TextView labelView = new TextView(this);
        createdView.setOrientation(LinearLayout.VERTICAL);
        createdView.setBackgroundColor(AppConfigViewHelper.getColor(this, R.color.app_config_cell_background));
        createdView.addView(labelView);
        labelView.setPadding(dp(12), dp(12), dp(12), dp(12));
        labelView.setTypeface(Typeface.DEFAULT_BOLD);
        labelView.setTextColor(AppConfigViewHelper.getAccentColor(this));
        labelView.setText(getIntent().getStringExtra(ARG_SELECTION_TITLE));
        return createdView;
    }

    private View generateFooter()
    {
        // Create container
        LinearLayout dividerLayout = new LinearLayout(this);
        dividerLayout.setOrientation(LinearLayout.VERTICAL);

        // Top line divider (edge)
        View topLineView = new View(this);
        topLineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        topLineView.setBackgroundColor(AppConfigViewHelper.getColor(this, R.color.app_config_section_divider_line));
        dividerLayout.addView(topLineView);

        // Middle divider (gradient on background)
        View gradientView = new View(this);
        int colors[] = new int[]
        {
            AppConfigViewHelper.getColor(this, R.color.app_config_section_gradient_start),
            AppConfigViewHelper.getColor(this, R.color.app_config_section_gradient_end),
            AppConfigViewHelper.getColor(this, R.color.app_config_background)
        };
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        gradientView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(8)));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
        {
            gradientView.setBackgroundDrawable(drawable);
        }
        else
        {
            gradientView.setBackground(drawable);
        }
        dividerLayout.addView(gradientView);

        // Return created view
        return dividerLayout;
    }
}
