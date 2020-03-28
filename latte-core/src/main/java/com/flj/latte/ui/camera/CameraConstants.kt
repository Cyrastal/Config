package com.flj.latte.ui.camera

import android.os.Environment
import com.flj.latte.R
import com.flj.latte.global.Latte
import com.flj.latte.util.file.FileUtil
import java.io.File

/**
 * @author 傅令杰
 */
object CameraConstants {

    //系统相机目录
    private val SYSTEM_ALBUM_DIR =
        Latte.instance.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DCIM)

    //创建一个和当前应用同名的相册
    private val APP_NAME = Latte.instance.applicationContext.getString(R.string.app_name)

    val ORIGINAL_ALBUM = File(
        SYSTEM_ALBUM_DIR?.absolutePath
                + "/" + APP_NAME + "/"
    )
    val ORIGINAL_FILE = File(
        ORIGINAL_ALBUM.absolutePath,
        FileUtil.getFileNameByTime("IMG", "jpg")
    )

    //存放剪裁后的临时文件
    val CROPPED_ALBUM: File = FileUtil.createDir("cropped_tmp")
    val CROPPED_FILE = File(
        CROPPED_ALBUM.absolutePath,
        FileUtil.getFileNameByTime("IMG", "jpg")
    )
}
