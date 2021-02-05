package com.example.myapplication.ui

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import kotlinx.coroutines.launch

class HomeViewModel(val app: Application) : AndroidViewModel(app) {

    private val minute: Long = 60_000L
    private val second: Long = 1_000L

    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val notifyIntent = Intent(app, AlarmReceiver::class.java)
    private val notifyPendingIntent: PendingIntent

    private val TRIGGER_TIME = "tigger_time"
    val timerLengthOptions: IntArray
    private lateinit var timer: CountDownTimer
    private var sharedPrefs = app.getSharedPreferences("TanabZani", Context.MODE_PRIVATE)
    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    private val _timeSelection = MutableLiveData<Int>()
    val timeSelection: LiveData<Int>
        get() = _timeSelection

    private var _alarmOn = MutableLiveData<Boolean>(false)
    val alarmOn: LiveData<Boolean>
        get() = _alarmOn

    init {
        _alarmOn.value = PendingIntent.getBroadcast(app,1,notifyIntent,PendingIntent.FLAG_NO_CREATE) != null
        timerLengthOptions = app.resources.getIntArray(R.array.spinner_item_time)

        notifyPendingIntent =
            PendingIntent.getBroadcast(app, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (alarmOn.value == true) {createTimer()}
    }

    fun createTimer() {
        viewModelScope.launch {

            val triggerTime = loadTimer()

            timer = object : CountDownTimer(triggerTime, second) {
                override fun onFinish() {
                    resetTimer()
                }

                override fun onTick(millisUntilFinished: Long) {
                    _elapsedTime.value = triggerTime - SystemClock.elapsedRealtime()
                    if (_elapsedTime.value ?: 0 < 0) {
                        resetTimer()
                    }
                }

            }

            timer.start()

        }
    }

    fun setAlarm(isOn: Boolean) {
        if (isOn) {
            startTimer()
        } else {
            cancelNotification()
        }
    }

    private fun cancelNotification() {
        resetTimer()
    }

    private fun startTimer() {
        if (_alarmOn.value == false) {
            _alarmOn.value = true

            val selectedInterval = if (timeSelection.value == 0) {
                10 * second
            } else {
                timerLengthOptions[timeSelection.value!!] * minute
            }

            val triggerTime = SystemClock.elapsedRealtime() + selectedInterval


            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                notifyPendingIntent
                )

            saveTimer(triggerTime)

        }
        createTimer()
    }

    fun setTimeSelection(selected: Int) {
        _timeSelection.value = selected
    }

    private fun resetTimer() {
        _elapsedTime.value = 0
        timer.cancel()
        _alarmOn.value = false
        alarmManager.cancel(notifyPendingIntent)
    }

    private fun saveTimer(time: Long) {
        sharedPrefs.edit().putLong(TRIGGER_TIME, time).apply()
    }

    private fun loadTimer(): Long {
        return sharedPrefs.getLong(TRIGGER_TIME, 0)
    }
}