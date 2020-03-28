package com.flj.latte.ui.camera

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.FileUtils
import com.flj.latte.R
import com.flj.latte.fragments.LatteFragment
import com.flj.latte.global.Latte
import com.flj.latte.util.file.FileUtil
import com.flj.latte.util.log.LogUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 傅令杰
 * 照片处理类
 */

class CameraHandler internal constructor(private val mFragment: LatteFragment) :
    View.OnClickListener {

    private val mDialog: AlertDialog? = mFragment.context?.let { AlertDialog.Builder(it).create() }
    private val mContext = Latte.instance.applicationContext

    init {
        //分别创建原始和剪裁后的相册
        if (!CameraConstants.ORIGINAL_ALBUM.exists()) {
            CameraConstants.ORIGINAL_ALBUM.mkdirs()
        }
        if (!CameraConstants.CROPPED_ALBUM.exists()) {
            CameraConstants.CROPPED_ALBUM.mkdirs()
        }
    }

    fun beginCameraDialog() {
        mDialog?.show()
        val window = mDialog?.window
        if (window != null) {
            window.setContentView(R.layout.dialog_camera_panel)
            window.setGravity(Gravity.BOTTOM)
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //设置属性
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            params.dimAmount = 0.5f
            window.attributes = params

            window.findViewById<View>(R.id.photodialog_btn_cancel).setOnClickListener(this)
            window.findViewById<View>(R.id.photodialog_btn_take).setOnClickListener(this)
            window.findViewById<View>(R.id.photodialog_btn_native).setOnClickListener(this)
        }
    }

    private fun getTmpFile(): File {
        val date = Date(System.currentTimeMillis())
        //必须要加上单引号
        val dateFormat = SimpleDateFormat("'IMG'${FileUtil.TIME_FORMAT}", Locale.getDefault())
        val imageName = dateFormat.format(date) + "." + "jpg"
        val album = File(
            "${mContext.getExternalFilesDir(Environment.DIRECTORY_DCIM)?.
                absolutePath}/${Latte.instance.applicationContext.getString(
                R.string.app_name
            )}/"
        )
        return File(album.absolutePath, imageName)
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //创建和应用名字一样的相册

        val tempFile = getTmpFile()

        LogUtil.d("TEMP_FILE", tempFile)

        //兼容7.0及以上的写法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentValues = ContentValues(1)
            contentValues.put(MediaStore.Images.Media.DATA, tempFile.absolutePath)
            val uri =
                mContext.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
            //需要讲Uri路径转化为实际路径
            val realFile = FileUtils.getFileByPath(FileUtil.getRealFilePath(uri))
            val realUri = Uri.fromFile(realFile)
            CameraImageBean.instance.path = realUri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            contentValues.clear()

        } else {
            val fileUri = Uri.fromFile(tempFile)
            CameraImageBean.instance.path = fileUri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        }
        mFragment.startActivityForResult(intent, R.id.request_take_photo)
    }

    private fun pickPhoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        mFragment.startActivityForResult(
            Intent.createChooser(intent, "选择获取图片的方式"),
            R.id.request_pick_photo
        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.photodialog_btn_cancel -> mDialog?.cancel()
            R.id.photodialog_btn_take -> {
                takePhoto()
                mDialog?.cancel()
            }
            R.id.photodialog_btn_native -> {
                pickPhoto()
                mDialog?.cancel()
            }
        }
    }
}
