package ppo.tabata.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import ppo.tabata.R
import ppo.tabata.databinding.ActivityMainBinding
import java.util.*

class MainActivity : LocaleAwareCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: EditTabataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(EditTabataViewModel::class.java)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
        setTitle(R.string.app_name)

        val prefs = getDefaultSharedPreferences(this)
        val scale = prefs.getFloat("size_coef", 1F)
        resources.configuration.fontScale = scale
        val metrics = resources.displayMetrics
        metrics.scaledDensity = resources.configuration.fontScale * metrics.density
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
        return super.onOptionsItemSelected(item)
    }
}