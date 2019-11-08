package domain.shadowss.screen

import android.os.Bundle
import domain.shadowss.R
import domain.shadowss.controller.ManagerController
import domain.shadowss.screen.base.BaseActivity
import domain.shadowss.screen.base.BaseView

interface ManagerView : BaseView

class ManagerActivity : BaseActivity<ManagerController>(), ManagerView {

    override val requiredLocation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
    }
}
