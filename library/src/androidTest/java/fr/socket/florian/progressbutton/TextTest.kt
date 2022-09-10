package fr.socket.florian.progressbutton

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import fr.socket.florian.progressbutton.utils.ButtonActivityTestRule
import fr.socket.florian.progressbutton.utils.pressAndHold
import fr.socket.florian.progressbutton.utils.release
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TextTest : BaseTest() {
    @Rule
    @JvmField
    val activityRule = ButtonActivityTestRule()

    @Test
    fun textAsResTest() {
        val activity = activityRule.launchActivity(R.layout.text_res)
        onView(withText(activity.getString(R.string.button_text)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun textAsStringTest() {
        activityRule.launchActivity(R.layout.text_string)
        onView(withText("my text"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun allCapsTest() {
        val activity = activityRule.launchActivity(R.layout.all_caps)
        onView(withText(activity.getString(R.string.button_text).toUpperCase()))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun textAppearanceTest() {
        val activity = activityRule.launchActivity(R.layout.text_appearance)
        onView(withText(activity.getString(R.string.button_text))).check(matches(object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View?): Boolean {
                if (item == null || item !is TextView) return false
                return item.textSize == (activity.resources.getDimension(R.dimen.abc_text_size_title_material) + 0.5).toFloat()
            }

            override fun describeTo(description: Description?) {}
        }))
    }

    @Test
    fun textColorColorTest() {
        activityRule.launchActivity(R.layout.text_color_hardcoded)
        checkTextColorIsWhite()
    }

    @Test
    fun textColorStateListTest() {
        activityRule.launchActivity(R.layout.text_color_statelist)
        checkTextColorIsWhite()
    }

    @Test
    fun textColorResTest() {
        activityRule.launchActivity(R.layout.text_color_res)
        checkTextColorIsWhite()
    }

    @Test
    fun checkTextVisibleOnPressed() {
        activityRule.launchActivity(R.layout.text_res)
        onView(withText(getInstrumentation().targetContext.getString(R.string.button_text)))
            .perform(pressAndHold())
        Thread.sleep(10000)
        onView(withText(getInstrumentation().targetContext.getString(R.string.button_text)))
            .check(matches(isDisplayed()))
        onView(withText(getInstrumentation().targetContext.getString(R.string.button_text)))
            .perform(release())
    }

    private fun checkTextColorIsWhite() {
        onView(withText(getInstrumentation().targetContext.getString(R.string.button_text))).check(matches(object :
            TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View?): Boolean {
                if (item == null || item !is TextView) return false
                return item.currentTextColor == Color.WHITE
            }

            override fun describeTo(description: Description?) {}
        }))
    }
}