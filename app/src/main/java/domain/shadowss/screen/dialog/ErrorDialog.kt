package domain.shadowss.screen.dialog

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import domain.shadowss.R
import domain.shadowss.screen.view.setData
import kotlinx.android.synthetic.main.dialog_error.*

class ErrorDialog(activity: Activity) : BaseDialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_error)
        dialog_close.setOnClickListener {
            if (isClosable) {
                dismiss()
            }
        }
    }

    override fun onClosable() {
        dialog_close.visibility = View.VISIBLE
    }

    fun setData(data: String) {
        val textId = data.split(",")[1]
        dialog_code.text = textId.substring(0, textId.indexOf("]]"))
        dialog_text.setData(data)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if () {

        }
    }
}