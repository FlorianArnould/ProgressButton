package fr.socket.florian.progressbutton

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.manual_test.*

class ButtonActivity : AppCompatActivity() {
    val button: ProgressButton
        get() = findViewById(R.id.progress_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutRes = intent.getIntExtra(LAYOUT_EXTRA, -1)
        if (layoutRes != -1) setContentView(layoutRes)
        else {
            setContentView(R.layout.manual_test)
            success_button.setOnClickListener {
                progress_button.success {}
            }
            error_button.setOnClickListener {
                progress_button.error {}
            }
        }
    }

    companion object {
        const val LAYOUT_EXTRA = "layout"
    }
}
