package ppo.tabata.ui

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ppo.tabata.R

class EditTabataViewModel: ViewModel() {

    var color = MutableLiveData<Int>()

    fun currentColor() = color
    fun updateColor(value: Int) { color.value = value }
}