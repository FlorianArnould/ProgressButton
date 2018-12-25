package fr.socket.florian.progressbutton.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import fr.socket.florian.progressbutton.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


fun clickOnButton() {
    Espresso.onView(ViewMatchers.withId(R.id.progress_button))
        .perform(ViewActions.click())
}

fun withBackground(drawable: Drawable): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(item: View?): Boolean {
            if (item == null || item !is AppCompatButton) return false
            return areDrawablesIdentical(item.background.current, drawable)
        }

        override fun describeTo(description: Description?) {}
    }
}

private fun areDrawablesIdentical(drawableA: Drawable, drawableB: Drawable): Boolean {
    val stateA = drawableA.constantState
    val stateB = drawableB.constantState
    // If the constant state is identical, they are using the same drawable resource.
    // However, the opposite is not necessarily true.
    return stateA != null && stateB != null && stateA == stateB || getBitmap(drawableA).sameAs(
        getBitmap(
            drawableB
        )
    )
}

private fun getBitmap(drawable: Drawable): Bitmap {
    val result: Bitmap
    if (drawable is BitmapDrawable) {
        result = drawable.bitmap
    } else {
        var width = drawable.intrinsicWidth
        var height = drawable.intrinsicHeight
        // Some drawables have no intrinsic width - e.g. solid colours.
        if (width <= 0) {
            width = 1
        }
        if (height <= 0) {
            height = 1
        }

        result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
    }
    return result
}