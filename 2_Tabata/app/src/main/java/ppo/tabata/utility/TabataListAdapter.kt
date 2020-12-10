package ppo.tabata.utility

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ppo.tabata.R
import ppo.tabata.data.TabataEntity
import ppo.tabata.databinding.RecyclerviewItemBinding
import ppo.tabata.ui.EditTabataViewModel

class TabataListAdapter(private var tabataList: List<TabataEntity>?, private val clickListener: (TabataEntity) -> Unit)
    : ListAdapter<TabataEntity, TabataListAdapter.TabataViewHolder>(TabataComparator()) {

    private companion object {lateinit var binding: RecyclerviewItemBinding}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabataViewHolder {
        binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context))
        binding.playButton.setOnClickListener {

        }
        return TabataViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: TabataViewHolder, position: Int) {
        if (!tabataList.isNullOrEmpty())
            tabataList?.get(position)?.let { holder.bind(it, clickListener) }
    }

    override fun submitList(list: List<TabataEntity>?) {
        super.submitList(list)
        if (list != null) {
            tabataList = list
        }
    }

    class TabataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tabata: TabataEntity, clickListener: (TabataEntity) -> Unit) {
            binding.textView.text = tabata.name
            binding.work.text = EditTabataViewModel.getTime(tabata.work)
            binding.rest.text = EditTabataViewModel.getTime(tabata.rest)
            binding.reps.text = tabata.repeats.toString()
            binding.cycles.text = tabata.cycles.toString()
            binding.itemColor.setBackgroundColor(Color.parseColor(tabata.color))
            itemView.setOnClickListener { clickListener(tabata) }
        }
    }

    class TabataComparator : DiffUtil.ItemCallback<TabataEntity>() {
        override fun areItemsTheSame(oldItem: TabataEntity, newItem: TabataEntity): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: TabataEntity, newItem: TabataEntity): Boolean =
            (oldItem.name == newItem.name && oldItem.color == newItem.color)
    }
}
