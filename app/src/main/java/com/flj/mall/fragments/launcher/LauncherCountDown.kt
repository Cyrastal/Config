package com.flj.mall.fragments.launcher

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.appcompat.widget.AppCompatTextView
import com.flj.mall.fragments.MainFragment

class LauncherCountDown(
    private val fragment: LauncherFragment,
    private val tvTimer: AppCompatTextView
) :
    CountDownTimer(6 * 1000, 1000) {

    override fun onFinish() {
        fragment.post {
            fragment.startWithPop(MainFragment())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onTick(time: Long) {
        fragment.post {
            tvTimer.text = "跳过 ${(time / 1000)}"
        }
    }
}