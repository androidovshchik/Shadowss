package domain.shadowss.screen.dialog

import android.app.Activity
import android.os.Bundle
import domain.shadowss.R
import domain.shadowss.screen.view.setData
import kotlinx.android.synthetic.main.dialog_error.*

class ErrorDialog(activity: Activity) : BaseDialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_error)
        dialog_close.setOnClickListener {
            if (allowClose) {
                dismiss()
            }
        }
    }

    override fun onClosable() {

    }

    fun setErrorData(data: String) {
        dialog_code.text = data.split(",")[1]
            .replace("]]", "")
        dialog_text.setData(data)
    }
}