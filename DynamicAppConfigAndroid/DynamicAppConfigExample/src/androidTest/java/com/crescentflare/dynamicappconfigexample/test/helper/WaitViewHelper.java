package com.crescentflare.dynamicappconfigexample.test.helper;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static org.hamcrest.Matchers.isA;

/**
 * Test helper: wait for view
 * Utilities to easily wait for a view to reach a certain state
 */
public class WaitViewHelper {

    // --
    // Utility functions
    // --

    public static void waitOptionalTextDisappear(String text, int timeout) {
        onView(isRoot()).perform(waitForOptionalTextToDisappear(text, timeout));
    }


    // --
    // View action: wait for a view with the given text to disappear (or skip if the text isn't there at all)
    // --

    private static ViewAction waitForOptionalTextToDisappear(final String text, final int timeout) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isA(View.class);
            }

            @Override
            public String getDescription() {
                return "Wait for view with text " + text + " to disappear within " + timeout + "ms";
            }

            public boolean viewHasText(View view, String text) {
                if (view instanceof TextView) {
                    return ((TextView)view).getText().equals(text);
                }
                return false;
            }

            public View findViewByText(View view, String text) {
                if (!(view instanceof ViewGroup)) {
                    return null;
                }
                ViewGroup group = (ViewGroup)view;
                int childCount = group.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = group.getChildAt(i);
                    if (child.isShown()) {
                        if (viewHasText(child, text)) {
                            return child;
                        }
                        if (child instanceof ViewGroup) {
                            View foundMatch = findViewByText(child, text);
                            if (foundMatch != null) {
                                return foundMatch;
                            }
                        }
                    }
                }
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                final long endTime = System.currentTimeMillis() + timeout;
                View foundView = null;
                do {
                    if (foundView == null || !viewHasText(foundView, text) || !foundView.isShown()) {
                        foundView = findViewByText(view, text);
                    }
                    if (foundView == null) {
                        return;
                    }
                    uiController.loopMainThreadForAtLeast(100);
                }
                while (System.currentTimeMillis() < endTime);

                //Not found -> timeout
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(view.toString())
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }
}
