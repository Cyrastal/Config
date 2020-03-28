package com.flj.latte.ui.scanner

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet

import me.dm7.barcodescanner.core.ViewFinderView


/**
 * @author 傅令杰
 */
class LatteViewFinderView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ViewFinderView(context, attributeSet) {

    init {
        mSquareViewFinder = true
        mBorderPaint.color = Color.YELLOW
        mLaserPaint.color = Color.YELLOW
    }
}
