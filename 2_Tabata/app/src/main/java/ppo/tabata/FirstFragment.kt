package ppo.tabata

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ppo.tabata.data.TabataViewModel
import ppo.tabata.data.TabataViewModelFactory
import ppo.tabata.databinding.FragmentFirstBinding
import ppo.tabata.utility.TabataApp
import ppo.tabata.utility.TabataListAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding;

    private val tabataViewModel: TabataViewModel by viewModels {
        TabataViewModelFactory((activity?.application as TabataApp).repository)
    }
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        binding = FragmentFirstBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TabataListAdapter(mContext)
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