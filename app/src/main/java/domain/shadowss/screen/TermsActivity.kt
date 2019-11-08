package domain.shadowss.screen

import android.os.Bundle
import domain.shadowss.R
import domain.shadowss.controller.StartController
import domain.shadowss.screen.base.BaseActivity
import domain.shadowss.screen.base.BaseView

interface TermsView : BaseView

class TermsActivity : BaseActivity<StartController>(), TermsView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
    }
}
