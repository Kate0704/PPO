package ppo.battleship

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ppo.battleship.databinding.CellItemBinding


class GridAdapter(private var res: Resources,
                  private var cellList: MutableList<Cell>,
                  private val clickListener: (Cell) -> Unit)
    : ListAdapter<Cell, GridAdapter.CellViewHolder>(CellComparator()) {

    companion object {lateinit var binding: CellItemBinding}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        binding = CellItemBinding.inflate(LayoutInflater.from(parent.context))
        return CellViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        cellList[position].let { holder.bind(it, position, clickListener) }
    }

    override fun submitList(list:  MutableList<Cell>?) {
        super.submitList(list)
        if (!list.isNullOrEmpty()) { cellList = list }
    }

    inner class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cell: Cell, position: Int, clickListener: (Cell) -> Unit) {
            binding.cellImg.setImageResource(cell.imgRes)
            itemView.setOnClickListener { clickListener(cell) }
        }
    }

    override fun getItemCount(): Int {
        return cellList.size
    }

    class CellComparator : DiffUtil.ItemCallback<Cell>() {
        override fun areItemsTheSame(oldItem: Cell, newItem: Cell): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: Cell, newItem: Cell): Boolean {return false}
    }
}
