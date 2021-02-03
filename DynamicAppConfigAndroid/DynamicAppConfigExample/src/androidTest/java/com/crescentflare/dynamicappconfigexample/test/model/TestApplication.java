package com.crescentflare.dynamicappconfigexample.test.model;

import com.crescentflare.dynamicappconfigexample.R;
import com.crescentflare.dynamicappconfigexample.test.helper.WaitViewHelper;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test model: application
 * Used as the main application model to reach into other flows
 */
public class TestApplication
{
    // ---
    // Singleton
    // ---

    public static TestApplication instance = new TestApplication();

    private TestApplication()
    {
    }


    // ---
    // Checks
    // ---

    public ManageAppConfigModel expectAppConfigurationsScreen()
    {
        onView(withId(R.id.app_config_toolbar_title)).check(matches(withText("App configurations")));
        WaitViewHelper.waitOptionalTextDisappear("Loading configurations...", 5000);
        return new ManageAppConfigModel();
    }
}
