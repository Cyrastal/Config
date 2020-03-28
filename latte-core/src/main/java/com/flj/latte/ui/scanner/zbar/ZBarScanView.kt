package com.flj.latte.ui.scanner.zbar

import android.content.Context
import android.util.AttributeSet
import com.flj.latte.ui.scanner.LatteViewFinderView

import me.dm7.barcodescanner.core.IViewFinder
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * @author 傅令杰
 */
class ZBarScanView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) :
    ZXingScannerView(context, attributeSet) {

    override fun createViewFinderView(context: Context): IViewFinder {
        return LatteViewFinderView(context)
    }
}
