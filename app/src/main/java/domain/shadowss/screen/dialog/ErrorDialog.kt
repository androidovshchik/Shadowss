package domain.shadowss.screen.dialog

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import domain.shadowss.R
import domain.shadowss.screen.view.setData
import domain.shadowss.screen.view.updateData
import kotlinx.android.synthetic.main.dialog_error.*
import org.jetbrains.anko.browse
import timber.log.Timber

class ErrorDialog(activity: Activity) : BaseDialog(activity) {

    var textData: String? = null

    var marketLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_error)
        dialog_close.setOnClickListener {
            if (isClosable) {
                dismiss()
            }
        }
    }

    override fun onShow(dialog: DialogInterface?) {
        super.onShow(dialog)
        dialog_title.updateData()
        textData?.let {
            it.split(",")[1].apply {
                dialog_code.text = substring(0, indexOf("]]"))
            }
            dialog_text.setData(it)
        } ?: resetWidgets()
        dialog_close.apply {
            dialog_close.visibility = View.GONE
            updateData()
        }
    }

    override fun onClosableState() {
        dialog_close.visibility = View.VISIBLE
    }

    override fun resetWidgets() {
        dialog_code.text = ""
        dialog_text.text = ""
        dialog_close.text = ""
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        marketLink?.let {
            makeCallback<Activity> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$it")))
                } catch (e: Throwable) {
                    Timber.e(e)
                    browse("https://play.google.com/store/apps/details?id=$it")
                }
            }
            marketLink = null
        }
    }
}