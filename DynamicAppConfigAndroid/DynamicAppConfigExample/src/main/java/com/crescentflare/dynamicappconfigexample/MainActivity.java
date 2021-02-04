package com.crescentflare.dynamicappconfigexample;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.activity.ManageAppConfigActivity;
import com.crescentflare.dynamicappconfig.manager.AppConfigStorage;
import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigManager;
import com.crescentflare.dynamicappconfigexample.utility.Logger;

/**
 * The example activity shows a simple screen with a message
 */
public class MainActivity extends AppCompatActivity implements AppConfigStorage.ChangedConfigListener
{
    // ---
    // Constants
    // ---

    private static final int RESULT_CODE_MANAGE_APP_CONFIG = 1001;


    // ---
    // Initialization
    // ---

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillContent();
        if (AppConfigStorage.instance.isInitialized() && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK) > 0)
        {
            ManageAppConfigActivity.startWithResult(this, RESULT_CODE_MANAGE_APP_CONFIG);
        }
    }


    // ---
    // Activity state handling
    // ---

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        fillContent();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        AppConfigStorage.instance.removeChangedConfigListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        AppConfigStorage.instance.addChangedConfigListener(this);
        fillContent();
    }


    // ---
    // Fill content (show configuration values)
    // ---

    @Override
    public void onChangedConfig()
    {
        Logger.log("Configuration changed");
        fillContent();
    }

    public void fillContent()
    {
        // Log config
        Logger.logVerbose("apiUrl set to: " + ExampleAppConfigManager.currentConfig().getApiUrl());
        Logger.logVerbose("runType set to: " + ExampleAppConfigManager.currentConfig().getRunType().toString());
        Logger.logVerbose("acceptAllSsl set to: " + (ExampleAppConfigManager.currentConfig().isAcceptAllSSL() ? "true" : "false"));
        Logger.logVerbose("networkTimeout set to: " + ExampleAppConfigManager.currentConfig().getNetworkTimeoutSec());

        // Fetch text views
        TextView tvConfigName = (TextView)findViewById(R.id.activity_main_config_name);
        TextView tvConfigApiUrl = (TextView)findViewById(R.id.activity_main_config_api_url);
        TextView tvConfigRunType = (TextView)findViewById(R.id.activity_main_config_run_type);
        TextView tvConfigAcceptAllSSL = (TextView)findViewById(R.id.activity_main_config_accept_all_ssl);
        TextView tvConfigNetworkTimeout = (TextView)findViewById(R.id.activity_main_config_network_timeout_sec);
        TextView tvConfigConsoleUrl = (TextView)findViewById(R.id.activity_main_config_console_url);
        TextView tvConfigConsoleEnabled = (TextView)findViewById(R.id.activity_main_config_console_enabled);
        TextView tvConfigConsoleTimeout = (TextView)findViewById(R.id.activity_main_config_console_timeout_sec);
        TextView tvConfigLogLevel = (TextView)findViewById(R.id.activity_main_config_log_level);

        // Fill with config settings
        tvConfigName.setText(getString(R.string.prefix_config_name) + " " + ExampleAppConfigManager.currentConfig().getName());
        tvConfigApiUrl.setText(getString(R.string.prefix_config_api_url) + " " + ExampleAppConfigManager.currentConfig().getApiUrl());
        tvConfigRunType.setText(getString(R.string.prefix_config_run_type) + " " + ExampleAppConfigManager.currentConfig().getRunType().toString());
        tvConfigAcceptAllSSL.setText(getString(R.string.prefix_config_accept_all_ssl) + " " + (ExampleAppConfigManager.currentConfig().isAcceptAllSSL() ? "true" : "false"));
        tvConfigNetworkTimeout.setText(getString(R.string.prefix_config_network_timeout_sec) + " " + ExampleAppConfigManager.currentConfig().getNetworkTimeoutSec());
        tvConfigConsoleUrl.setText(getString(R.string.prefix_config_console_url) + " " + ExampleAppConfigManager.currentConfig().getConsoleUrl());
        tvConfigConsoleEnabled.setText(getString(R.string.prefix_config_console_enabled) + " " + (ExampleAppConfigManager.currentConfig().isConsoleEnabled() ? "true" : "false"));
        tvConfigConsoleTimeout.setText(getString(R.string.prefix_config_console_timeout_sec) + " " + ExampleAppConfigManager.currentConfig().getConsoleTimeoutSec());
        tvConfigLogLevel.setText(getString(R.string.prefix_config_log_level) + " " + ExampleAppConfigManager.currentConfig().getLogLevel().toString());

        // Set long click listener on the action bar to show the selection menu again
        View actionBar = findActionBar();
        if (actionBar != null)
        {
            actionBar.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    ManageAppConfigActivity.startWithResult(MainActivity.this, RESULT_CODE_MANAGE_APP_CONFIG);
                    return true;
                }
            });
        }
    }


    // ---
    // Helpers
    // ---

    public ViewGroup findActionBar()
    {
        int id = getResources().getIdentifier("action_bar", "id", "android");
        ViewGroup actionBar = null;
        if (id != 0)
        {
            actionBar = (ViewGroup)findViewById(id);
        }
        if (actionBar == null)
        {
            actionBar = findToolbar((ViewGroup)findViewById(android.R.id.content).getRootView());
        }
        return actionBar;
    }

    private ViewGroup findToolbar(ViewGroup viewGroup)
    {
        ViewGroup toolbar = null;
        for (int i = 0, len = viewGroup.getChildCount(); i < len; i++)
        {
            View view = viewGroup.getChildAt(i);
            if (view.getClass().getName().equals("android.support.v7.widget.Toolbar") || view.getClass().getName().equals("androidx.appcompat.widget.Toolbar") || view.getClass().getName().equals("android.widget.Toolbar"))
            {
                toolbar = (ViewGroup)view;
            }
            else if (view instanceof ViewGroup)
            {
                toolbar = findToolbar((ViewGroup)view);
            }
            if (toolbar != null)
            {
                break;
            }
        }
        return toolbar;
    }
}
