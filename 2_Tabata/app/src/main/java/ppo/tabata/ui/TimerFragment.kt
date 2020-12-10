package ppo.tabata.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ppo.tabata.R
import ppo.tabata.databinding.FragmentTabataListBinding

class TimerFragment : Fragment() {

    private val binding: FragmentTabataListBinding by lazy { FragmentTabataListBinding.inflate(layoutInflater) }
    private val viewModel: EditTabataViewModel by activityViewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        return binding.root
    }

}