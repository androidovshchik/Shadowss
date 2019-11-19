package domain.shadowss.screen.dialog

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import domain.shadowss.R
import domain.shadowss.screen.view.setData
import kotlinx.android.synthetic.main.dialog_overflow.*

interface CountCallback {

    fun onFinishCount()
}

class CountTimer : CountDownTimer(60_000L, 100L) {

    override fun onFinish() {}

    override fun onTick(millisUntilFinished: Long) {
        if (millisUntilFinished <= 5000) {

        } else {

        }
    }
}

class OverflowDialog(activity: Activity) : BaseDialog(activity, R.style.OverflowDialog),
    CountCallback {

    override val shouldBeClosable = false

    private val countTimer = CountTimer()

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

    }

    override fun onFinishCount() {
        dismiss()
    }

    override fun resetWidgets() {
        dialog_time.text = ""
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        makeCallback<CountCallback> {
            onFinishCount()
        }
    }
}