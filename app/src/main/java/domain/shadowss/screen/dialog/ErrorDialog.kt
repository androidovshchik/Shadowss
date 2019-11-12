package domain.shadowss.screen.dialog

import android.app.Activity
import android.os.Bundle
import domain.shadowss.R

class ErrorDialog(activity: Activity) : BaseDialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_error)
    }
}