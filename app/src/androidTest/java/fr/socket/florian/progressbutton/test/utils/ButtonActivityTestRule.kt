package fr.socket.florian.progressbutton.test.utils

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import fr.socket.florian.progressbutton.test.ButtonActivity


class ButtonActivityTestRule : ActivityTestRule<ButtonActivity>(ButtonActivity::class.java, true, false) {
    fun launchActivity(layoutRes: Int): ButtonActivity {
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            ButtonActivity::class.java
        )
        intent.putExtra(ButtonActivity.LAYOUT_EXTRA, layoutRes)
        return launchActivity(intent)
    }
}