package com.crescentflare.dynamicappconfigexample.test.model;

import android.os.SystemClock;

import com.crescentflare.dynamicappconfig.manager.AppConfigStorage;
import com.crescentflare.dynamicappconfigexample.R;
import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigLogLevel;
import com.crescentflare.dynamicappconfigexample.test.helper.CheckViewHelper;
import com.crescentflare.dynamicappconfigexample.test.helper.WaitViewHelper;
import com.crescentflare.dynamicappconfigexample.test.model.shared.SettingType;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.crescentflare.dynamicappconfigexample.test.helper.CheckViewHelper.withConfigTagStringMatching;
import static com.crescentflare.dynamicappconfigexample.test.helper.CheckViewHelper.withCustomPluginTagStringMatching;
import static com.crescentflare.dynamicappconfigexample.test.helper.CheckViewHelper.withStringAdapterContent;
import static com.crescentflare.dynamicappconfigexample.test.helper.CheckViewHelper.withTagStringMatching;
import static com.crescentflare.dynamicappconfigexample.test.helper.PerformViewHelper.setCellSwitch;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Test model: manage app config
 * Interaction related to the app configurations screen
 */
public class ManageAppConfigModel
{
    // ---
    // Configuration enum
    // ---

    public enum Configuration
    {
        Mock,
        Test,
        TestInsecure,
        Accept,
        Production
    }


    // ---
    // Plugin enum
    // ---

    public enum CustomPlugin
    {
        ViewLog
    }


    // ---
    // Interaction
    // ---

    public ManageAppConfigModel revertToConfigurationDefaults()
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                AppConfigStorage.instance.clearAllToDefaults(getInstrumentation().getTargetContext());
            }
        });
        return this;
    }

    public ManageAppConfigModel selectConfig(Configuration configuration)
    {
        onView(withTagValue(withConfigTagStringMatching(configurationToString(configuration)))).perform(scrollTo()).perform(click());
        return this;
    }

    public ManageAppConfigModel editConfig(Configuration configuration)
    {
        onView(withTagValue(withConfigTagStringMatching(configurationToString(configuration)))).perform(scrollTo()).perform(longClick());
        return this;
    }

    public Setting changeGlobalSetting(SettingType setting)
    {
        return new Setting(this, setting.toString());
    }

    public CustomPluginModel openCustomPlugin(CustomPlugin customPlugin)
    {
        onView(withTagValue(withCustomPluginTagStringMatching(customPluginToString(customPlugin)))).perform(scrollTo()).perform(click());
        return new CustomPluginModel();
    }


    // ---
    // Checks
    // ---

    public MainAppModel expectMainAppScreen()
    {
        CheckViewHelper.checkOnPage("Example App Config");
        return new MainAppModel();
    }

    public EditAppConfigModel expectEditConfigScreen()
    {
        onView(withId(R.id.app_config_toolbar_title)).check(matches(withText("Edit configuration")));
        WaitViewHelper.waitOptionalTextDisappear("Loading configurations...", 5000);
        return new EditAppConfigModel();
    }


    // ---
    // Helpers
    // ---

    private String configurationToString(Configuration configuration)
    {
        switch (configuration)
        {
            case Mock:
                return "Mock server";
            case Test:
                return "Test server";
            case TestInsecure:
                return "Test server (insecure)";
            case Accept:
                return "Acceptation server";
            case Production:
                return "Production";
        }
        return "";
    }

    private String customPluginToString(CustomPlugin customPlugin)
    {
        switch (customPlugin)
        {
            case ViewLog:
                return "View log";
        }
        return "";
    }


    // ---
    // Setting class for changing values
    // ---

    public static class Setting
    {
        private ManageAppConfigModel model;
        private String key;

        public Setting(ManageAppConfigModel model, String key)
        {
            this.model = model;
            this.key = key;
        }

        public ManageAppConfigModel to(boolean value)
        {
            onView(withTagValue(withTagStringMatching(key))).perform(scrollTo()).perform(setCellSwitch(value));
            SystemClock.sleep(2000);
            return model;
        }

        public ManageAppConfigModel to(int value)
        {
            onView(withTagValue(withTagStringMatching(key))).perform(scrollTo()).perform(click());
            onView(withId(R.id.app_config_dialog_input)).perform(replaceText("" + value));
            onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click());
            return model;
        }

        public ManageAppConfigModel to(String value)
        {
            onView(withTagValue(withTagStringMatching(key))).perform(scrollTo()).perform(click());
            onView(withId(R.id.app_config_dialog_input)).perform(replaceText(value));
            onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click());
            return model;
        }

        public ManageAppConfigModel to(ExampleAppConfigLogLevel value)
        {
            onView(withTagValue(withTagStringMatching(key))).perform(scrollTo()).perform(click());
            onData(allOf(is(instanceOf(String.class)), withStringAdapterContent(value.toString()))).perform(click());
            return model;
        }
    }
}
