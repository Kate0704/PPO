package ppo.tabata.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import ppo.tabata.R
import ppo.tabata.databinding.FragmentTabataEditBinding


class TabataEditFragment : Fragment() {

    private lateinit var binding: FragmentTabataEditBinding

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
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

//        binding.selectColor.setOnClickListener{
//            private fun createColorPickerDialog(id : Int){
//                ColorPickerDialog.newBuilder()
//                    .setColor(Color.RED)
//                    .setDialogType(ColorPickerDialog.TYPE_PRESETS)
//                    .setAllowCustom(true)
//                    .setAllowPresets(true)
//                    .setColorShape(ColorShape.SQUARE)
//                    .setDialogId(id)
//                    .show(context as? FragmentActivity)
//            }
//        }
    }
}