package domain.shadowss.screen

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import domain.shadowss.R
import domain.shadowss.controller.RegistrationController
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity

interface RegistrationView : BaseView

class RegistrationActivity : BaseActivity<RegistrationController>(), RegistrationView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        toolbar_back.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                finish()
            }
        }
        iv_logo.setImageBitmap(BitmapFactory.decodeStream(assets.open("logo.png")))
        val affineFormats = ArrayList<String>()
        affineFormats.add("+7 ([000]) [000]-[00]-[00]#[000]")

        val listener = MaskedTextChangedListener.installOn(
            et_phone,
            "+7 ([000]) [000]-[00]-[00]",
            affineFormats,
            AffinityCalculationStrategy.WHOLE_STRING,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {

                }
            }
        )

        et_phone.hint = listener.placeholder()
        btn_next.setOnClickListener {
            startActivity<DriverActivity>()
        }
    }
}
