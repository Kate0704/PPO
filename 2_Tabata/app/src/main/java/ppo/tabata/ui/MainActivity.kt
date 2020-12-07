package ppo.tabata.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import ppo.tabata.R
import ppo.tabata.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ColorPickerDialogListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: EditTabataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(EditTabataViewModel::class.java)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogDismissed(dialogId: Int) {}

    override fun onColorSelected(dialogId: Int, color: Int) {
        viewModel.updateColor(color)
//        val v = findViewById<Button>(R.id.select_color)
//        v.setBackgroundColor(color)
    }
}