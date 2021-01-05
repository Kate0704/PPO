package ppo.battleship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ppo.battleship.databinding.FragmentFieldBinding

class FieldFragment : Fragment() {

    private val binding by lazy { FragmentFieldBinding.inflate(layoutInflater) }
    private var cellList: MutableList<Cell> = MutableList(100) {Cell()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = GridAdapter (resources, cellList) { cell -> cellClicked(cell) }
        binding.field.layoutManager = GridLayoutManager(context, 10)
        binding.field.adapter = adapter

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun cellClicked(cell: Cell) {
    }
}