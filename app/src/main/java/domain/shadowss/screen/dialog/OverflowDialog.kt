package domain.shadowss.screen.dialog

import android.app.Activity
import android.os.Bundle
import domain.shadowss.R

class OverflowDialog(activity: Activity) : BaseDialog(activity) {

    override val shouldBeClosable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_overflow)
    }

    override fun resetWidgets() {}
}