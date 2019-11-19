package domain.shadowss.screen

import android.os.Bundle
import domain.shadowss.R
import domain.shadowss.controller.ManagerController
import domain.shadowss.screen.view.setData
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.generic.instance

interface ManagerView : BaseView

class ManagerActivity : BaseActivity(), ManagerView {

    override val controller: ManagerController by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        toolbar_title.setData("[[TOP,0004]]")
    }
}
