package com.crescentflare.dynamicappconfigexample.test.testcase;

import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.crescentflare.dynamicappconfigexample.MainActivity;
import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigRunType;
import com.crescentflare.dynamicappconfigexample.test.model.ManageAppConfigModel;
import com.crescentflare.dynamicappconfigexample.test.model.TestApplication;
import com.crescentflare.dynamicappconfigexample.test.model.shared.SettingType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Feature: I can edit an application configuration
 * As an app developer or tester
 * I want to be able to edit a specific configuration
 * So I can further customize app behavior during testing
 */
@RunWith(AndroidJUnit4.class)
public class Edit
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
     * Scenario: Edit a configuration
     * Given I am on the "App configurations" page
     * When I reset configuration data
     * And I edit the "Test server" configuration
     * Then I see the "Edit configuration" page
     * When I change "apiUrl" into string "https://changed.example.com/"
     * And I change "networkTimeoutSec" into number "10"
     * And I change "runType" into enum "runQuickly"
     * And I change "acceptAllSSL" into boolean "true"
     * And I apply the changes
     * And I select the "Test server" configuration
     * Then I see "apiUrl" set to "https://changed.example.com/"
     * And I see "networkTimeoutSec" set to "10"
     * And I see "runType" set to "runQuickly"
     * And I see "acceptAllSSL" set to "true"
     */
    @Test
    public void testEditConfiguration()
    {
        TestApplication.instance
                .expectAppConfigurationsScreen()
                .revertToConfigurationDefaults()
                .editConfig(ManageAppConfigModel.Configuration.Test)
                .expectEditConfigScreen()
                .changeSetting(SettingType.ApiURL).to("https://changed.example.com")
                .changeSetting(SettingType.NetworkTimeoutSeconds).to(10)
                .changeSetting(SettingType.RunType).to(ExampleAppConfigRunType.RunQuickly)
                .changeSetting(SettingType.AcceptAllSSL).to(true)
                .applyChanges()
                .expectAppConfigurationsScreen()
                .selectConfig(ManageAppConfigModel.Configuration.Test)
                .expectMainAppScreen()
                .expectSetting(SettingType.Name).toBe("Test server")
                .expectSetting(SettingType.ApiURL).toBe("https://changed.example.com")
                .expectSetting(SettingType.NetworkTimeoutSeconds).toBe(10)
                .expectSetting(SettingType.RunType).toBe(ExampleAppConfigRunType.RunQuickly)
                .expectSetting(SettingType.AcceptAllSSL).toBe(true);
    }
}
