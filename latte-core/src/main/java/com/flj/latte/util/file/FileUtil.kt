package com.flj.latte.util.file

import android.content.ContentResolver
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.flj.latte.global.Latte
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 傅令杰
 */
object FileUtil {

    //格式化的模板
    public const val TIME_FORMAT = "-yyyy-MMdd-HH-mm-ss"
    private val mContext = Latte.instance.applicationContext

    private val SDCARD_DIR = Environment.getExternalStorageDirectory().path

    private fun getTimeFormatName(timeFormatHeader: String): String {
        val date = Date(System.currentTimeMillis())
        //必须要加上单引号
        val dateFormat = SimpleDateFormat("'$timeFormatHeader'$TIME_FORMAT", Locale.getDefault())
        return dateFormat.format(date)
    }

    /**
     * @param timeFormatHeader 格式化的头(除去时间部分)
     * @param extension        后缀名
     * @return 返回时间格式化后的文件名
     */
    fun getFileNameByTime(timeFormatHeader: String, extension: String): String {
        return getTimeFormatName(timeFormatHeader) + "." + extension
    }

    fun createDir(sdcardDirName: String): File {
        //拼接成SD卡中完整的dir
        val dir = "$SDCARD_DIR/$sdcardDirName/"
        val fileDir = File(dir)
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        return fileDir
    }

    private fun createFile(sdcardDirName: String, fileName: String): File {
        return File(createDir(sdcardDirName), fileName)
    }

    private fun createFileByTime(
        sdcardDirName: String,
        timeFormatHeader: String,
        extension: String
    ): File {
        val fileName = getFileNameByTime(timeFormatHeader, extension)
        return createFile(sdcardDirName, fileName)
    }

    //获取文件的MIME
    fun getMimeType(filePath: String): String? {
        val extension = getExtension(filePath)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    //获取文件的后缀名
    fun getExtension(filePath: String): String {
        var suffix = ""
        val file = File(filePath)
        val name = file.name
        val idx = name.lastIndexOf('.')
        if (idx > 0) {
            suffix = name.substring(idx + 1)
        }
        return suffix
    }

    fun writeToDisk(inputStream: InputStream, dir: String, name: String): File {
        val file = FileUtil.createFile(dir, name)
        var bis: BufferedInputStream? = null
        var fos: FileOutputStream? = null
        var bos: BufferedOutputStream? = null

        try {
            bis = BufferedInputStream(inputStream)
            fos = FileOutputStream(file)
            bos = BufferedOutputStream(fos)

            val data = ByteArray(1024 * 4)

            var count: Int
            while (true) {
                count = bis.read(data)
                if (count == -1) {
                    break
                }
                bos.write(data, 0, count)
            }

            bos.flush()
            fos.flush()


        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos?.close()
                fos?.close()
                bis?.close()
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return file
    }

    fun writeToDisk(
        inputStream: InputStream,
        dir: String,
        prefix: String,
        extension: String
    ): File {
        val file = FileUtil.createFileByTime(dir, prefix, extension)
        var bis: BufferedInputStream? = null
        var fos: FileOutputStream? = null
        var bos: BufferedOutputStream? = null

        try {
            bis = BufferedInputStream(inputStream)
            fos = FileOutputStream(file)
            bos = BufferedOutputStream(fos)

            val data = ByteArray(1024 * 4)
            var count: Int
            while (true) {
                count = bis.read(data)
                if (count == -1) {
                    break
                }
                bos.write(data, 0, count)
            }
            bos.flush()
            fos.flush()


        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos?.close()
                fos?.close()
                bis?.close()
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return file
    }

    /**
     * 读取raw目录中的文件,并返回为字符串
     */
    fun getRawFile(id: Int): String {
        val `is` = mContext.resources.openRawResource(id)
        val bis = BufferedInputStream(`is`)
        val isr = InputStreamReader(bis)
        val br = BufferedReader(isr)
        val stringBuilder = StringBuilder()
        try {
            br.lineSequence().forEach {
                stringBuilder.append(it)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                br.close()
                isr.close()
                bis.close()
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return stringBuilder.toString()
    }


    /**
     * 读取assets目录下的文件,并返回字符串
     */
    fun getAssetsFile(name: String): String? {
        var `is`: InputStream? = null
        var bis: BufferedInputStream? = null
        var isr: InputStreamReader? = null
        var br: BufferedReader? = null
        var stringBuilder: StringBuilder? = null
        val assetManager = mContext.assets
        try {
            `is` = assetManager.open(name)
            bis = BufferedInputStream(`is`)
            isr = InputStreamReader(bis)
            br = BufferedReader(isr)
            stringBuilder = StringBuilder()
            br.lineSequence().forEach {
                stringBuilder.append(it)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                br?.close()
                isr?.close()
                bis?.close()
                `is`?.close()
                assetManager.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return stringBuilder?.toString()
    }

    fun getRealFilePath( uri: Uri?): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null)
            data = uri.path
        else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = mContext.contentResolver?.query(
                uri,
                arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null
            )
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }
}
