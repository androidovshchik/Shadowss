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
import org.kodein.di.generic.instance
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

interface RegistrationView : BaseView

class RegistrationActivity : BaseActivity(), RegistrationView {

    override val controller: RegistrationController by instance()

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
        et_phone.hint = "(+7) 999 999 9999"
        val slots = UnderscoreDigitSlotsParser().parseSlots("(+7) ___ ___ ___")
        val formatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
        formatWatcher.installOn(et_phone)
        btn_next.setOnClickListener {
            startActivity<DriverActivity>()
        }
    }

    companion object {

        const val EXTRA_DRIVER = "driver"
    }
}
