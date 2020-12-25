package com.crescentflare.dynamicappconfigexample.appconfig.plugins;


import android.app.Activity;
import android.content.Intent;

import com.crescentflare.dynamicappconfig.plugin.AppConfigPlugin;
import com.crescentflare.dynamicappconfigexample.utility.Logger;
import com.crescentflare.dynamicappconfigexample.utility.ShowLogActivity;

/**
 * App config: custom plugin for logging
 * This plugin is used to display the log when clicking on it from the app config menu
 */
public class ExampleAppConfigLogPlugin implements AppConfigPlugin
{
    @Override
    public String displayName()
    {
        return "View log";
    }

    @Override
    public String displayValue()
    {
        String logString = Logger.logString();
        if (logString.length() == 0) {
            return "0 lines";
        }
        return "" + logString.split("\\n").length + " lines";
    }

    @Override
    public boolean canInteract()
    {
        return true;
    }

    @Override
    public void interact(Activity fromActivity)
    {
        fromActivity.startActivity(new Intent(fromActivity, ShowLogActivity.class));
    }
}
