package com.crescentflare.dynamicappconfig.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.R;
import com.crescentflare.dynamicappconfig.helper.AppConfigAlertHelper;
import com.crescentflare.dynamicappconfig.helper.AppConfigResourceHelper;
import com.crescentflare.dynamicappconfig.helper.AppConfigViewHelper;
import com.crescentflare.dynamicappconfig.manager.AppConfigStorage;
import com.crescentflare.dynamicappconfig.model.AppConfigBaseModel;
import com.crescentflare.dynamicappconfig.model.AppConfigStorageItem;
import com.crescentflare.dynamicappconfig.view.AppConfigCellList;
import com.crescentflare.dynamicappconfig.view.AppConfigClickableCell;
import com.crescentflare.dynamicappconfig.view.AppConfigEditableCell;
import com.crescentflare.dynamicappconfig.view.AppConfigSwitchCell;

import java.util.ArrayList;

/**
 * Library activity: editing activity
 * Be able to change or create a new configuration copy
 */
public class EditAppConfigActivity extends AppCompatActivity
{
    // ---
    // Constants
    // ---

    private static final String ARG_CONFIG_NAME = "ARG_CONFIG_NAME";
    private static final String ARG_CREATE_CUSTOM = "ARG_CREATE_CUSTOM";
    private static final int RESULT_CODE_SELECT_ENUM = 1004;


    // ---
    // Members
    // ---

    private ArrayList<View> fieldViews = new ArrayList<>();
    private LinearLayout layout = null;
    private AppConfigCellList editingView = null;
    private LinearLayout spinnerView = null;
    private AppConfigStorageItem initialEditValues = null;


    // ---
    // Initialization
    // ---

    public static Intent newInstance(Context context, String config, boolean createCustom)
    {
        Intent intent = new Intent(context, EditAppConfigActivity.class);
        intent.putExtra(ARG_CONFIG_NAME, config);
        intent.putExtra(ARG_CREATE_CUSTOM, createCustom);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Create layout and configure action bar
        super.onCreate(savedInstanceState);
        layout = createContentView();
        setTitle(AppConfigResourceHelper.getString(this, getIntent().getBooleanExtra(ARG_CREATE_CUSTOM, false) ? "app_config_title_edit_new" : "app_config_title_edit"));
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        setContentView(layout);

        // Load data and populate content
        AppConfigStorage.instance.loadFromSource(this, new Runnable()
        {
            @Override
            public void run()
            {
                populateContent();
                initialEditValues = fetchEditedValues();
            }
        });
    }

    public static void startWithResult(Activity fromActivity, String config, boolean createCustom, int resultCode)
    {
        fromActivity.startActivityForResult(newInstance(fromActivity, config, createCustom), resultCode);
    }


