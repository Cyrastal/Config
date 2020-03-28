package com.flj.latte.fragments.bottom

import android.widget.Toast
import com.flj.latte.R
import com.flj.latte.fragments.LatteFragment
import com.flj.latte.global.Latte


/**
 * @author 傅令杰
 */
abstract class DoubleClickExitFragment : LatteFragment() {
    private var mTouchTime: Long = 0

    companion object {
        // 再点一次退出程序时间设置
        private const val mWaitTime = 2000L
    }

    override fun onBackPressedSupport(): Boolean {
        if (System.currentTimeMillis() - mTouchTime < mWaitTime) {
            _mActivity.finish()
        } else {
            mTouchTime = System.currentTimeMillis()
            Toast.makeText(
                context,
                "双击退出" + Latte.instance.applicationContext.getString(R.string.app_name),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        return true
    }
}
