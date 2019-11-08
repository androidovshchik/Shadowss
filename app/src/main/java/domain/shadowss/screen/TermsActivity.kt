package domain.shadowss.screen

import android.os.Bundle
import domain.shadowss.R
import domain.shadowss.controller.StartController

interface TermsView : BaseView

class TermsActivity : BaseActivity<StartController>(), TermsView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
    }
}
