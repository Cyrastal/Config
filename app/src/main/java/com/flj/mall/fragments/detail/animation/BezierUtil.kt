package com.flj.mall.fragments.detail.animation

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import android.widget.LinearLayout
import com.flj.mall.R


object BezierUtil {

    internal fun startAnimation(
        v: View, fromXDelta: Int,
        fromYDelta: Int, fx: Int, fy: Int, mx: Int, my: Int, tx: Int,
        ty: Int, listener: AnimationListener?
    ) {
        val set = AnimationSet(false)
        val translateAnimation1 =
            TranslateAnimation(
                fromXDelta.toFloat(),
                (mx - fx).toFloat(),
                fromYDelta.toFloat(),
                (my - fy).toFloat()
            )
        translateAnimation1.interpolator = DecelerateInterpolator()
        translateAnimation1.repeatCount = 0
        translateAnimation1.fillAfter = false
        set.addAnimation(translateAnimation1)

        val translateAnimation2 =
            TranslateAnimation(
                fromXDelta.toFloat(),
                (tx - mx).toFloat(),
                fromYDelta.toFloat(),
                (ty - my).toFloat()
            )
        translateAnimation2.interpolator = AccelerateInterpolator()
        translateAnimation2.repeatCount = 0
        translateAnimation2.fillAfter = false
        set.addAnimation(translateAnimation2)
        set.duration = 700
        set.fillAfter = false
        set.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                v.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                v.visibility = View.GONE
                listener?.onAnimationEnd()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        v.startAnimation(set)
    }

    internal fun createAnimLayout(activity: Activity): ViewGroup {
        val rootView = activity.window.decorView as ViewGroup
        val animLayout = LinearLayout(activity)
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        animLayout.layoutParams = lp
        animLayout.id = Integer.MAX_VALUE - 1
        animLayout.setBackgroundResource(android.R.color.transparent)
        rootView.addView(animLayout)
        return animLayout
    }

    internal fun addViewToAnimLayout(
        context: Context,
        view: View?,
        location: IntArray,
        wrap_content: Boolean
    ): View? {
        if (view == null) return null
        val x = location[0]
        val y = location[1]
        val params: LinearLayout.LayoutParams
        if (wrap_content) {
            var drawable: Drawable? = null
            if (view is ImageView) {
                drawable = view.drawable
            }
            params = if (drawable == null) {
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            } else {
                LinearLayout.LayoutParams(drawable.intrinsicWidth, drawable.intrinsicHeight)
            }
        } else {
            val wh = context.resources.getDimensionPixelSize(R.dimen.db_goods_wh)
            params = LinearLayout.LayoutParams(wh, wh)
        }
        params.leftMargin = x
        params.topMargin = y
        view.layoutParams = params
        return view
    }

    interface AnimationListener {
        /**
         * 处理动画结束后的逻辑，不要涉及动画相关的View
         */
        fun onAnimationEnd()
    }
}
