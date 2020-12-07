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
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorShape
import ppo.tabata.R
import ppo.tabata.databinding.FragmentTabataEditBinding


class TabataEditFragment : Fragment(){

    private lateinit var binding: FragmentTabataEditBinding
    private val viewModel by activityViewModels<EditTabataViewModel>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabataEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
//            val newTabata =
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.selectColor.setOnClickListener{
            createColorPickerDialog()
        }

        viewModel.currentColor().observe(viewLifecycleOwner, Observer<Int> {
            val col = it
            binding.selectColor.setBackgroundColor(col)
        })
    }

    private fun createColorPickerDialog(){
        val color = binding.selectColor.background
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setColor(viewModel.currentColor().value!!)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .show(context as? FragmentActivity)
    }
}