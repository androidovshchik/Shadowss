package domain.shadowss.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ArrayAdapter
import domain.shadowss.R
import domain.shadowss.controller.StartController
import domain.shadowss.domain.Language
import domain.shadowss.screen.views.setData
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.startActivity

interface StartView : BaseView

class StartActivity : BaseActivity<StartController>(), StartView {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        preferences.language = Language.EN.id
        val appName = getString(R.string.app_name)
        tv_welcome.text = "${tv_welcome.text}\n${appName}"
        val adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            Language.map.map { it.value.desc }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_language.adapter = adapter
        tv_terms.apply {
            movementMethod = LinkMovementMethod.getInstance()
            val start = text.length + 1
            setData("TXT,0002", "${tv_terms.text} ")
            val result = text.replace("_+".toRegex(), appName)
            text = SpannableStringBuilder(result).apply {
                setSpan(object : ClickableSpan() {

                    override fun onClick(widget: View) {
                        startActivity<TermsActivity>()
                    }
                }, start, result.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        btn_driver.setOnClickListener {
            startActivity<RegistrationActivity>()
        }
        btn_manager.setOnClickListener {
            startActivity<RegistrationActivity>()
        }
    }
}
