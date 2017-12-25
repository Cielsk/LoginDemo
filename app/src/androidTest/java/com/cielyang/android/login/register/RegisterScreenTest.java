package com.cielyang.android.login.register;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.view.View;

import com.cielyang.android.login.R;
import com.cielyang.android.login.login.LoginActivity;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterScreenTest {

    private static final String USERNAME = "johnsmith";
    private static final String EMAIL = "johnsmith@test.com";
    private static final String PASSWORD = "qwerty";

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.link_register)).perform(click());
    }

    @Test
    public void inputCorrectFields() {
        onView(withId(R.id.edit_text_username)).perform(clearText());
        onView(withId(R.id.edit_text_username)).perform(typeText(USERNAME), closeSoftKeyboard());

        onView(withId(R.id.edit_text_email)).perform(clearText());
        onView(withId(R.id.edit_text_email)).perform(typeText(EMAIL), closeSoftKeyboard());

        onView(withId(R.id.edit_text_password)).perform(clearText());
        onView(withId(R.id.edit_text_password)).perform(typeText(PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.text_input_layout_username)).check(matches(hasNoErrorText()));
        onView(withId(R.id.text_input_layout_email)).check(matches(hasNoErrorText()));
        onView(withId(R.id.text_input_layout_password)).check(matches(hasNoErrorText()));
        onView(withId(R.id.text_input_layout_password)).check(
                matches(showPasswordVisibilityDrawable()));
    }

    private Matcher<? super View> hasNoErrorText() {
        return new CustomTypeSafeMatcher<View>("empty error of TextInputLayout") {
            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                return TextUtils.isEmpty(error);
            }
        };
    }

    private Matcher<? super View> showPasswordVisibilityDrawable() {
        return new CustomTypeSafeMatcher<View>("show password toggle drawable or not") {
            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                if (!((TextInputLayout) view).isPasswordVisibilityToggleEnabled()) {
                    return false;
                }

                Drawable drawable = ((TextInputLayout) view).getPasswordVisibilityToggleDrawable();
                return !(drawable == null || !drawable.isVisible());
            }
        };
    }

    @Test
    public void inputInvalidUsername() throws Exception {
        onView(withId(R.id.edit_text_username)).perform(clearText());
        onView(withId(R.id.edit_text_username)).perform(typeText("1 2 3"), closeSoftKeyboard());

        onView(withId(R.id.text_input_layout_username))
                .check(matches(hasTextLayoutErrorText(R.string.error_invalid_username)));
        onView(withId(R.id.edit_text_username)).perform(clearText());
    }

    @Test
    public void inputInvalidEmail() throws Exception {
        onView(withId(R.id.edit_text_email)).perform(clearText());
        onView(withId(R.id.edit_text_email)).perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.text_input_layout_email))
                .check(matches(hasTextLayoutErrorText(R.string.error_invalid_email)));
        onView(withId(R.id.edit_text_email)).perform(clearText());
    }

    @Test
    public void inputShortPassword() throws Exception {
        onView(withId(R.id.edit_text_password)).perform(clearText());
        onView(withId(R.id.edit_text_password)).perform(typeText("abc"), closeSoftKeyboard());

        onView(withId(R.id.text_input_layout_password))
                .check(matches(hasTextLayoutErrorText(R.string.error_short_password)));
        onView(withId(R.id.edit_text_password)).perform(clearText());
    }

    @Test
    public void inputInvalidPassword() throws Exception {
        onView(withId(R.id.edit_text_password)).perform(clearText());
        onView(withId(R.id.edit_text_password)).perform(typeText("1234567"), closeSoftKeyboard());

        onView(withId(R.id.text_input_layout_password))
                .check(matches(hasTextLayoutErrorText(R.string.error_invalid_password)));
        onView(withId(R.id.edit_text_password)).perform(clearText());
    }

    private Matcher<? super View> hasTextLayoutErrorText(@StringRes int resId) {
        return new CustomTypeSafeMatcher<View>("empty error of TextInputLayout") {
            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();
                String str = view.getContext().getString(resId);

                return error != null && str.equals(error);
            }
        };
    }

    @Test
    public void noInputs() throws Exception {
        onView(withId(R.id.edit_text_username)).perform(clearText());
        onView(withId(R.id.edit_text_email)).perform(clearText());
        onView(withId(R.id.edit_text_password)).perform(clearText());
        onView(withId(R.id.btn_register)).perform(click());

        onView(withId(R.id.text_input_layout_username))
                .check(matches(hasTextLayoutErrorText(R.string.error_field_required)));
        onView(withId(R.id.text_input_layout_email))
                .check(matches(hasTextLayoutErrorText(R.string.error_field_required)));
        onView(withId(R.id.text_input_layout_password))
                .check(matches(hasTextLayoutErrorText(R.string.error_field_required)));
    }

    @Test
    public void clickLoginLink() throws Exception {
        onView(withId(R.id.link_login)).perform(click());

        onView(withId(R.id.btn_login)).check(matches(isDisplayed()));
    }
}
