package domain.shadowss.screen

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import domain.shadowss.R
import domain.shadowss.controller.StartController
import domain.shadowss.screen.views.setData
import kotlinx.android.synthetic.main.activity_terms.*
import kotlinx.android.synthetic.main.toolbar.*

interface TermsView : BaseView

class TermsActivity : BaseActivity<StartController>(), TermsView {

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
            }
            setData("TOP,0000")
            text = text.replace("_+".toRegex(), appName)
        }
        web.loadUrl("file:///android_asset/terms.html")
    }
}
