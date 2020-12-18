package ppo.tabata.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import ppo.tabata.R
import ppo.tabata.data.TabataEntity
import ppo.tabata.databinding.ActivityTimerBinding
import ppo.tabata.viewModels.TimerViewModel

class TimerActivity : LocaleAwareCompatActivity() {

    private val binding: ActivityTimerBinding by lazy {ActivityTimerBinding.inflate(layoutInflater)}
    private val viewModel: TimerViewModel by lazy { ViewModelProvider(this).get(TimerViewModel::class.java) }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.setTabata(intent.getSerializableExtra("tabata") as TabataEntity)

        binding.runStop.setOnClickListener{
            binding.next.isEnabled = true
            binding.prev.isEnabled = true
            if (viewModel.isRunning) {
                viewModel.pause()
                binding.runStop.setImageResource(R.drawable.ic_play)
            }
            else {
                viewModel.startTimer()
                binding.runStop.setImageResource(R.drawable.ic_pause)
            }
        }
        viewModel.currentText.observe(this, Observer<String>{
            binding.currText.text = it
        })
        viewModel.prevText.observe(this, Observer<String>{
            binding.prevText.text = it
        })
        viewModel.nextText.observe(this, Observer<String>{
            binding.nextText.text = it
        })
        viewModel.timeRemainingText.observe(this, Observer<String>{
            binding.time.text = it
        })
        viewModel.timePercentRemaining.observe(this, Observer<Int>{
            binding.progressBar.progress = it
        })
        viewModel.isFinished.observe(this, Observer<Boolean>{
            if (it) {
                binding.runStop.setImageResource(R.drawable.ic_play)
                binding.runStop.isEnabled = false
                binding.runStop.isClickable = false
                binding.next.isEnabled = false
                binding.next.isClickable = false
            }
        })
        binding.next.setOnClickListener{
            binding.runStop.setImageResource(R.drawable.ic_pause)
            viewModel.rewind(0)
        }
        binding.prev.setOnClickListener{
            if (viewModel.isFinished.value == true) finish()
            else {
                binding.runStop.setImageResource(R.drawable.ic_pause)
                viewModel.rewind(2)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (viewModel.countDownTimer != null) viewModel.pause()
        finish()
    }
}