package ppo.tabata.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.NonNull
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jaredrummler.android.colorpicker.ColorShape
import ppo.tabata.R
import ppo.tabata.data.TabataEntity
import ppo.tabata.databinding.FragmentTabataEditBinding


class TabataEditFragment : Fragment(){

    private lateinit var binding: FragmentTabataEditBinding
    private lateinit var viewModel: EditTabataViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabataEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditTabataViewModel::class.java)


        binding.buttonSave.setOnClickListener {
            val newTabata = TabataEntity(binding.tabataTitle.text.toString(), viewModel.color.value!!,
            binding.wa)
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.selectColor.setOnClickListener{
            createColorPickerDialog(1)
        }
    }

    private fun createColorPickerDialog(id: Int){
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setColor(Color.GREEN)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .setDialogId(id)
                .show(context as? FragmentActivity)
    }
}