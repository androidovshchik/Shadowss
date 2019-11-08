package domain.shadowss.screen

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import domain.shadowss.R
import domain.shadowss.controller.TermsController
import domain.shadowss.screen.views.setData
import kotlinx.android.synthetic.main.activity_terms.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.margin

interface TermsView : BaseView

class TermsActivity : BaseActivity<TermsController>(), TermsView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        val appName = getString(R.string.app_name)
        toolbar_back.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                finish()
            }
        }
        toolbar_title.apply {
            (layoutParams as RelativeLayout.LayoutParams).apply {
                removeRule(RelativeLayout.CENTER_IN_PARENT)
                addRule(RelativeLayout.CENTER_VERTICAL)
                addRule(RelativeLayout.END_OF, R.id.toolbar_back)
                addRule(RelativeLayout.ALIGN_PARENT_END)
                margin = 0
            }
            setData("TOP,0000")
            text = text.replace("_+".toRegex(), appName)
        }
        assets.open("terms.html")
            .bufferedReader().use {
                web.loadDataWithBaseURL(
                    "file:///android_asset/",
                    it.readText().replace("AppName", appName),
                    "text/html",
                    "UTF-8",
                    null
                )
            }
    }
}
