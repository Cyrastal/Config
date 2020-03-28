package com.flj.latte.web.event

import com.flj.latte.util.log.LogUtil

/**
 * @author 傅令杰
 */
class UndefineEvent : Event() {
    override fun execute(params: String?): String? {
        LogUtil.e("UndefineEvent", params)
        return null
    }
}
