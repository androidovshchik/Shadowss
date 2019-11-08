package domain.shadowss.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import domain.shadowss.R
import domain.shadowss.controller.StartController
import domain.shadowss.domain.Language
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.startActivity

interface StartView : BaseView

class StartActivity : BaseActivity<StartController>(), StartView {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        tv_welcome.text = "${tv_welcome.text}\n${getString(R.string.app_name)}"
        val adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            Language.map.map { it.value.desc }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_language.adapter = adapter
        ll_terms.setOnClickListener {
            cb_terms.isChecked = !cb_terms.isChecked
        }
        btn_driver.setOnClickListener {
            startActivity<RegistrationActivity>()
        }
        btn_manager.setOnClickListener {
            startActivity<RegistrationActivity>()
        }
    }
}
