package fr.socket.florian.progressbutton.test

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import fr.socket.florian.progressbutton.R
import fr.socket.florian.progressbutton.test.utils.ButtonActivityTestRule
import fr.socket.florian.progressbutton.test.utils.clickOnButton
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@LargeTest
class RevertTest : BaseTest() {
    @Rule
    @JvmField
    val activityRule = ButtonActivityTestRule()

    lateinit var activity: ButtonActivity

    @Before
    fun createActivity() {
        activity = activityRule.launchActivity(R.layout.always_revert)
    }

    @Test
    fun revertOnSuccessAttributeTest() {
        clickOnButton()
        val countDown = CountDownLatch(1)
        activity.button.success {
            countDown.countDown()
        }
        countDown.await(10, TimeUnit.SECONDS)
        checkAlpha1()
    }

    @Test
    fun revertOnErrorAttributeTest() {
        clickOnButton()
        val countDown = CountDownLatch(1)
        activity.button.error {
            countDown.countDown()
        }
        countDown.await(10, TimeUnit.SECONDS)
        checkAlpha1()
    }

    @Test
    fun revertDelayAttributeTest() {
        Assert.assertEquals(0, activity.button.revertDelay)
    }

    private fun checkAlpha1() {
        Espresso.onView(ViewMatchers.withText(activity.getString(R.string.button_text)))
            .check(ViewAssertions.matches(object : TypeSafeMatcher<View>() {
                override fun matchesSafely(item: View?): Boolean {
                    if (item == null || item !is TextView) return false
                    return item.alpha == 1f
                }

                override fun describeTo(description: Description?) {}
            }))
    }
}