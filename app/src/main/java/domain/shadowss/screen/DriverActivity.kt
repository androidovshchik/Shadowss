package domain.shadowss.screen

import android.os.Bundle
import android.view.View
import domain.shadowss.R
import domain.shadowss.controller.DriverController
import kotlinx.android.synthetic.main.toolbar.*

interface DriverView : BaseView

class DriverActivity : BaseActivity<DriverController>(), DriverView {

    override val requireLocation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)
        toolbar_back.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                finish()
            }
        }
    }
}
