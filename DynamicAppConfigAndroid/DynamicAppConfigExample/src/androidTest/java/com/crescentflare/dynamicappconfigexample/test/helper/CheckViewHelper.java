package com.crescentflare.dynamicappconfigexample.test.helper;

import androidx.test.espresso.matcher.BoundedMatcher;

import com.crescentflare.dynamicappconfigexample.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Test helper: check view
 * Utilities to easily check for view content
 */
public class CheckViewHelper {

    // --
    // Convenience functions
    // --

    public static void checkOnPage(String pageTitle) {
        onView(allOf(isDescendantOfA(withId(R.id.action_bar_container)), withText(pageTitle))).check(matches(isDisplayed()));
    }


    // --
    // Custom matcher for checking tags
    // --

    public static Matcher<Object> withConfigTagStringMatching(String expectedText) {
        checkNotNull(expectedText);
        return withTagStringMatching(equalTo("config: " + expectedText));
    }

    public static Matcher<Object> withCustomPluginTagStringMatching(String expectedText) {
        checkNotNull(expectedText);
        return withTagStringMatching(equalTo("plugin: " + expectedText));
    }

    public static Matcher<Object> withTagStringMatching(String expectedText) {
        checkNotNull(expectedText);
        return withTagStringMatching(equalTo(expectedText));
    }

    @SuppressWarnings("rawtypes")
    private static Matcher<Object> withTagStringMatching(final Matcher<String> itemTextMatcher) {
        checkNotNull(itemTextMatcher);
        return new BoundedMatcher<Object, String>(String.class) {
            @Override
            public boolean matchesSafely(String string) {
                return itemTextMatcher.matches(string);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with string: ");
                itemTextMatcher.describeTo(description);
            }
        };
    }


    // --
    // Custom matcher for a list view containing a string adapter
    // --

    public static Matcher<Object> withStringAdapterContent(String expectedText) {
        checkNotNull(expectedText);
        return withStringAdapterContent(equalTo(expectedText));
    }

    private static Matcher<Object> withStringAdapterContent(final Matcher<String> itemTextMatcher) {
        checkNotNull(itemTextMatcher);
        return new BoundedMatcher<Object, String>(String.class) {
            @Override
            public boolean matchesSafely(String entry) {
                return itemTextMatcher.matches(entry);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with item text: ");
                itemTextMatcher.describeTo(description);
            }
        };
    }
}
