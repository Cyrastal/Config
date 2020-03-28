package com.flj.mall

import android.app.Application
import com.flj.latte.global.Latte
import com.flj.latte.net.interceptors.EmptyInterceptor
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Latte.instance.init(this)
            //假装网络延迟，方便观察loading
            .withLoaderDelayed(0)
            .withInterceptor(EmptyInterceptor())
            //之后使用我远程部署的测试数据
            .withApiHost("http://mock.fulingjie.com/mock/api/")
            .withIconTypeface(FontAwesome)
    }
}