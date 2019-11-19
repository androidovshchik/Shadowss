package domain.shadowss.screen.dialog

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import domain.shadowss.R
import domain.shadowss.screen.view.setData
import kotlinx.android.synthetic.main.dialog_overflow.*
import java.lang.ref.WeakReference
import kotlin.math.ceil

interface CountCallback {

    fun onTick(millisUntilFinished: Long)

    fun onFinish()
}

class CountTimer(callback: CountCallback) : CountDownTimer(60_000L, 100L) {

    private val reference = WeakReference(callback)

    override fun onFinish() {
        reference.get()?.onFinish()
    }

    override fun onTick(millisUntilFinished: Long) {
        reference.get()?.onTick(millisUntilFinished)
    }
}

class OverflowDialog(activity: Activity) : BaseDialog(activity, R.style.OverflowDialog),
    CountCallback {

    override val shouldBeClosable = false

    private val countTimer = CountTimer(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.apply {
            setBackgroundDrawableResource(R.color.colorDim)
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
        setContentView(R.layout.dialog_overflow)
        dialog_title.setData("[[TXT,0004]]")
    }

    override fun onShow(dialog: DialogInterface?) {
        super.onShow(dialog)
        countTimer.start()
    }

    override fun onTick(millisUntilFinished: Long) {
        dialog_time.text = ceil(millisUntilFinished / 1000.0).toInt().toString()
    }

    override fun onFinish() {
        dismiss()
    }

    override fun resetWidgets() {
        dialog_time.text = ""
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        countTimer.cancel()
        makeCallback<CountCallback> {
            onFinish()
        }
    }
}