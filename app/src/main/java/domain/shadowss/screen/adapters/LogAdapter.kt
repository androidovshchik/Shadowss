package domain.shadowss.screen.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import domain.shadowss.R
import domain.shadowss.extension.inflate
import kotlinx.android.synthetic.main.item_text.view.*

class LogAdapter : BaseAdapter<String>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_text))
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

        private val text: TextView = itemView.tv_text

        @SuppressLint("SetTextI18n")
        override fun onBindItem(position: Int, item: String) {
            text.text = item
        }
    }
}