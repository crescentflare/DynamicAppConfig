package com.crescentflare.dynamicappconfigexample.test.model;

import com.crescentflare.dynamicappconfig.manager.AppConfigStorage;
import com.crescentflare.dynamicappconfigexample.R;
import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigLogLevel;
import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigRunType;
import com.crescentflare.dynamicappconfigexample.test.model.shared.SettingType;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test model: main app
 * Interaction related to the main example application
 */
public class MainAppModel
{
    // ---
    // Interaction
    // ---

    public ManualSetting setSettingManually(SettingType setting)
    {
        return new ManualSetting(this, setting.toString(), false);
    }

    public ManualSetting setGlobalSettingManually(SettingType setting)
    {
        return new ManualSetting(this, setting.toString(), true);
    }


    // ---
    // Checks
    // ---

    public Setting expectSetting(SettingType setting)
    {
        return new Setting(this, settingToViewId(setting), settingToPrefix(setting));
    }


    // ---
    // Helper
    // ---

    private int settingToViewId(SettingType setting)
    {
        switch (setting)
        {
            case Name:
                return R.id.activity_main_config_name;
            case ApiURL:
                return R.id.activity_main_config_api_url;
            case RunType:
                return R.id.activity_main_config_run_type;
            case AcceptAllSSL:
                return R.id.activity_main_config_accept_all_ssl;
            case NetworkTimeoutSeconds:
                return R.id.activity_main_config_network_timeout_sec;
            case ConsoleURL:
                return R.id.activity_main_config_console_url;
            case ConsoleEnabled:
                return R.id.activity_main_config_console_enabled;
            case ConsoleTimeoutSeconds:
                return R.id.activity_main_config_console_timeout_sec;
            case LogLevel:
                return R.id.activity_main_config_log_level;
        }
        return 0;
    }

    private String settingToPrefix(SettingType setting)
    {
        return setting.toString() + ": ";
    }


    // ---
    // Setting class for manually changing values
    // ---

    public static class ManualSetting
    {
        private MainAppModel model;
        private String key;
        private boolean global;

        public ManualSetting(MainAppModel model, String key, boolean global)
        {
            this.model = model;
            this.key = key;
            this.global = global;
        }

        public MainAppModel to(boolean value)
        {
            return to(value ? "true" : "false");
        }

        public MainAppModel to(int value)
        {
            return to("" + value);
        }

        public MainAppModel to(final String value)
        {
            getInstrumentation().runOnMainSync(new Runnable()
            {
                @Override
                public void run()
                {
                    if (global)
                    {
                        AppConfigStorage.instance.manuallyChangeGlobalConfig(getInstrumentation().getTargetContext(), key, value);
                    }
                    else
                    {
                        AppConfigStorage.instance.manuallyChangeCurrentConfig(getInstrumentation().getTargetContext(), key, value);
                    }
                }
            });
            return model;
        }

        public MainAppModel to(ExampleAppConfigRunType value)
        {
            return to("" + value);
        }

        public MainAppModel to(ExampleAppConfigLogLevel value)
        {
            return to("" + value);
        }
    }


    // ---
    // Setting class for checking values
    // ---

    public static class Setting
    {
        private MainAppModel model;
        private String prefix;
        private int viewId;

        public Setting(MainAppModel model, int viewId, String prefix)
        {
            this.model = model;
            this.viewId = viewId;
            this.prefix = prefix;
        }

        public MainAppModel toBe(boolean value)
        {
            onView(withId(viewId)).check(matches(withText(prefix + (value ? "true" : "false"))));
            return model;
        }

        public MainAppModel toBe(int value)
        {
            onView(withId(viewId)).check(matches(withText(prefix + value)));
            return model;
        }

        public MainAppModel toBe(String value)
        {
            onView(withId(viewId)).check(matches(withText(prefix + value)));
            return model;
        }

        public MainAppModel toBe(ExampleAppConfigRunType value)
        {
            onView(withId(viewId)).check(matches(withText(prefix + value)));
            return model;
        }

        public MainAppModel toBe(ExampleAppConfigLogLevel value)
        {
            onView(withId(viewId)).check(matches(withText(prefix + value)));
            return model;
        }
    }
}
