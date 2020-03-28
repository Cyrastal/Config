package com.flj.latte.wechat.templates

import com.flj.latte.wechat.BaseWXEntryActivity
import com.flj.latte.wechat.LatteWeChat

class WXEntryTemplate : BaseWXEntryActivity() {

    override fun onSignInSuccess(userInfo: String?) {
        userInfo?.let { LatteWeChat.instance.signInCallback?.onSignInSuccess(it) }
    }

    override fun onResume() {
        super.onResume()
        finish()
        overridePendingTransition(0, 0)
    }

}