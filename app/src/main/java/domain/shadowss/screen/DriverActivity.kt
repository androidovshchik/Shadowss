package domain.shadowss.screen

import android.os.Bundle
import android.view.View
import domain.shadowss.R
import domain.shadowss.controller.DriverController
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.generic.instance

interface DriverView : BaseView

class DriverActivity : BaseActivity(), DriverView {

    override val controller: DriverController by instance()

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
