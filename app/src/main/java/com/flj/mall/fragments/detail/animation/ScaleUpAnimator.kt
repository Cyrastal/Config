package com.flj.mall.fragments.detail.animation

import android.animation.ObjectAnimator
import android.view.View
import com.daimajia.androidanimations.library.BaseViewAnimator

class ScaleUpAnimator : BaseViewAnimator() {

    override fun prepare(target: View?) {
        val animatorX = ObjectAnimator.ofFloat(
            target
            , "scaleX",
            0.8F, 1F, 1.4F, 1.2F, 1F
        )
        val animatorY = ObjectAnimator.ofFloat(
            target
            , "scaleY",
            0.8F, 1F, 1.4F, 1.2F, 1F
        )
        animatorAgent.playTogether(animatorX, animatorY)
    }
}