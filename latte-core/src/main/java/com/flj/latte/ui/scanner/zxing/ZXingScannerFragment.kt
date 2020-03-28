package com.flj.latte.ui.scanner.zxing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.flj.latte.fragments.LatteFragment
import com.flj.latte.util.callback.CallbackManager
import com.flj.latte.util.callback.CallbackType
import com.flj.latte.util.callback.IGlobalCallback
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * @author 傅令杰
 */
class ZXingScannerFragment : LatteFragment(), ZXingScannerView.ResultHandler {

    private lateinit var mScanView: ZXingScanView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScanView.setAutoFocus(true)
        mScanView.setResultHandler(this)
    }

    override fun setLayout(): Any {
        return mScanView
    }

    override fun onBindView(
        inflater: LayoutInflater,
        savedInstanceState: Bundle?,
        rootView: View
    ) {
    }

    override fun onResume() {
        super.onResume()
        mScanView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScanView.stopCameraPreview()
        mScanView.stopCamera()
    }

    override fun handleResult(result: Result) {
        @Suppress("UNCHECKED_CAST")
        val callback = CallbackManager
            .instance
            .getCallback(CallbackType.ON_SCAN) as IGlobalCallback<String>?
        callback?.executeCallback(result.text)
        supportDelegate.pop()
    }
}