    // ---
    // State handling
    // ---

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode >= RESULT_CODE_SELECT_ENUM && requestCode < RESULT_CODE_SELECT_ENUM + 1000 && resultCode == RESULT_OK)
        {
            String resultString = data.getStringExtra(AppConfigStringChoiceActivity.ARG_INTENT_RESULT_SELECTED_STRING);
            if (resultString.length() > 0)
            {
                int index = requestCode - RESULT_CODE_SELECT_ENUM;
                if (index < fieldViews.size() && fieldViews.get(index) instanceof AppConfigClickableCell)
                {
                    ((AppConfigClickableCell)fieldViews.get(index)).setText(fieldViews.get(index).getTag() + ": " + resultString);
                }
            }
            supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void onBackPressed()
    {
        boolean hasChange = false;
        if (initialEditValues != null)
        {
            hasChange = !fetchEditedValues().equals(initialEditValues);
        }
        if (hasChange)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle(AppConfigResourceHelper.getString(this, "app_config_title_dialog_confirm_save_changes"))
                    .setCancelable(true)
                    .setPositiveButton(AppConfigResourceHelper.getString(this, "app_config_action_confirm"), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            saveData();
                        }
                    })
                    .setNegativeButton(AppConfigResourceHelper.getString(this, "app_config_action_deny"), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    });
            alert.show();
        }
        else
        {
            super.onBackPressed();
        }
    }


    // ---
    // Menu handling
    // ---

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.edit, menu);
        if (menu == null)
        {
            return false;
        }
        menu.findItem(R.id.app_config_menu_save).setVisible(!getIntent().getBooleanExtra(ARG_CREATE_CUSTOM, false));
        menu.findItem(R.id.app_config_menu_create).setVisible(getIntent().getBooleanExtra(ARG_CREATE_CUSTOM, false));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        boolean hasChange = false;
        if (initialEditValues != null)
        {
            hasChange = !fetchEditedValues().equals(initialEditValues);
        }
        menu.findItem(R.id.app_config_menu_save).setEnabled(hasChange);
        menu.findItem(R.id.app_config_menu_create).setEnabled(hasChange);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.app_config_menu_create || item.getItemId() == R.id.app_config_menu_save)
        {
            saveData();
            return true;
        }
        else if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // ---
    // View component generators
    // ---

    private int dp(int dp)
    {
        return AppConfigViewHelper.dp(dp);
    }

    private AppConfigClickableCell generateButtonView(String action)
    {
        return generateButtonView(null, action);
    }

    private AppConfigClickableCell generateButtonView(String label, String setting)
    {
        AppConfigClickableCell cellView = new AppConfigClickableCell(this);
        cellView.setTag(label);
        cellView.setText(setting);
        return cellView;
    }

    private AppConfigEditableCell generateEditableView(final String label, final String setting, final boolean limitNumbers)
    {
        final AppConfigEditableCell editView = new AppConfigEditableCell(this);
        editView.setDescription(label);
        editView.setValue(setting);
        editView.setNumberLimit(limitNumbers);
        editView.setTag(label);
        editView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AppConfigAlertHelper.inputDialog(EditAppConfigActivity.this, getString(R.string.app_config_title_dialog_edit_value, label), label, editView.getValue(), limitNumbers ? AppConfigAlertHelper.InputType.NumbersOnly : AppConfigAlertHelper.InputType.Normal, new AppConfigAlertHelper.OnAlertInputListener()
                {
                    @Override
                    public void onInputEntered(String text)
                    {
                        editView.setValue(text);
                        supportInvalidateOptionsMenu();
                    }

                    @Override
                    public void onInputCanceled()
                    {
                        // No implementation
                    }
                });
            }
        });
        return editView;
    }

    private AppConfigSwitchCell generateSwitchView(String label, boolean setting)
    {
        AppConfigSwitchCell switchView = new AppConfigSwitchCell(this);
        LinearLayout.LayoutParams switchViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switchView.setLayoutParams(switchViewLayoutParams);
        switchView.setText(label);
        switchView.setChecked(setting);
        switchView.setTag(label);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                supportInvalidateOptionsMenu();
            }
        });
        return switchView;
    }


    // ---
    // View and layout generation
    // ---

    private LinearLayout createContentView()
    {
        // Create main layout
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add a toolbar on top (if no action bar is present)
        if (getSupportActionBar() == null)
        {
            Toolbar bar = new Toolbar(this);
            layout.addView(bar, 0);
            setSupportActionBar(bar);
        }

        // Add frame layout to contain the editing views or loading indicator
        FrameLayout container = new FrameLayout(this);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.setBackgroundColor(AppConfigResourceHelper.getColor(this, "app_config_background"));
        layout.addView(container);

        // Add editing view for changing configuration
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editingView = new AppConfigCellList(this);
        editingView.setVisibility(View.GONE);
        scrollView.addView(editingView);
        container.addView(scrollView);

        // Add spinner view for loading
        spinnerView = new LinearLayout(this);
        spinnerView.setBackgroundColor(Color.WHITE);
        spinnerView.setGravity(Gravity.CENTER);
        spinnerView.setOrientation(LinearLayout.VERTICAL);
        spinnerView.setPadding(dp(8), dp(8), dp(8), dp(8));
        container.addView(spinnerView);

        // Add progress bar to it (animated spinner)
        ProgressBar iconView = new ProgressBar(this);
        spinnerView.addView(iconView);

        // Add loading text to it
        TextView progressTextView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, dp(12), 0, 0);
        progressTextView.setLayoutParams(layoutParams);
        progressTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        progressTextView.setText(AppConfigResourceHelper.getString(this, "app_config_loading"));
        spinnerView.addView(progressTextView);
        return layout;
    }

    private void generateEditingContent(String category, ArrayList<String> values, AppConfigStorageItem config, AppConfigBaseModel baseModel)
    {
        // Create container
        LinearLayout fieldEditLayout = new LinearLayout(this);
        String title = getIntent().getBooleanExtra(ARG_CREATE_CUSTOM, false) ? AppConfigResourceHelper.getString(this, "app_config_header_edit_new") : getIntent().getStringExtra(ARG_CONFIG_NAME);
        if (category != null)
        {
            if (category.length() > 0)
            {
                title += ": " + category;
            }
            else
            {
                title += ": " + AppConfigResourceHelper.getString(this, "app_config_header_edit_other");
            }
        }
        editingView.startSection(title);

        // Fetch objects and filter by category
        ArrayList<String> editValues = new ArrayList<>();
        ArrayList<Object> editObjects = new ArrayList<>();
        for (String value : values)
        {
            boolean belongsToCategory = true;
            if (category != null && baseModel != null)
            {
                belongsToCategory = baseModel.valueBelongsToCategory(value, category);
            }
            if (belongsToCategory && !value.equals("name"))
            {
                editValues.add(value);
                editObjects.add(baseModel != null ? baseModel.getCurrentValue(value) : config.get(value));
            }
        }

        // Add editing views
        for (int i = 0; i < editValues.size(); i++)
        {
            final String value = editValues.get(i);
            View layoutView = null;
            final Object previousResult = i > 0 ? editObjects.get(i - 1) : null;
            final Object result = editObjects.get(i);
            if (result != null)
            {
                if (result instanceof Boolean)
                {
                    layoutView = generateSwitchView(value, (Boolean)result);
                }
                else if (result.getClass().isEnum())
                {
                    final int index = fieldViews.size();
                    layoutView = generateButtonView(value, value + ": " + result.toString());
                    layoutView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Object constants[] = result.getClass().getEnumConstants();
                            ArrayList<String> enumValues = new ArrayList<>();
                            for (int i = 0; i < constants.length; i++)
                            {
                                enumValues.add(constants[i].toString());
                            }
                            if (enumValues.size() > 0)
                            {
                                AppConfigStringChoiceActivity.startWithResult(
                                        EditAppConfigActivity.this,
                                        AppConfigResourceHelper.getString(EditAppConfigActivity.this, "app_config_title_choose_enum_prefix") + " " + value,
                                        AppConfigResourceHelper.getString(EditAppConfigActivity.this, "app_config_header_choose_enum"),
                                        enumValues,
                                        RESULT_CODE_SELECT_ENUM + index
                                );
                            }
                        }
                    });
                }
                else if (result instanceof Integer || result instanceof Long)
                {
                    layoutView = generateEditableView(value, "" + result, true);
                }
                else if (result instanceof String)
                {
                    layoutView = generateEditableView(value, (String)result, false);
                }
                if (layoutView != null)
                {
                    editingView.addSectionItem(layoutView);
                    fieldViews.add(layoutView.findViewWithTag(value));
                }
            }
        }

        // End section
        editingView.endSection();
    }

    private void populateContent()
    {
        // Show/hide spinner depending on the config being loaded
        spinnerView.setVisibility(AppConfigStorage.instance.isLoaded() ? View.GONE : View.VISIBLE);
        editingView.setVisibility(AppConfigStorage.instance.isLoaded() ? View.VISIBLE : View.GONE);
        if (!AppConfigStorage.instance.isLoaded())
        {
            return;
        }

        // Clear all views to re-populate
        editingView.removeAllViews();
        fieldViews.clear();

        // Determine values and categories
        AppConfigStorageItem config = AppConfigStorage.instance.getConfigNotNull(getIntent().getStringExtra(ARG_CONFIG_NAME));
        ArrayList<String> values = config.valueList();
        ArrayList<String> categories = new ArrayList<>();
        AppConfigBaseModel baseModel = null;
        if (AppConfigStorage.instance.getConfigManager() != null)
        {
            baseModel = AppConfigStorage.instance.getConfigManager().getBaseModelInstance();
            baseModel.applyCustomSettings(getIntent().getStringExtra(ARG_CONFIG_NAME), config);
            values = AppConfigStorage.instance.getConfigManager().getBaseModelInstance().configurationValueList();
            categories = baseModel.getConfigurationCategories();
        }

        // Add section for name (if applicable)
        if (AppConfigStorage.instance.isCustomConfig(getIntent().getStringExtra(ARG_CONFIG_NAME)) || getIntent().getBooleanExtra(ARG_CREATE_CUSTOM, false))
        {
            String name = getIntent().getStringExtra(ARG_CONFIG_NAME);
            if (getIntent().getBooleanExtra(ARG_CREATE_CUSTOM, false))
            {
                name += " " + AppConfigResourceHelper.getString(this, "app_config_modifier_copy");
            }
            AppConfigEditableCell editableCell = generateEditableView("name", name, false);
            editingView.startSection(AppConfigResourceHelper.getString(this, "app_config_header_edit_name"));
            editingView.addSectionItem(editableCell);
            editingView.endSection();
            fieldViews.add(editableCell.findViewWithTag("name"));
        }

        // Add editing fields to view
        if (categories.size() > 0)
        {
            for (String category : categories)
            {
                generateEditingContent(category, values, config, baseModel);
            }
        }
        else
        {
            generateEditingContent(null, values, config, baseModel);
        }

        // Start button section
        editingView.startSection(AppConfigResourceHelper.getString(this, "app_config_header_edit_actions"));

        // Add buttons
        if (getIntent().getBooleanExtra(ARG_CREATE_CUSTOM, false))
        {
            AppConfigClickableCell createButton = generateButtonView(AppConfigResourceHelper.getString(this, "app_config_action_ok_edit_new"));
            createButton.setId(AppConfigResourceHelper.getIdentifier(this, "app_config_activity_edit_save"));
            editingView.addSectionItem(createButton);
            createButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    saveData();
                }
            });
        }
        else
        {
            // Updating configuration handler
            AppConfigClickableCell saveButton = generateButtonView(AppConfigResourceHelper.getString(this, "app_config_action_ok_edit"));
            saveButton.setId(AppConfigResourceHelper.getIdentifier(this, "app_config_activity_edit_save"));
            editingView.addSectionItem(saveButton);
            saveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    saveData();
                }
            });

            // Restore to defaults or delete handler
            String buttonText = AppConfigResourceHelper.getString(this, AppConfigStorage.instance.isCustomConfig(getIntent().getStringExtra(ARG_CONFIG_NAME)) ? "app_config_action_delete" : "app_config_action_restore");
            AppConfigClickableCell deleteButton = generateButtonView(buttonText);
            deleteButton.setId(AppConfigResourceHelper.getIdentifier(this, "app_config_activity_edit_clear"));
            editingView.addSectionItem(deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String configName = getIntent().getStringExtra(ARG_CONFIG_NAME);
                    if (AppConfigStorage.instance.isCustomConfig(configName) || AppConfigStorage.instance.isConfigOverride(configName))
                    {
                        AppConfigStorage.instance.removeConfig(configName);
                        AppConfigStorage.instance.synchronizeCustomConfigWithPreferences(EditAppConfigActivity.this, getIntent().getStringExtra(ARG_CONFIG_NAME));
                        setResult(RESULT_OK);
                    }
                    else
                    {
                        setResult(RESULT_CANCELED);
                    }
                    finish();
                }
            });
        }
        AppConfigClickableCell cancelButton = generateButtonView(AppConfigResourceHelper.getString(this, "app_config_action_cancel"));
        cancelButton.setId(AppConfigResourceHelper.getIdentifier(this, "app_config_activity_edit_cancel"));
        editingView.addSectionItem(cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        // End section
        editingView.endSection();
    }


    // ---
    // Configuration mutations
    // ---

    private AppConfigStorageItem fetchEditedValues()
    {
        AppConfigStorageItem item = new AppConfigStorageItem();
        String name = getIntent().getStringExtra(ARG_CONFIG_NAME);
        for (View view : fieldViews)
        {
            if (view.getTag() == null)
            {
                break;
            }
            if (view.getTag().equals("name"))
            {
                if (view instanceof AppConfigEditableCell)
                {
                    name = ((AppConfigEditableCell)view).getValue();
                }
            }
            else
            {
                if (view instanceof AppConfigEditableCell)
                {
                    if (((AppConfigEditableCell)view).isNumberLimit())
                    {
                        long number = 0;
                        try
                        {
                            number = Long.parseLong(((AppConfigEditableCell)view).getValue());
                        }
                        catch (Exception ignored)
                        {
                        }
                        item.putLong((String)view.getTag(), number);
                    }
                    else
                    {
                        item.putString((String)view.getTag(), ((AppConfigEditableCell)view).getValue());
                    }
                }
                else if (view instanceof AppConfigSwitchCell)
                {
                    item.putBoolean((String)view.getTag(), ((AppConfigSwitchCell)view).isChecked());
                }
                else if (view instanceof AppConfigClickableCell)
                {
                    item.putString((String)view.getTag(), ((AppConfigClickableCell)view).getText().replace(view.getTag() + ": ", ""));
                }
            }
        }
        if (name.length() > 0)
        {
            item.putString("name", name);
        }
        return item;
    }

    private void saveData()
    {
        AppConfigStorageItem item = fetchEditedValues();
        String name = item.getString("name");
        item.removeSetting("name");
        if (name.length() > 0)
        {
            if (getIntent().getBooleanExtra(ARG_CREATE_CUSTOM, false))
            {
                AppConfigStorage.instance.putCustomConfig(name, item);
            }
            else
            {
                String oldName = getIntent().getStringExtra(ARG_CONFIG_NAME);
                boolean wasSelected = oldName.equals(AppConfigStorage.instance.getSelectedConfigName());
                if (AppConfigStorage.instance.isCustomConfig(oldName) || AppConfigStorage.instance.isConfigOverride(oldName))
                {
                    AppConfigStorage.instance.removeConfig(oldName);
                }
                AppConfigStorage.instance.putCustomConfig(name, item);
                if (wasSelected)
                {
                    AppConfigStorage.instance.selectConfig(EditAppConfigActivity.this, name);
                }
            }
            AppConfigStorage.instance.synchronizeCustomConfigWithPreferences(EditAppConfigActivity.this, name);
            setResult(RESULT_OK);
            finish();
        }
    }
}
