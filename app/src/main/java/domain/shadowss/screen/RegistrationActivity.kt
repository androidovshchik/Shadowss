package domain.shadowss.screen

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import domain.shadowss.R
import domain.shadowss.controller.RegistrationController
import domain.shadowss.screen.view.setData
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity

interface RegistrationView : BaseView

class RegistrationActivity : BaseActivity<RegistrationController>(), RegistrationView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val isDriver = intent.getBooleanExtra(EXTRA_DRIVER, false)
        toolbar_back.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                finish()
            }
        }
        toolbar_title.setData(if (isDriver) "[[TOP,0001]]" else "[[TOP,0002]]")
        iv_logo.setImageBitmap(BitmapFactory.decodeStream(assets.open("logo.png")))
        btn_next.setOnClickListener {
            startActivity<DriverActivity>()
        }
    }

    companion object {

        const val EXTRA_DRIVER = "driver"
    }
}
