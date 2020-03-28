package com.flj.latte.net.download

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import com.flj.latte.global.Latte
import com.flj.latte.net.callback.IComplete
import com.flj.latte.net.callback.ISuccess
import com.flj.latte.util.file.FileUtil
import okhttp3.ResponseBody
import java.io.File

/**
 * @author 傅令杰
 */

internal class SaveFileTask(
    private val complete: IComplete?,
    private val success: ISuccess?
) : AsyncTask<Any, Void, File>() {

    override fun doInBackground(vararg params: Any): File {
        var downloadDir: String? = params[0] as String
        val body = params[2] as ResponseBody
        val name = params[3] as String
        val inputStream = body.byteStream()
        if (downloadDir == null || downloadDir == "") {
            downloadDir = "down_loads"
        }
        return FileUtil.writeToDisk(inputStream, downloadDir, name)
    }

    override fun onPostExecute(file: File) {
        super.onPostExecute(file)
        success?.onSuccess(file.path)
        complete?.onComplete()
        autoInstallApk(file)
    }

    private fun autoInstallApk(file: File) {
        if (FileUtil.getExtension(file.path) == "apk") {
            val install = Intent()
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            install.action = Intent.ACTION_VIEW
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            Latte.instance.applicationContext.startActivity(install)
        }
    }
}
