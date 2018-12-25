package fr.socket.florian.progressbutton

import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Handler
import android.util.AttributeSet
import android.util.StateSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import kotlinx.android.synthetic.main.progress_button.view.*

class ProgressButton : RelativeLayout, View.OnClickListener {
    private var onClickListener: OnClickListener? = null
    var normalStateBackground = StateListDrawable()
    var successBackground: Drawable? = null
    var errorBackground: Drawable? = null
    var succeed: State = State.WAITING
        private set
    var revertDelay: Int = 1000
    var revertOnSuccess: Boolean = false
    var revertOnError: Boolean = false

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        successBackground = context.getDrawable(R.drawable.button_success)
        errorBackground = context.getDrawable(R.drawable.button_error)

        inflate(context, R.layout.progress_button, this)
        button.setOnClickListener(this)
        initAttributes(attrs)
    }

    private fun initAttributes(attrs: AttributeSet?) {
        var normalBackground = context.getDrawable(R.drawable.button_normal)
        var pressedBackground = context.getDrawable(R.drawable.button_pressed)
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.ProgressButton, 0, 0).apply {
                try {
                    setText(this)
                    buttonText.isAllCaps = getBoolean(R.styleable.ProgressButton_textAllCaps, false)
                    setTextColor(this)
                    setTextAppearance(this)
                    setProgressColor(this)
                    normalBackground = getDrawable(R.styleable.ProgressButton_background) ?: normalBackground
                    pressedBackground = getDrawable(R.styleable.ProgressButton_pressedBackground) ?: pressedBackground
                    successBackground = getDrawable(R.styleable.ProgressButton_successBackground) ?: successBackground
                    errorBackground = getDrawable(R.styleable.ProgressButton_errorBackground) ?: errorBackground
                    revertDelay = getInt(R.styleable.ProgressButton_revertDelayMs, 1000)
                    revertOnSuccess = getBoolean(R.styleable.ProgressButton_revertOnSuccess, false)
                    revertOnError = getBoolean(R.styleable.ProgressButton_revertOnError, false)
                } finally {
                    recycle()
                }
            }
        }
        normalStateBackground.addState(intArrayOf(android.R.attr.state_pressed), pressedBackground)
        normalStateBackground.addState(StateSet.WILD_CARD, normalBackground)
        button.background = normalStateBackground
    }

    private fun setText(array: TypedArray) {
        val res = array.getResourceId(R.styleable.ProgressButton_text, -1)
        if (res != -1) buttonText.setText(res)
        else {
            val string = array.getNonResourceString(R.styleable.ProgressButton_text)
            if (string != null) buttonText.text = string
        }
    }

    private fun setTextColor(array: TypedArray) {
        val color = array.getColor(R.styleable.ProgressButton_textColor, -1)
        if(color != -1) buttonText.setTextColor(color)
        else {
            val colorStateList = array.getColorStateList(R.styleable.ProgressButton_textColor)
            if(colorStateList != null) buttonText.setTextColor(colorStateList)
        }
    }

    private fun setTextAppearance(array: TypedArray) {
        val res = array.getResourceId(R.styleable.ProgressButton_textAppearance, -1)
        if (res != -1) buttonText.setTextAppearance(res)
    }

    private fun setProgressColor(array: TypedArray) {
        val color = array.getColor(R.styleable.ProgressButton_progressColor, -1)
        if (color != -1) buttonProgress.indeterminateDrawable.colorFilter = PorterDuffColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)

    }

    override fun onClick(view: View?) {
        displayButtonToProgress {
            onClickListener?.onClick(this)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        onClickListener = l
    }

    private fun displayButtonToProgress(callback: () -> Unit) {
        button.isEnabled = false
        buttonText.animate().alpha(0f).duration = 500
        buttonText.postOnAnimation {
            buttonProgress.animate().alpha(1f).duration = 500
            buttonProgress.postOnAnimation(callback)
        }
    }

    private fun progressToSomething(background: Drawable?, @DrawableRes iconRes: Int, shouldRevert: Boolean, callback: (() -> Unit)?) {
        buttonProgress.animate().alpha(0f).duration = 500
        buttonProgress.postOnAnimation {
            val transitionDrawable = TransitionDrawable(
                arrayOf(
                    normalStateBackground,
                    background
                )
            )
            button.background = transitionDrawable
            transitionDrawable.isCrossFadeEnabled = true
            transitionDrawable.startTransition(500)
            resultButtonIcon.setImageResource(iconRes)
            resultButtonIcon.animate().alpha(1f).duration = 500
            if(shouldRevert) Handler().postDelayed({ reinitialize(callback) }, revertDelay.toLong())
            else resultButtonIcon.postOnAnimation { callback?.invoke() }
        }
    }

    fun success(callback: (() -> Unit)? = null) {
        succeed = State.SUCCEED
        progressToSomething(successBackground, R.drawable.ic_check, revertOnSuccess, callback)
    }

    fun error(callback: (() -> Unit)? = null) {
        succeed = State.FAILED
        progressToSomething(errorBackground, R.drawable.ic_close, revertOnError, callback)
    }

    fun reinitialize(callback: (() -> Unit)? = null) {
        val succeed = this.succeed
        this.succeed = State.WAITING
        if (succeed != State.WAITING) {
            val transitionDrawable = TransitionDrawable(
                arrayOf(
                    if (succeed == State.SUCCEED) successBackground else errorBackground,
                    normalStateBackground
                )
            )
            button.background = transitionDrawable
            transitionDrawable.isCrossFadeEnabled = true
            transitionDrawable.startTransition(500)
            resultButtonIcon.animate().alpha(0f).duration = 500
            resultButtonIcon.postOnAnimation {
                buttonText.animate().alpha(1f).duration = 500
                button.isEnabled = true
                callback?.invoke()
            }
        } else callback?.invoke()
    }

    companion object {
        enum class State {
            WAITING,
            SUCCEED,
            FAILED
        }

    }
}