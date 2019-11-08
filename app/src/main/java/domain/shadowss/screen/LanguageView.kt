package domain.shadowss.screen

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import domain.shadowss.R
import domain.shadowss.extension.use

@SuppressLint("Recycle")
fun TextView.obtainText(attrs: AttributeSet?, styleable: IntArray, index: Int) {
    attrs?.let {
        context.obtainStyledAttributes(it, styleable).use {
            text = getString(index)
        }
    }
}

class LanguageText : TextView {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        obtainText(attrs, R.styleable.LanguageText, R.styleable.LanguageText_text)
    }

    @Suppress("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        obtainText(attrs, R.styleable.LanguageText, R.styleable.LanguageText_text)
    }

    override fun hasOverlappingRendering() = false
}

class LanguageButton : Button {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        obtainText(attrs, R.styleable.LanguageText, R.styleable.LanguageText_text)
    }

    @Suppress("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        obtainText(attrs, R.styleable.LanguageText, R.styleable.LanguageText_text)
    }

    override fun hasOverlappingRendering() = false
}