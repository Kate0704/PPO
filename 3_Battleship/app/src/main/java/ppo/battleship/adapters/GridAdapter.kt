package ppo.battleship.adapters

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ppo.battleship.R
import ppo.battleship.models.Cell
import ppo.battleship.models.FieldReviewer
import ppo.battleship.models.Type


class GridAdapter(private val res: Resources,
                  var cellList: MutableList<Cell>,
                  private val cellClicked: (Cell, Int) -> Boolean)
    : ListAdapter<Cell, GridAdapter.CellViewHolder>(CellComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.cell_item, parent, false)
        return CellViewHolder(v)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        cellList[position].let { holder.bind(it, cellClicked) }
    }

    override fun submitList(list:  MutableList<Cell>?) {
        if (!list.isNullOrEmpty()) { cellList = list }
        this.notifyDataSetChanged()
    }

    inner class CellViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(cell: Cell, cellClicked: (Cell, Int) -> Boolean) {
            val imgView = itemView.findViewById<ImageView>(R.id.cell_img)
            when (cell.getCellType()) {
                    Type.EMPTY -> imgView.setImageResource(cell.emptyRes)
                    Type.SHIP -> imgView.setImageResource(R.color.ship)
                    Type.HIT -> {
                        imgView.setImageResource(cell.hitRes)
                        imgView.scaleType = ImageView.ScaleType.FIT_XY
                    }
                    Type.MISS -> {
                        imgView.setImageResource(cell.emptyRes)
                        imgView.foreground = res.getDrawable(R.drawable.cell_miss)}
            }
            imgView.setOnClickListener{
                if (cellClicked(cell, adapterPosition)) {
                    when (cell.getCellType()) {
                        Type.EMPTY -> imgView.setImageResource(cell.emptyRes)
                        Type.SHIP -> imgView.setImageResource(R.color.ship)
                        Type.HIT -> {
                            imgView.setImageResource(cell.hitRes)
                            imgView.scaleType = ImageView.ScaleType.FIT_XY
                        }
                        Type.MISS -> {
                            imgView.setImageResource(cell.emptyRes)
                            imgView.foreground = res.getDrawable(R.drawable.cell_miss)
                        }
                    }
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
