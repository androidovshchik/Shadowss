package domain.shadowss.screen.views

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import domain.shadowss.R
import domain.shadowss.extension.use
import domain.shadowss.manager.LanguageManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

interface LanguageView : KodeinAware {

    val languageManager: LanguageManager

    fun getContext(): Context

    fun setText(text: CharSequence?)
}

@SuppressLint("Recycle")
fun LanguageView.setData(data: String?) {
    setText(languageManager.getText(data))
}

@SuppressLint("Recycle")
private fun LanguageView.obtainText(attrs: AttributeSet?, styleable: IntArray, index: Int) {
    attrs?.let {
        getContext().obtainStyledAttributes(it, styleable).use {
            if (hasValue(index)) {
                setData(getString(index))
            }
        }
    }
}

class LanguageText : TextView, LanguageView {

    override val kodein by closestKodein()

    override val languageManager: LanguageManager by instance()

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

class LanguageButton : Button, LanguageView {

    override val kodein by closestKodein()

    override val languageManager: LanguageManager by instance()

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        obtainText(attrs, R.styleable.LanguageButton, R.styleable.LanguageButton_text)
    }

    @Suppress("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        obtainText(attrs, R.styleable.LanguageButton, R.styleable.LanguageButton_text)
    }

    override fun hasOverlappingRendering() = false
}