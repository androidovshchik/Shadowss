package domain.shadowss.screen.dialogs

import android.app.Activity
import android.os.Bundle
import android.view.Window
import domain.shadowss.R

class ErrorDialog(activity: Activity) : BaseDialog(activity) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_error)
    }
}