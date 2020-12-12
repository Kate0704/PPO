package ppo.tabata.ui

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import ppo.tabata.R
import ppo.tabata.databinding.ActivityTimerBinding
import ppo.tabata.viewModels.TimerViewModel

class TimerActivity : LocaleAwareCompatActivity() {

    private val binding: ActivityTimerBinding by lazy {ActivityTimerBinding.inflate(layoutInflater)}
    private val viewModel: TimerViewModel by lazy { ViewModelProvider(this).get(TimerViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        playPauseButton.setOnClickListener{
//            if(viewModel.isRunning()) {
//                viewModel.pause()
//                playPauseButton.text = getString(R.string.start)
//            }
//            else{
//                viewModel.start()
//                playPauseButton.text = getString(R.string.pause)
//            }
//        }
//        stopButton.setOnClickListener{
//            viewModel.stop()
//            playPauseButton.text = getString(R.string.start)
//        }
//
//        viewModel.getColorCurrent().observe(viewLifecycleOwner, Observer<String>{
//            currentTextView.setBackgroundColor(Color.parseColor(it))
//        })
//        viewModel.getColorNext().observe(viewLifecycleOwner, Observer<String>{
//            nextTextView.setBackgroundColor(Color.parseColor(it))
//        })
//        viewModel.getTextCurrent().observe(viewLifecycleOwner, Observer<String>{
//            currentTextView.text = it
//        })
//        viewModel.getTextNext().observe(viewLifecycleOwner, Observer<String>{
//            nextTextView.text = it
//        })
//        viewModel.getTimeRemaining().observe(viewLifecycleOwner, Observer<String>{
//            timeRemainig.text = it
//        })
//        viewModel.getPercentTimeRemaining().observe(viewLifecycleOwner, Observer<Int>{
//            progressBar.progress = it
//        })
    }
}