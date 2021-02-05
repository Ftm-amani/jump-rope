package com.example.myapplication.util

import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter



@BindingAdapter("formattedTime")
fun TextView.setFormattedTime(time: Long) {
    val sec = time / 1000
    text = if (sec < 60) sec.toString() else DateUtils.formatElapsedTime(sec)
}