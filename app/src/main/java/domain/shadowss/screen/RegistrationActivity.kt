package domain.shadowss.screen

import android.os.Bundle
import domain.shadowss.R
import domain.shadowss.controller.RegistrationController
import domain.shadowss.screen.base.BaseActivity
import domain.shadowss.screen.base.BaseView

interface RegistrationView : BaseView

class RegistrationActivity : BaseActivity<RegistrationController>(), RegistrationView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }
}
