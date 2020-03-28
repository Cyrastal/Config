package com.flj.latte.wechat.templates

import android.widget.Toast
import com.flj.latte.wechat.BaseWXPayEntryActivity
import com.tencent.mm.opensdk.modelbase.BaseReq

/**
 * Created by 傅令杰 on 2017/1/2
 */
class WXPayEntryTemplate : BaseWXPayEntryActivity() {

    override fun onPaySuccess() {
        Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show()
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onPayFail() {
        Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show()
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onPayCancel() {
        Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show()
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onReq(baseReq: BaseReq) {}
}