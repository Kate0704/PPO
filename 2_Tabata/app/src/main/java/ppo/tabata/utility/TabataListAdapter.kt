package ppo.tabata.utility

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ppo.tabata.R
import ppo.tabata.data.TabataEntity

class TabataListAdapter internal constructor(
    context: Context
) : ListAdapter<TabataEntity, TabataListAdapter.TabataViewHolder>(TabataComparator()) {

    public val mContext: Context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabataViewHolder {
        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.recyclerview_item, parent, false)
        return TabataViewHolder(view)
    }

    override fun onBindViewHolder(holder: TabataViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name, current.color)
    }

    class TabataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tabataItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(name: String?, color: String?) {
            tabataItemView.text = name
            tabataItemView.setBackgroundColor(Color.parseColor(color))
        }

//        companion object {
//            fun create(parent: ViewGroup): TabataViewHolder {
//                val view: View = LayoutInflater.from(mContext)
//                    .inflate(R.layout.recyclerview_item, parent, false)
//                return TabataViewHolder(view)
//            }
//        }
    }

    class TabataComparator : DiffUtil.ItemCallback<TabataEntity>() {
        override fun areItemsTheSame(oldItem: TabataEntity, newItem: TabataEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TabataEntity, newItem: TabataEntity): Boolean {
            return (oldItem.name == newItem.name && oldItem.color == newItem.color)
        }
    }
}
