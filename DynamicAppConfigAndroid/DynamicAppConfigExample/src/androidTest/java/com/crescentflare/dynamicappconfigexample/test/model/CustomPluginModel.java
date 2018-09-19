package com.crescentflare.dynamicappconfigexample.test.model;

import com.crescentflare.dynamicappconfigexample.test.helper.CheckViewHelper;

/**
 * Test model: custom plugin
 * Check for a custom plugin screen
 */
public class CustomPluginModel
{
    // ---
    // Checks
    // ---

    @SuppressWarnings("UnusedReturnValue")
    public CustomPluginModel expectShowLogScreen()
    {
        CheckViewHelper.checkOnPage("Example App Config log");
        return this;
    }
}
