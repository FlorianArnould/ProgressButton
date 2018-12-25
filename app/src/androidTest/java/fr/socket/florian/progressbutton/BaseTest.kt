package fr.socket.florian.progressbutton

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.BeforeClass

abstract class BaseTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun init() {
            System.setProperty(
                "org.mockito.android.target",
                InstrumentationRegistry.getInstrumentation().targetContext.cacheDir.path
            )
        }
    }
}