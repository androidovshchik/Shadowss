package domain.shadowss.screen

import android.os.Bundle
import android.view.View
import domain.shadowss.R
import domain.shadowss.controller.ManagerController
import kotlinx.android.synthetic.main.toolbar.*

interface ManagerView : BaseView

class ManagerActivity : BaseActivity<ManagerController>(), ManagerView {

    override val requireLocation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        toolbar_back.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                finish()
            }
        }
    }
}
