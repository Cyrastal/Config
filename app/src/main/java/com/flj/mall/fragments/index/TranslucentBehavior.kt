package com.flj.mall.fragments.index

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.flj.latte.global.Latte
import com.flj.mall.R
import kotlin.math.roundToInt

@Suppress("unused")
class TranslucentBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<Toolbar>(context, attrs) {

    companion object {
        //延长滑动
        private const val MORE = 100
    }

    //注意，这里一定要是类变量
    private var mOffset = 0

    //确定我们所依赖的动作载体
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: Toolbar,
        dependency: View
    ): Boolean {
        return dependency.id == R.id.rv_index
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: Toolbar,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return true
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        toolbar: Toolbar,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        val startOffset = 0
        val context =
            Latte.instance.getConfiguration<Context>(R.id.key_application_context)
        val endOffset = context.resources.getDimensionPixelOffset(
            R.dimen.header_height
        ) + MORE
        mOffset += dyConsumed
        when {
            mOffset <= startOffset -> toolbar.background.alpha = 0
            mOffset in (startOffset + 1) until endOffset -> {
                val percent =
                    (mOffset - startOffset).toFloat() / endOffset
                val alpha = (percent * 255).roundToInt()
                toolbar.background.alpha = alpha
            }
            mOffset >= endOffset -> toolbar.background
        }
    }
}