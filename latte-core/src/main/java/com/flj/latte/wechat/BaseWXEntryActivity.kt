package com.flj.latte.wechat

import com.alibaba.fastjson.JSON
import com.flj.latte.net.RestClient
import com.flj.latte.net.callback.IError
import com.flj.latte.net.callback.IFailure
import com.flj.latte.net.callback.ISuccess
import com.flj.latte.util.log.LogUtil.d
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth

/**
 * Created by 傅令杰 on 2017/4/25
 */
abstract class BaseWXEntryActivity : BaseWXActivity() {
    //用户登录成功后回调
    protected abstract fun onSignInSuccess(userInfo: String?)

    //微信发送请求到第三方应用后的回调
    override fun onReq(baseReq: BaseReq) {}

    //第三方应用发送请求到微信后的回调
    override fun onResp(baseResp: BaseResp) {
        val code = (baseResp as SendAuth.Resp).code
        val authUrl = StringBuilder()
        authUrl
            .append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
            .append(LatteWeChat.APP_ID)
            .append("&secret=")
            .append(LatteWeChat.APP_SECRET)
            .append("&code=")
            .append(code)
            .append("&grant_type=authorization_code")
        //        LogUtil.d("authUrl", authUrl.toString());
        getAuth(authUrl.toString())
    }

    private fun getAuth(authUrl: String) {
        RestClient
            .builder()
            .url(authUrl)
            .success(object : ISuccess {
                override fun onSuccess(response: String) {
                    val authObj = JSON.parseObject(response)
                    val accessToken = authObj.getString("access_token")
                    val openId = authObj.getString("openid")
                    val userInfoUrl = StringBuilder()
                    userInfoUrl
                        .append("https://api.weixin.qq.com/sns/userinfo?access_token=")
                        .append(accessToken)
                        .append("&openid=")
                        .append(openId)
                        .append("&lang=")
                        .append("zh_CN")
                    d("userInfoUrl", userInfoUrl.toString())
                    getUserInfo(userInfoUrl.toString())
                }
            })
            .build()
            .get()
    }

    private fun getUserInfo(userInfoUrl: String) {
        RestClient
            .builder()
            .url(userInfoUrl)
            .success(object : ISuccess {
                override fun onSuccess(response: String) {
                    onSignInSuccess(response)
                }
            })
            .failure(object : IFailure {
                override fun onFailure() {}
            })
            .error(object : IError {
                override fun onError(code: Int, msg: String) {
                    d(this@BaseWXEntryActivity::getLocalClassName.toString(), msg)
                }
            })
            .build()
            .get()
    }
}