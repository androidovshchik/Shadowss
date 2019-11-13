package domain.shadowss.screen

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ArrayAdapter
import domain.shadowss.R
import domain.shadowss.controller.StartController
import domain.shadowss.local.Preferences
import domain.shadowss.manager.LanguageManager
import domain.shadowss.model.Language
import domain.shadowss.screen.dialog.ErrorDialog
import domain.shadowss.screen.view.replaceUnderline
import domain.shadowss.screen.view.updateData
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.sdk19.listeners.onItemSelectedListener
import org.jetbrains.anko.startActivity
import org.kodein.di.generic.instance
import kotlin.math.max

interface StartView : BaseView {

    fun onSuccess()

    fun onError()
}

class StartActivity : BaseActivity(), StartView {

    override val controller: StartController by instance()

    private val languageManager: LanguageManager by instance()

    private val preferences: Preferences by instance()

    private val errorDialog: ErrorDialog by instance()

    private var isDriver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        spn_language.let {
            val keys = Language.map.keys.toTypedArray()
            val adapter = ArrayAdapter(
                applicationContext,
                android.R.layout.simple_spinner_item,
                Language.map.map { item -> item.value.desc })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            it.adapter = adapter
            it.setSelection(max(0, keys.indexOf(preferences.language)), false)
            it.onItemSelectedListener {
                onItemSelected { _, _, position, _ ->
                    preferences.language = keys[position]
                    languageManager.updatePack(applicationContext)
                    tv_welcome.updateData()
                    tv_terms.updateData()
                    btn_driver.updateData()
                    btn_manager.updateData()
                    updateText()
                }
            }
        }
        cb_terms.apply {
            isChecked = preferences.agree
            setOnCheckedChangeListener { _, isChecked ->
                preferences.agree = isChecked
            }
        }
        tv_terms.movementMethod = LinkMovementMethod.getInstance()
        updateText()
        iv_logo.setImageBitmap(BitmapFactory.decodeStream(assets.open("logo.png")))
        btn_driver.setOnClickListener {
            it.isEnabled = false
            isDriver = true
            controller.onChoice()
        }
        btn_manager.setOnClickListener {
            it.isEnabled = false
            isDriver = false
            controller.onChoice()
        }
        controller.checkRights(this)
    }

    override fun onSuccess() {
        startActivity<RegistrationActivity>(
            RegistrationActivity.EXTRA_DRIVER to isDriver
        )
    }

    override fun onError() {
        btn_driver.isEnabled = true
        btn_manager.isEnabled = true
    }

    @SuppressLint("SetTextI18n")
    private fun updateText() {
        val appName = getString(R.string.app_name)
        tv_welcome.apply {
            replaceUnderline("\n$appName", "$text\n$appName")
        }
        tv_terms.apply {
            val start = text.indexOf("\u2009") + 1
            val result = replaceUnderline(appName, "$text$appName")
            text = SpannableStringBuilder(result).apply {
                setSpan(object : ClickableSpan() {

                    override fun onClick(widget: View) {
                        startActivity<TermsActivity>()
                    }
                }, start, result.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}
