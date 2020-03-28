package com.flj.latte.pay

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.alibaba.fastjson.JSON
import com.flj.latte.R
import com.flj.latte.net.RestClient
import com.flj.latte.net.callback.ISuccess
import com.flj.latte.util.log.LogUtil

/**
 * @author 傅令杰
 */
class LattePay internal constructor(
    context: Context, private val mParams: LinkedHashMap<String, Any>,
    private val mAlPayUrl: String,
    //设置支付回调监听
    private val mAlPayResultListener: IAlPayResultListener
) : View.OnClickListener {

    companion object {
        fun builder(): LattePayBuilder {
            return LattePayBuilder()
        }
    }

    private val mDialog: AlertDialog = AlertDialog.Builder(context).create()

    //自主选择支付方式
    fun select() {
        mDialog.show()
        val window = mDialog.window
        if (window != null) {
            window.setContentView(R.layout.dialog_pay_panel)
            window.setGravity(Gravity.BOTTOM)
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //设置属性
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            window.attributes = params

            window.findViewById<View>(R.id.btn_dialog_pay_alpay).setOnClickListener(this)
            window.findViewById<View>(R.id.btn_dialog_pay_wechat).setOnClickListener(this)
            window.findViewById<View>(R.id.btn_dialog_pay_cancel).setOnClickListener(this)
        }
    }

    private fun alPay() {
        //获取签名字符串
        RestClient.builder()
            .url(mAlPayUrl)
            .params(mParams)
            .success(object : ISuccess {
                override fun onSuccess(response: String) {
                    val paySign = JSON.parseObject(response).getString("result")
                    LogUtil.d("PAY_SIGN", paySign)
                    //必须是异步的调用客户端支付接口
                    val payAsyncTask = PayAsyncTask(mAlPayResultListener)
                    payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paySign)
                }
            })
            .build()
            .post()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_dialog_pay_alpay) {
            alPay()
            mDialog.cancel()
        } else if (id == R.id.btn_dialog_pay_cancel) {
            mDialog.cancel()
        }
    }
}
