package ppo.tabata.utility

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ppo.tabata.data.TabataEntity
import ppo.tabata.databinding.RecyclerviewItemBinding
import ppo.tabata.ui.activities.TimerActivity
import ppo.tabata.viewModels.EditTabataViewModel

class TabataListAdapter(private var tabataList: List<TabataEntity>?,
                        private var itemListener: RecyclerViewLongClickListener,
                        private val clickListener: (TabataEntity) -> Unit)
    : ListAdapter<TabataEntity, TabataListAdapter.TabataViewHolder>(TabataComparator()) {

    companion object {lateinit var binding: RecyclerviewItemBinding}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabataViewHolder {
        binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context))
        return TabataViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: TabataViewHolder, position: Int) {
        tabataList?.get(position)?.let { holder.bind(it, clickListener) }
    }

    override fun submitList(list: List<TabataEntity>?) {
        super.submitList(list)
        if (!list.isNullOrEmpty()) { tabataList = list }
    }

    inner class TabataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bind(tabata: TabataEntity, clickListener: (TabataEntity) -> Unit) {
            binding.textView.text = tabata.name
            binding.work.text = EditTabataViewModel.getTime(tabata.work)
            binding.rest.text = EditTabataViewModel.getTime(tabata.rest)
            binding.reps.text = tabata.repeats.toString()
            binding.cycles.text = tabata.cycles.toString()
            binding.itemColor.setBackgroundColor(Color.parseColor(tabata.color))
            itemView.setOnClickListener { clickListener(tabata) }
            binding.playButton.setOnClickListener {
                val intent = Intent(it.context, TimerActivity::class.java)
                intent.putExtra("tabata", tabataList?.get(adapterPosition))
                it.context.startActivity(intent)
            }
            binding.deleteButton.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            tabataList?.get(adapterPosition)?.let { itemListener.recyclerViewListLongClicked(it) }
        }
    }

    interface RecyclerViewLongClickListener {
        fun recyclerViewListLongClicked(tabata: TabataEntity)
    }

    class TabataComparator : DiffUtil.ItemCallback<TabataEntity>() {
        override fun areItemsTheSame(oldItem: TabataEntity, newItem: TabataEntity): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: TabataEntity, newItem: TabataEntity): Boolean =
            (oldItem.name == newItem.name && oldItem.color == newItem.color)
    }
}
