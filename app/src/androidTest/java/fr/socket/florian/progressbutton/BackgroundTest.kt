package fr.socket.florian.progressbutton

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import fr.socket.florian.progressbutton.utils.ButtonActivityTestRule
import fr.socket.florian.progressbutton.utils.clickOnButton
import fr.socket.florian.progressbutton.utils.pressAndHold
import fr.socket.florian.progressbutton.utils.withBackground
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
@LargeTest
class BackgroundTest : BaseTest() {
    @Rule
    @JvmField
    val activityRule = ButtonActivityTestRule()

    lateinit var activity: ButtonActivity

    @Before
    fun createActivity() {
        activity = activityRule.launchActivity(R.layout.background)
    }

    @Test
    fun normalBackgroundTest() {
        Espresso.onView(ViewMatchers.withResourceName("button"))
            .check(ViewAssertions.matches(withBackground(activity.getDrawable(R.drawable.normal_background)!!)))
    }

    @Test
    fun pressedBackgroundTest() {
        Espresso.onView(ViewMatchers.withId(R.id.progress_button)).perform(pressAndHold())
        Espresso.onView(ViewMatchers.withResourceName("button"))
            .check(ViewAssertions.matches(withBackground(activity.getDrawable(R.drawable.pressed_background)!!)))
    }

    @Test
    fun successBackgroundTest() {
        clickOnButton()
        val countDown = CountDownLatch(1)
        activity.button.success {
            countDown.countDown()
        }
        countDown.await(10, TimeUnit.SECONDS)
        Espresso.onView(ViewMatchers.withResourceName("button"))
            .check(ViewAssertions.matches(withBackground(activity.getDrawable(R.drawable.success_background)!!)))
    }

    @Test
    fun errorBackgroundTest() {
        clickOnButton()
        val countDown = CountDownLatch(1)
        activity.button.error {
            countDown.countDown()
        }
        countDown.await(10, TimeUnit.SECONDS)
        Espresso.onView(ViewMatchers.withResourceName("button"))
            .check(ViewAssertions.matches(withBackground(activity.getDrawable(R.drawable.error_background)!!)))
    }
}