package domain.shadowss.screen

import android.os.Bundle
import domain.shadowss.R
import domain.shadowss.controller.DriverController
import domain.shadowss.screen.base.BaseActivity

interface DriverView {

}

class DriverActivity : BaseActivity<DriverController>(), DriverView {

    override val requiredLocation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)
    }
}
