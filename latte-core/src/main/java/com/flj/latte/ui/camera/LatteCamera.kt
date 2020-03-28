package com.flj.latte.ui.camera


import com.flj.latte.fragments.LatteFragment

/**
 * @author 傅令杰
 */
object LatteCamera {

    internal fun start(fragment: LatteFragment) {
        CameraHandler(fragment).beginCameraDialog()
    }
}
