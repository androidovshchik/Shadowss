package domain.shadowss.screen

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.chibatching.kotpref.blockingBulk
import defpackage.marsh.RGI1Data
import defpackage.marsh.SARM
import domain.shadowss.R
import domain.shadowss.controller.RegistrationController
import domain.shadowss.local.Preferences
import domain.shadowss.screen.dialog.ErrorDialog
import domain.shadowss.screen.dialog.OverflowDialog
import domain.shadowss.screen.view.setData
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.sdk19.listeners.onItemSelectedListener
import org.kodein.di.generic.instance
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

interface RegistrationView : BaseView {

    var regCode: String

    var mobilePhone: String

    var userType: Int

    fun onWait()

    fun onSuccess(value: String)

    fun onError(data: String, instance: Any? = null)
}

class RegistrationActivity : BaseActivity(), RegistrationView {

    override val controller: RegistrationController by instance()

    private val preferences: Preferences by instance()

    private val errorDialog: ErrorDialog by instance()

    private val overflowDialog: OverflowDialog by instance()

    private lateinit var formatWatcher: MaskFormatWatcher

    @Volatile
    override var regCode = ""

    @Volatile
    override var mobilePhone = ""

    @Volatile
    override var userType = 0

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        userType = if (intent.getBooleanExtra(EXTRA_DRIVER, false)) DRIVER else MANAGER
        val data = intent.getSerializableExtra(EXTRA_ARRAY) as Array<RGI1Data>
        toolbar_back.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                finish()
            }
        }
        toolbar_title.setData(if (userType == DRIVER) "[[TOP,0001]]" else "[[TOP,0002]]")
        iv_logo.setImageBitmap(BitmapFactory.decodeStream(assets.open("logo.png")))
        spn_country.let {
            val adapter = ArrayAdapter(
                applicationContext,
                android.R.layout.simple_spinner_item,
                data.map { item -> "${item.country} ${item.regcode}" }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            it.adapter = adapter
            it.setSelection(0, false)
            it.onItemSelectedListener {
                onItemSelected { _, _, position, _ ->
                    updatePhone(data[position])
                }
            }
        }
        updatePhone(data[0])
        btn_next.setOnClickListener {
            val item = data[spn_country.selectedItemPosition]
            val codeLength = item.regcode.length - 3
            val numbers = et_phone.text.toString()
                .replace("[(+) ]".toRegex(), "")
            if (numbers.length - codeLength in item.min..item.max) {
                if (controller.onContinue(applicationContext)) {
                    regCode = item.regcode.substring(1, item.regcode.length - 1)
                    mobilePhone = numbers.substring(codeLength)
                }
            } else {
                onError("[[MSG,0011]]")
            }
        }
        controller.checkRights(this)
    }

    override fun onWait() {
        overflowDialog.apply {
            onUiThread {
                show()
            }
        }
    }

    override fun onSuccess(value: String) {
        preferences.blockingBulk {
            user = userType
            token = value
        }
        when (userType) {
            MANAGER -> startActivity(intentFor<ManagerActivity>().clearTask().newTask())
            DRIVER -> startActivity(intentFor<DriverActivity>().clearTask().newTask())
        }
    }

    override fun onError(data: String, instance: Any?) {
        errorDialog.apply {
            txtData = data
            when (instance) {
                is SARM -> msg = instance.dataerr
            }
            onUiThread {
                show()
            }
        }
    }

    private fun updatePhone(item: RGI1Data) {
        val raw = "${item.regcode} ${item.numex.replace("[^\\s]".toRegex(), "_")}"
        val mask = MaskImpl.createTerminated(UnderscoreDigitSlotsParser().parseSlots(raw))
        et_phone.let {
            it.hint = "${item.regcode} ${item.numex}"
            if (!::formatWatcher.isInitialized) {
                formatWatcher = MaskFormatWatcher(mask).apply {
                    installOn(it)
                }
            } else {
                formatWatcher.setMask(mask)
            }
        }
    }

    companion object {

        const val MANAGER = 1

        const val DRIVER = 2

        const val EXTRA_DRIVER = "driver"

        const val EXTRA_ARRAY = "array"
    }
}
