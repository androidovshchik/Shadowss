package domain.shadowss.screen.dialog

import android.app.Activity
import android.os.Bundle
import android.view.Window
import domain.shadowss.R

class ErrorDialog(activity: Activity) : BaseDialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_error)
    }
}