package com.flj.latte.ui.loader

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import com.flj.latte.R
import com.wang.avi.AVLoadingIndicatorView
import com.wang.avi.indicators.LineScaleIndicator

/**
 * @author 傅令杰
 */
class LoadingDialogFragment : DialogFragment() {

    private val mDefaultIndicator = LineScaleIndicator()
    private lateinit var mAvLoadingIndicatorView: AVLoadingIndicatorView
    private lateinit var mDialog: AppCompatDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialog = AppCompatDialog(context, R.style.loading_dialog)
        mDialog.setCancelable(false)
        mDialog.setCanceledOnTouchOutside(false)
        mAvLoadingIndicatorView =
            AVLoadingIndicatorView(ContextThemeWrapper(context, R.style.AVLoadingIndicatorView))
        mAvLoadingIndicatorView.indicator = mDefaultIndicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mAvLoadingIndicatorView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAvLoadingIndicatorView.show()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return mDialog
    }

    override fun onDestroy() {
        super.onDestroy()
        mAvLoadingIndicatorView.hide()
        mDialog.cancel()
    }
}