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
import ppo.tabata.databinding.RecyclerviewItemBinding

class TabataListAdapter : ListAdapter<TabataEntity, TabataListAdapter.TabataViewHolder>(TabataComparator()) {

    private companion object {lateinit var binding: RecyclerviewItemBinding}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabataViewHolder {
        return TabataViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TabataViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name, current.color)
    }

    class TabataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(name: String, color: Int) {
            binding.textView.text = name
            binding.itemColor.setBackgroundColor(color)
        }

        companion object {
            fun create(parent: ViewGroup): TabataViewHolder {
                binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context))
                return TabataViewHolder(binding.root)
            }
        }
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
