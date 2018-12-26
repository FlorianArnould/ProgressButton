package fr.socket.florian.progressbutton

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ButtonActivity : AppCompatActivity() {
    val button: ProgressButton
        get() = findViewById(R.id.progress_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutRes = intent.getIntExtra(LAYOUT_EXTRA, -1)
        if (layoutRes != -1) setContentView(layoutRes)
    }

    companion object {
        const val LAYOUT_EXTRA = "layout"
    }
}
