package com.crescentflare.dynamicappconfigexample.test.testcase;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.crescentflare.dynamicappconfigexample.MainActivity;
import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigLogLevel;
import com.crescentflare.dynamicappconfigexample.test.model.ManageAppConfigModel;
import com.crescentflare.dynamicappconfigexample.test.model.TestApplication;
import com.crescentflare.dynamicappconfigexample.test.model.shared.SettingType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Feature: I can manually change the active configuration or a global setting
 * As an app developer or tester
 * I want to be able to manually change the active configuration or a global setting
 * So I can optimize testing and make smaller test scripts
 */
@RunWith(AndroidJUnit4.class)
public class Manual
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
     * Scenario: Manually change a configuration
     * Given I am on the "App configurations" page
     * When I reset configuration data
     * And I select the "Test server" configuration
     * And I manually change "apiUrl" into "https://manualchange.example.com/"
     * Then I see "apiUrl" set to "https://manualchange.example.com/"
     */
    @Test
    public void testManuallyChangeConfiguration()
    {
        TestApplication.instance
                .expectAppConfigurationsScreen()
                .revertToConfigurationDefaults()
                .selectConfig(ManageAppConfigModel.Configuration.Test)
                .expectMainAppScreen()
                .setSettingManually(SettingType.ApiURL).to("https://manualchange.example.com/")
                .expectSetting(SettingType.ApiURL).toBe("https://manualchange.example.com/");
    }

    /**
     * Scenario: Manually change a global setting
     * Given I am on the "App configurations" page
     * When I reset configuration data
     * And I select the "Test server" configuration
     * And I manually change "logLevel" into "logVerbose"
     * Then I see "logLevel" set to "logVerbose"
     */
    @Test
    public void testManuallyChangeGlobalSetting()
    {
        TestApplication.instance
                .expectAppConfigurationsScreen()
                .revertToConfigurationDefaults()
                .selectConfig(ManageAppConfigModel.Configuration.Test)
                .expectMainAppScreen()
                .setGlobalSettingManually(SettingType.LogLevel).to(ExampleAppConfigLogLevel.LogVerbose)
                .expectSetting(SettingType.LogLevel).toBe(ExampleAppConfigLogLevel.LogVerbose);
    }
}
