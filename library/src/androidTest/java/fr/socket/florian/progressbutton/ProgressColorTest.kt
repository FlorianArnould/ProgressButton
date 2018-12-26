package fr.socket.florian.progressbutton

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.ProgressBar
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import fr.socket.florian.progressbutton.utils.ButtonActivityTestRule
import fr.socket.florian.progressbutton.utils.clickOnButton
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ProgressColorTest : BaseTest() {
    @Rule
    @JvmField
    val activityRule = ButtonActivityTestRule()

    @Test
    fun progressColorHardcodedTest() {
        val activity = activityRule.launchActivity(R.layout.progress_color_hardcoded)
        clickOnButton()
        progressColorGreenCheck(activity)
    }

    @Test
    fun progressColorResTest() {
        val activity = activityRule.launchActivity(R.layout.progress_color_hardcoded)
        clickOnButton()
        progressColorGreenCheck(activity)
    }

    private fun progressColorGreenCheck(context: Context) {
        Espresso.onView(ViewMatchers.withResourceName("buttonProgress"))
            .check(ViewAssertions.matches(object : TypeSafeMatcher<View>() {
                override fun matchesSafely(item: View?): Boolean {
                    if (item == null || item !is ProgressBar) return false
                    return item.indeterminateDrawable.current.colorFilter == PorterDuffColorFilter(
                        context.getColor(R.color.greenTest),
                        PorterDuff.Mode.SRC_IN
                    )
                }

                override fun describeTo(description: Description?) {}
            }))
    }
}