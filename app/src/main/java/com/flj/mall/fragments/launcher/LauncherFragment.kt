package com.flj.mall.fragments.launcher

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.flj.latte.fragments.LatteFragment
import com.flj.mall.R
import com.flj.mall.fragments.MainFragment

class LauncherFragment : LatteFragment() {

    private lateinit var mTvTimer: AppCompatTextView
    private lateinit var mCountDownTimer: CountDownTimer

    override fun setLayout(): Any {
        return R.layout.fragment_launcher
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
        mTvTimer = rootView.findViewById(R.id.tv_timer)
        mTvTimer.setOnClickListener {
            post {
                mCountDownTimer.cancel()
                startWithPop(MainFragment())
            }
        }
        mCountDownTimer = LauncherCountDown(this, mTvTimer)
        mCountDownTimer.start()
    }
}