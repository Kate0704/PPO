package ppo.tabata.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ppo.tabata.R
import ppo.tabata.data.TabataViewModel
import ppo.tabata.data.TabataViewModelFactory
import ppo.tabata.databinding.FragmentTabataListBinding
import ppo.tabata.utility.TabataApp
import ppo.tabata.utility.TabataListAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TabataListFragment : Fragment() {

    private lateinit var binding: FragmentTabataListBinding;

    private val tabataViewModel: TabataViewModel by viewModels {
        TabataViewModelFactory((activity?.application as TabataApp).repository)
    }


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        binding = FragmentTabataListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TabataListAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        tabataViewModel.allTabatas.observe(viewLifecycleOwner, Observer { words ->
            words?.let { adapter.submitList(it) }
        })
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}