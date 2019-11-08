package domain.shadowss.screen

import android.os.Bundle
import domain.shadowss.R
import domain.shadowss.controller.StartController
import domain.shadowss.screen.base.BaseActivity
import domain.shadowss.screen.base.BaseView
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.startActivity

interface StartView : BaseView

class StartActivity : BaseActivity<StartController>(), StartView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        btn_driver.setOnClickListener {
            startActivity<RegistrationActivity>()
        }
        btn_manager.setOnClickListener {
            startActivity<RegistrationActivity>()
        }
    }
}
