package com.flj.latte.web

import android.webkit.JavascriptInterface
import com.alibaba.fastjson.JSON
import com.flj.latte.util.log.LogUtil
import com.flj.latte.web.event.EventManager

/**
 * @author 傅令杰
 */
internal class LatteWebInterface private constructor(private val container: IWebContainer) {

    companion object {
        fun create(container: IWebContainer): LatteWebInterface {
            return LatteWebInterface(container)
        }
    }

    @JavascriptInterface
    fun event(params: String?): String? {
        val action = JSON.parseObject(params).getString("action")
        val event = EventManager.instance.getEvent(action)
        LogUtil.json("WEB_EVENT", params)
        event.setContainer(container)
        return event.execute(params)
    }
}