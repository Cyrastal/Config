package com.flj.mall.fragments.detail.animation

import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.ScreenUtils
import com.flj.latte.fragments.LatteFragment


object BezierAnimation {

    fun addCart(
        fragment: LatteFragment, start: View, end: View,
        target: ImageView, animationListener: BezierUtil.AnimationListener
    ) {
        /* 起点 */
        val startXY = IntArray(2)
        start.getLocationInWindow(startXY)
        startXY[0] += start.width / 2
        val fx = startXY[0]
        val fy = startXY[1]

        val animMaskLayout = fragment.activity?.let { BezierUtil.createAnimLayout(it) }
        animMaskLayout?.addView(target)
        val v = fragment.context?.let { BezierUtil.addViewToAnimLayout(it, target, startXY, true) }
            ?: return
        /* 终点 */
        val endXY = IntArray(2)
        end.getLocationInWindow(endXY)
        val tx = endXY[0] + end.width / 2 - 48
        val ty = endXY[1] + end.height / 2
        /* 中点 */
        val mx = (tx + fx) / 2
        val my = ScreenUtils.getScreenHeight() / 10
        BezierUtil.startAnimation(v, 0, 0, fx, fy, mx, my, tx, ty, animationListener)
    }

}
