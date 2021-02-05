package com.crescentflare.dynamicappconfigexample.appconfig.plugins;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.crescentflare.dynamicappconfig.plugin.AppConfigPlugin;
import com.crescentflare.dynamicappconfigexample.R;
import com.crescentflare.dynamicappconfigexample.utility.Logger;
import com.crescentflare.dynamicappconfigexample.utility.ShowLogActivity;

/**
 * App config: custom plugin for logging
 * This plugin is used to display the log when clicking on it from the app config menu
 */
public class ExampleAppConfigLogPlugin implements AppConfigPlugin {

    @Override
    public String displayName() {
        return "View log";
    }

    @Override
    public String displayValue() {
        String logString = Logger.logString();
        if (logString.length() == 0) {
            return "0 lines";
        }
        return "" + logString.split("\\n").length + " lines";
    }

    @Override
    public boolean canInteract() {
        return true;
    }

    @Override
    public boolean canEdit() {
        return true;
    }

    @Override
    public void interact(Activity fromActivity) {
        fromActivity.startActivity(new Intent(fromActivity, ShowLogActivity.class));
    }

    @Override
    public void edit(final Activity fromActivity) {
        final AlertDialog dialog = new AlertDialog.Builder(fromActivity)
                .setTitle(fromActivity.getString(R.string.clear_log_title))
                .setPositiveButton(fromActivity.getString(R.string.clear_log_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logger.clear();
                        fromActivity.startActivity(new Intent(fromActivity, ShowLogActivity.class));
                    }
                })
                .setNegativeButton(fromActivity.getString(R.string.clear_log_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No implementation
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // No implementation
                    }
                })
                .create();
        dialog.show();
    }
}
