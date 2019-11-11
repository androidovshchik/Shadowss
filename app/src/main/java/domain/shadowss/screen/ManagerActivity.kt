package domain.shadowss.screen

import android.os.Bundle
import android.view.View
import domain.shadowss.R
import domain.shadowss.controller.ManagerController
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.generic.instance

interface ManagerView : BaseView

class ManagerActivity : BaseActivity(), ManagerView {

    override val requireLocation = true

    override val controller: ManagerController by instance()

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
