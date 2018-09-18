package com.crescentflare.dynamicappconfigexample.test.testcase;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.crescentflare.dynamicappconfigexample.MainActivity;
import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigLogLevel;
import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigRunType;
import com.crescentflare.dynamicappconfigexample.test.model.ManageAppConfigModel;
import com.crescentflare.dynamicappconfigexample.test.model.TestApplication;
import com.crescentflare.dynamicappconfigexample.test.model.shared.SettingType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Feature: I can change my application configuration or a global setting
 * As an app developer or tester
 * I want to be able to change the configuration or a global setting
 * So I can test multiple configurations and settings in one build
 */
@RunWith(AndroidJUnit4.class)
public class Main
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
     * Scenario: Selecting a configuration
     * Given I am on the "App configurations" page
     * When I select the "Test server" configuration
     * Then I see the "Test server" settings
     */
    @Test
    public void testSelectConfiguration()
    {
        TestApplication.instance
                .expectAppConfigurationsScreen()
                .revertToConfigurationDefaults()
                .selectConfig(ManageAppConfigModel.Configuration.Test)
                .expectMainAppScreen()
                .expectSetting(SettingType.Name).toBe("Test server")
                .expectSetting(SettingType.ApiURL).toBe("https://test.example.com/")
                .expectSetting(SettingType.RunType).toBe(ExampleAppConfigRunType.RunNormally)
                .expectSetting(SettingType.AcceptAllSSL).toBe(false)
                .expectSetting(SettingType.NetworkTimeoutSeconds).toBe(20);
    }

    /**
     * Scenario: Editing global settings
     * Given I am on the "App configurations" page
     * When I reset configuration data
     * And I change "consoleUrl" into string "https://console.example.com/"
     * And I change "consoleTimeoutSec" into number "100"
     * And I change "logLevel" into enum "logVerbose"
     * And I change "consoleEnabled" into boolean "true"
     * And I select the "Mock server" configuration
     * Then I see "consoleUrl" set to "https://console.example.com/"
     * And I see "consoleTimeoutSec" set to "100"
     * And I see "logLevel" set to "logVerbose"
     * And I see "consoleEnabled" set to "true"
     */
    @Test
    public void testEditGlobalSettings()
    {
        TestApplication.instance
                .expectAppConfigurationsScreen()
                .revertToConfigurationDefaults()
                .changeGlobalSetting(SettingType.ConsoleURL).to("https://console.example.com")
                .changeGlobalSetting(SettingType.ConsoleTimeoutSeconds).to(100)
                .changeGlobalSetting(SettingType.LogLevel).to(ExampleAppConfigLogLevel.LogVerbose)
                .changeGlobalSetting(SettingType.ConsoleEnabled).to(true)
                .selectConfig(ManageAppConfigModel.Configuration.Mock)
                .expectMainAppScreen()
                .expectSetting(SettingType.ConsoleURL).toBe("https://console.example.com")
                .expectSetting(SettingType.ConsoleTimeoutSeconds).toBe(100)
                .expectSetting(SettingType.LogLevel).toBe(ExampleAppConfigLogLevel.LogVerbose)
                .expectSetting(SettingType.ConsoleEnabled).toBe(true);
    }
}
