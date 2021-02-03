package com.crescentflare.dynamicappconfigexample.test.testcase;

import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.crescentflare.dynamicappconfigexample.MainActivity;
import com.crescentflare.dynamicappconfigexample.test.model.ManageAppConfigModel;
import com.crescentflare.dynamicappconfigexample.test.model.TestApplication;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Feature: I can use a custom plugin
 * As an app developer
 * I want to be able to add custom plugins in the selection menu
 * So I can integrate my own tools within the app config library easily
 */
@RunWith(AndroidJUnit4.class)
public class Plugin
{
    // ---
    // Members
    // ---

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    // ---
    // Scenarios
    // ---

    /**
     * Scenario: Using a custom plugin
     * Given I am on the "App configurations" page
     * When I select the "View log" custom plugin
     * Then I see the "Show log" screen
     */
    @Test
    public void testViewLog()
    {
        TestApplication.instance
                .expectAppConfigurationsScreen()
                .revertToConfigurationDefaults()
                .openCustomPlugin(ManageAppConfigModel.CustomPlugin.ViewLog)
                .expectShowLogScreen();
    }
}
