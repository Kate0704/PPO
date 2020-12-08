package ppo.tabata.ui

import android.R.attr.button
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
//import com.jaredrummler.android.colorpicker.ColorPickerDialog
//import com.jaredrummler.android.colorpicker.ColorShape
import ppo.tabata.R
import ppo.tabata.databinding.FragmentTabataEditBinding


class TabataEditFragment : Fragment(){

    private val binding by lazy { FragmentTabataEditBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<EditTabataViewModel>()
    private var selectedColor: Int = 2082305

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
//            val newTabata =
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.selectColor.setOnClickListener { createColorPickerDialog() }


    }

    private fun createColorPickerDialog(){
        context?.let {
            MaterialColorPickerDialog
                .Builder(it)
                .setColorShape(ColorShape.SQAURE)
                .setColorSwatch(ColorSwatch._700)
                .setDefaultColor(R.color.green_700)
                .setColorListener { color, _ ->
                    binding.selectColor.setBackgroundColor(color)
                    selectedColor = color
                }
                .show()
        }
    }
}