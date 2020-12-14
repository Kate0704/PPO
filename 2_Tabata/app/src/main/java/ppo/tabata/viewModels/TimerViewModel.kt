package ppo.tabata.viewModels

import android.app.Application
import android.content.res.Resources
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zeugmasolutions.localehelper.setCurrentLocale
import ppo.tabata.R
import ppo.tabata.data.TabataEntity
import java.util.*

class TimerViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var tabata: TabataEntity
    private var sequence = arrayListOf<String>()

    var currentText = MutableLiveData<String>("Warm-up")
    var prevText = MutableLiveData<String>("")
    var nextText = MutableLiveData<String>("Work")

    var timeRemainingText = MutableLiveData<String>("00:00")
    var timePercentRemaining: MutableLiveData<Int> = MutableLiveData(100)
    var isFinished = MutableLiveData<Boolean>(false)

    lateinit var countDownTimer: CountDownTimer
    private var timeRemaining: Long = 0
    private var timeRemainingStatic = timeRemaining
    var currIndex = 0
    private var stagesCount = 0
    private val interval: Long = 1000
    var isRunning: Boolean = false
    lateinit var res: Resources
    var sequenceText = arrayListOf<String>()
    var sequenceTime = arrayListOf<Int>()

    private val attr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
    private val soundPool = SoundPool.Builder().setAudioAttributes(attr).build()

    fun setTabata(tabata: TabataEntity, locale: Locale) {
        this.tabata = tabata
        timeRemaining = (tabata.warm_up * 1000).toLong()
        timeRemainingStatic = timeRemaining
        stagesCount = tabata.cycles * (tabata.repeats * 2 + 1)
        timeRemainingText.value = EditTabataViewModel.getTime(tabata.warm_up)
        res = getApplication<Application>().resources
        res.updateConfiguration(res.configuration, res.getDisplayMetrics())
        currentText.value = res.getString(R.string.warm_up)
        nextText.value = res.getString(R.string.work_short)
        soundPool.load(getApplication<Application>().applicationContext, R.raw.notification, 1)
    }

    fun createSequence() {
        for (i in 0 until tabata.cycles) {
            for (j in 0 until tabata.repeats) {
                sequenceText.add(res.getString(R.string.work_short))
            }
        }
    }

    fun pause() {
        isRunning = false
        countDownTimer.cancel()
    }

    private fun isCooldown(i: Int): Boolean = (i % (2 * tabata.repeats + 1) == 0)
    private fun isWork(): Boolean = (currIndex % 2 != (currIndex / (2 * tabata.repeats + 1)) % 2)


    fun startTimer() {
        isRunning = true
        countDownTimer = object : CountDownTimer(timeRemaining, interval) {

            override fun onFinish() {
                soundPool.play(1, 1F, 1F, 1, 0, 1F)
                currIndex += 1
                if (currIndex == stagesCount) {
                    currentText.value = res.getString(R.string.complete)
                    timeRemainingText.value = "00:00"
                    prevText.value = res.getString(R.string.timerCompleted)
                    timePercentRemaining.value = 100
                    isFinished.value = true
                } else {
                    when {
                        currIndex == 0 -> {
                            currentText.value = res.getString(R.string.warm_up)
                            nextText.value = res.getString(R.string.work_short)
                            prevText.value = ""
                            timeRemaining = tabata.warm_up.toLong() * 1000
                        }
                        isCooldown(currIndex) -> {
                            currentText.value = res.getString(R.string.cooldown)
                            nextText.value = res.getString(R.string.work_short)
                            prevText.value = res.getString(R.string.rest)
                            timeRemaining = tabata.cooldown.toLong() * 1000
                        }
                        isWork() -> {
                            currentText.value = res.getString(R.string.work_short)
                            prevText.value = if (currIndex == 1) res.getString(R.string.warm_up)
                                             else res.getString(R.string.work_short)
                            nextText.value = res.getString(R.string.rest)
                            timeRemaining = tabata.work.toLong() * 1000
                        }
                        else -> {
                            currentText.value = res.getString(R.string.rest)
                            when {
                                currIndex == stagesCount - 1 -> nextText.value = ""
                                isCooldown(currIndex + 1) -> nextText.value = res.getString(R.string.cooldown)
                                else -> nextText.value = res.getString(R.string.work_short)
                            }
                            prevText.value = res.getString(R.string.work_short)
                            timeRemaining = tabata.rest.toLong() * 1000
                        }
                    }
                    timeRemainingStatic = timeRemaining
                    timeRemainingText.value = EditTabataViewModel.getTime(timeRemaining.toInt() / 1000)
                    timePercentRemaining.value = 100
                    startTimer()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                timeRemainingText.value = EditTabataViewModel.getTime(timeRemaining.toInt() / 1000)
                timePercentRemaining.value = ((timeRemaining * 100) / timeRemainingStatic).toInt()
                timeRemaining -= interval
            }

        }.start()
    }
}