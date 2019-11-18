package domain.shadowss.screen.dialog

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import domain.shadowss.R

class OverflowDialog(activity: Activity) : BaseDialog(activity, R.style.OverflowDialog) {

    override val shouldBeClosable = false

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
    }

    override fun resetWidgets() {}
}