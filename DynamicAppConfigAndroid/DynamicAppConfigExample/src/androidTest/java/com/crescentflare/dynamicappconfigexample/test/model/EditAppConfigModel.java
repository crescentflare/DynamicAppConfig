package com.crescentflare.dynamicappconfigexample.test.model;

import android.os.SystemClock;

import com.crescentflare.dynamicappconfigexample.R;
import com.crescentflare.dynamicappconfigexample.appconfig.ExampleAppConfigRunType;
import com.crescentflare.dynamicappconfigexample.test.helper.CheckViewHelper;
import com.crescentflare.dynamicappconfigexample.test.helper.WaitViewHelper;
import com.crescentflare.dynamicappconfigexample.test.model.shared.SettingType;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.crescentflare.dynamicappconfigexample.test.helper.CheckViewHelper.withStringAdapterContent;
import static com.crescentflare.dynamicappconfigexample.test.helper.CheckViewHelper.withTagStringMatching;
import static com.crescentflare.dynamicappconfigexample.test.helper.PerformViewHelper.setCellSwitch;
import static com.crescentflare.dynamicappconfigexample.test.helper.PerformViewHelper.setCellText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Test model: edit app config
 * Interaction related to the edit configuration and new custom configuration screens
 */
public class EditAppConfigModel
{
    // ---
    // Interaction
    // ---

    public Setting changeSetting(SettingType setting)
    {
        return new Setting(this, setting.toString());
    }

    public EditAppConfigModel applyChanges()
    {
        onView(withId(R.id.app_config_activity_edit_save)).perform(scrollTo()).perform(click());
        return this;
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


    // ---
    // Setting class for changing values
    // ---

    public static class Setting
    {
        private EditAppConfigModel model;
        private String key;

        public Setting(EditAppConfigModel model, String key)
        {
            this.model = model;
            this.key = key;
        }

        public EditAppConfigModel to(boolean value)
        {
            onView(withTagValue(withTagStringMatching(key))).perform(scrollTo()).perform(setCellSwitch(value));
            SystemClock.sleep(2000);
            return model;
        }

        public EditAppConfigModel to(int value)
        {
            onView(withTagValue(withTagStringMatching(key))).perform(scrollTo()).perform(setCellText("" + value));
            return model;
        }

        public EditAppConfigModel to(String value)
        {
            onView(withTagValue(withTagStringMatching(key))).perform(scrollTo()).perform(setCellText(value));
            return model;
        }

        public EditAppConfigModel to(ExampleAppConfigRunType value)
        {
            onView(withTagValue(withTagStringMatching(key))).perform(scrollTo()).perform(click());
            onData(allOf(is(instanceOf(String.class)), withStringAdapterContent(value.toString()))).perform(click());
            return model;
        }
    }
}
