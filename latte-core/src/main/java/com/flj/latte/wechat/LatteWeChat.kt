package com.flj.latte.wechat

import android.app.Activity
import androidx.annotation.Nullable
import com.flj.latte.R
import com.flj.latte.global.Latte
import com.flj.latte.wechat.callbacks.IWeChatSignInCallback
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * @author 傅令杰
 */
class LatteWeChat private constructor() {

    private val mWXAPI: IWXAPI

    companion object {
        val APP_ID: String =
            Latte.instance.getConfiguration(R.id.key_we_chat_app_id)
        val APP_SECRET: String =
            Latte.instance.getConfiguration(R.id.key_we_chat_app_secret)

        val instance: LatteWeChat
            get() = Holder.instance
    }

    init {
        val activity: Activity = Latte.instance.getConfiguration(R.id.key_activity)
        mWXAPI = WXAPIFactory.createWXAPI(activity, APP_ID, true)
        mWXAPI.registerApp(APP_ID)
    }

    internal fun getWXAPI(): IWXAPI {
        return mWXAPI
    }

    var signInCallback: IWeChatSignInCallback? = null
        private set

    private object Holder {
        internal val instance = LatteWeChat()
    }

    fun onSignSuccess(@Nullable callback: IWeChatSignInCallback?): LatteWeChat {
        signInCallback = callback
        return this
    }

    fun signIn() {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "random_state"
        mWXAPI.sendReq(req)
    }
}