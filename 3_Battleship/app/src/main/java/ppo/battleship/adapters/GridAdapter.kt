package ppo.battleship.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ppo.battleship.R
import ppo.battleship.databinding.CellItemBinding
import ppo.battleship.models.Cell
import ppo.battleship.models.Type


class GridAdapter(private var cellList: MutableList<Cell>)
    : ListAdapter<Cell, GridAdapter.CellViewHolder>(CellComparator()) {

    companion object {lateinit var binding: CellItemBinding}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        binding = CellItemBinding.inflate(LayoutInflater.from(parent.context))
        return CellViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        cellList[position].let { holder.bind(it) }
    }

    override fun submitList(list:  MutableList<Cell>?) {
        super.submitList(list)
        if (!list.isNullOrEmpty()) { cellList = list }
    }

    inner class CellViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(cell: Cell) {
            val imgView = itemView.findViewById<ImageView>(R.id.cell_img)
            imgView.setImageResource(cell.emptyRes)
            imgView.setOnClickListener{
                if (cell.getCellType() == Type.EMPTY) cell.setCellType(
                    Type.SHIP)
                else cell.setCellType(Type.EMPTY)
                when (cell.getCellType()) {
                    Type.EMPTY -> imgView.setImageResource(cell.emptyRes)
                    Type.SHIP -> imgView.setImageResource(R.color.ship)
                    Type.HIT -> binding.cellImg.setImageResource(cell.hitRes)
                    Type.MISS -> binding.cellImg.setImageResource(
                        R.color.miss
                    )
                }
            }
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
