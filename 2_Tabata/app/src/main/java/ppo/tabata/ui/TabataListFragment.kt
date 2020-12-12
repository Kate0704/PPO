package ppo.tabata.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ppo.tabata.R
import ppo.tabata.data.TabataEntity
import ppo.tabata.viewModels.TabataViewModel
import ppo.tabata.viewModels.TabataViewModelFactory
import ppo.tabata.databinding.FragmentTabataListBinding
import ppo.tabata.utility.TabataApp
import ppo.tabata.utility.TabataListAdapter

class TabataListFragment : Fragment() {

    private val binding: FragmentTabataListBinding by lazy { FragmentTabataListBinding.inflate(layoutInflater) }
    private val viewModel: EditTabataViewModel by activityViewModels()
    private val tabataViewModel: TabataViewModel by viewModels {
        TabataViewModelFactory((activity?.application as TabataApp).repository)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TabataListAdapter (tabataViewModel.allTabatas.value) { tabata -> tabataItemClicked(tabata) }
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        tabataViewModel.allTabatas.observe(viewLifecycleOwner, Observer { tabatas ->
            tabatas?.let { adapter.submitList(it) }
        })
        binding.fab.setOnClickListener {
            viewModel.newTabata = true
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun tabataItemClicked(tabata: TabataEntity) {
        viewModel.setTabata(tabata)
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }
}