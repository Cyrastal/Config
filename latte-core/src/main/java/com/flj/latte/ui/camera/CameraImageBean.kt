package com.flj.latte.ui.camera

import android.net.Uri

/**
 * @author 傅令杰
 */
class CameraImageBean {

    lateinit var path: Uri

    private object Holder {
        internal val instance = CameraImageBean()
    }

    companion object {

        val instance: CameraImageBean
            get() = Holder.instance
    }
}
