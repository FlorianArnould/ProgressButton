package fr.socket.florian.progressbutton

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import fr.socket.florian.progressbutton.utils.ButtonActivityTestRule
import fr.socket.florian.progressbutton.utils.clickOnButton
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@LargeTest
class LifecycleTest : BaseTest() {
    @Rule
    @JvmField
    val activityRule = ButtonActivityTestRule()

    lateinit var activity: ButtonActivity

    @Before
    fun setContentView() {
        activity = activityRule.launchActivity(R.layout.text_res)
    }

    @Test
    fun onClickTest() {
        val listener = mock(View.OnClickListener::class.java)
        activity.button.setOnClickListener(listener)
        clickOnButton()
        verify(listener, times(1))
            .onClick(ArgumentMatchers.any())
    }

    @Test
    fun disableButtonDuringProgress() {
        val listener = mock(View.OnClickListener::class.java)
        activity.button.setOnClickListener(listener)
        clickOnButton()
        clickOnButton()
        verify(listener, times(1))
            .onClick(ArgumentMatchers.any())
    }

    @Test
    fun onSuccessTest() {
        clickOnButton()
        val countDown = CountDownLatch(1)
        activity.runOnUiThread {
            activity.button.success {
                countDown.countDown()
            }
        }
        countDown.await(10, TimeUnit.SECONDS)
        Assert.assertEquals(0, countDown.count)
    }

    @Test
    fun onErrorTest() {
        clickOnButton()
        val countDown = CountDownLatch(1)
        activity.runOnUiThread {
            activity.button.error {
                countDown.countDown()
            }
        }
        countDown.await(10, TimeUnit.SECONDS)
        Assert.assertEquals(0, countDown.count)
    }


}
