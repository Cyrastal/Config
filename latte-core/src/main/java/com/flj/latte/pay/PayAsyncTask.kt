package com.flj.latte.pay

import android.app.Activity
import android.os.AsyncTask

import com.alipay.sdk.app.PayTask
import com.flj.latte.R
import com.flj.latte.global.Latte
import com.flj.latte.ui.loader.LatteLoader
import com.flj.latte.util.log.LogUtil

/**
 * @author 傅令杰
 */
class PayAsyncTask internal constructor(private val LISTENER: IAlPayResultListener?) :
    AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String): String {
        val alPaySign = params[0]
        val activity = Latte.instance.getConfiguration<Activity>(R.id.key_activity)
        val payTask = PayTask(activity)
        return payTask.pay(alPaySign, true)
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        LatteLoader.instance.stopLoading()
        val payResult = PayResult(result)
        // 支付宝返回此次支付结构及加签，建议对支付宝签名信息拿签约是支付宝提供的公钥做验签
        val resultInfo = payResult.result
        val resultStatus = payResult.resultStatus
        if (resultInfo != null) {
            LogUtil.d("AL_PAY_RESULT", resultInfo)
        }
        if (resultStatus != null) {
            LogUtil.d("AL_PAY_RESULT", resultStatus)
        }

        when (resultStatus) {
            AL_PAY_STATUS_SUCCESS -> LISTENER?.onAlPaySuccess()
            AL_PAY_STATUS_FAIL -> LISTENER?.onAlPayFail()
            AL_PAY_STATUS_PAYING -> LISTENER?.onAlPaying()
            AL_PAY_STATUS_CANCEL -> LISTENER?.onAlPayCancel()
            AL_PAY_STATUS_CONNECT_ERROR -> LISTENER?.onAlPayConnectError()
            else -> {
            }
        }
    }

    companion object {

        //订单支付成功
        private const val AL_PAY_STATUS_SUCCESS = "9000"
        //订单处理中
        private const val AL_PAY_STATUS_PAYING = "8000"
        //订单支付失败
        private const val AL_PAY_STATUS_FAIL = "4000"
        //用户取消
        private const val AL_PAY_STATUS_CANCEL = "6001"
        //支付网络错误
        private const val AL_PAY_STATUS_CONNECT_ERROR = "6002"
    }
}
