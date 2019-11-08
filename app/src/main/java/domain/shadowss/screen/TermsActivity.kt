package domain.shadowss.screen

import android.os.Bundle
import domain.shadowss.R
import domain.shadowss.controller.StartController
import domain.shadowss.screen.base.BaseActivity

interface TermsView {

}

class TermsActivity : BaseActivity<StartController>(), TermsView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
    }
}
