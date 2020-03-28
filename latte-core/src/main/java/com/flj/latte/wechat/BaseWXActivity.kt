package com.flj.latte.wechat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * Created by 傅令杰 on 2017/4/25
 */
abstract class BaseWXActivity : AppCompatActivity(), IWXAPIEventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //这个必须写在onCreate中
        LatteWeChat.instance.getWXAPI().handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        LatteWeChat.instance.getWXAPI().handleIntent(getIntent(), this)
    }
}