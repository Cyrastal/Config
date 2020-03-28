package com.flj.latte.fragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.FileUtils
import com.flj.latte.R
import com.flj.latte.global.Latte
import com.flj.latte.ui.camera.CameraConstants
import com.flj.latte.ui.camera.CameraImageBean
import com.flj.latte.ui.camera.LatteCamera
import com.flj.latte.ui.scanner.ScannerEngines
import com.flj.latte.ui.scanner.zbar.ZBarScannerFragment
import com.flj.latte.ui.scanner.zxing.ZXingScannerFragment
import com.flj.latte.util.callback.CallbackManager
import com.flj.latte.util.callback.CallbackType
import com.flj.latte.util.callback.IGlobalCallback
import com.flj.latte.util.log.LogUtil
import com.yalantis.ucrop.UCrop
import me.yokeyword.fragmentation.ISupportFragment
import permissions.dispatcher.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*

/**
 * @author 傅令杰
 */
@RuntimePermissions
abstract class LatteFragment : BaseFragment() {

    private var mTouchTime: Long = 0

    companion object {
        // 再点一次退出程序时间设置
        private const val WAIT_TIME = 2000L
    }

    protected fun exitWithDoubleClick(message: String): Boolean {
        if (System.currentTimeMillis() - mTouchTime < WAIT_TIME) {
            _mActivity.finish()
        } else {
            mTouchTime = System.currentTimeMillis()
            Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show()
        }
        return true
    }

    fun <T : LatteFragment> getParent(): T {
        @Suppress("UNCHECKED_CAST")
        return parentFragment as T
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun requestFilePermission() {
    }

    //不是直接调用方法
    @NeedsPermission(Manifest.permission.CAMERA)
    internal fun startCamera() {
        LatteCamera.start(this)
    }

    //这个是真正调用的方法
    fun startCameraWithFileCheck() {
        //先请求一次文件权限
        requestFilePermissionWithPermissionCheck()
        startCameraWithPermissionCheck()
    }

    //这个是真正调用的方法
    fun startScanWithFileCheck() {
        //先请求一次文件权限
        requestFilePermissionWithPermissionCheck()
        startScanQrCodeWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    internal fun startScanQrCode() {
        when (Latte.instance.getConfiguration<ScannerEngines>(R.id.key_scanner_engine)) {
            ScannerEngines.ZXING -> supportDelegate.startForResult(
                ZXingScannerFragment(),
                R.id.request_scan
            )
            ScannerEngines.ZBAR -> supportDelegate.startForResult(
                ZBarScannerFragment(),
                R.id.request_scan
            )
        }
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    internal fun showRationaleForCamera(request: PermissionRequest) {
        context?.let {
            AlertDialog.Builder(it)
                .setPositiveButton("同意使用") { _, _ -> request.proceed() }
                .setNegativeButton("拒绝使用") { _, _ -> request.cancel() }
                .setCancelable(false)
                .setMessage("权限管理")
                .show()
        }
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    internal fun showDeniedForCamera() {
        Toast.makeText(context, "拒绝相机使用权限", Toast.LENGTH_LONG).show()
    }

    //记住选项
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    internal fun showNeverAskForCamera() {
        Toast.makeText(context, "记住选择", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ISupportFragment.RESULT_OK) {
            when (requestCode) {
                R.id.request_take_photo -> {
                    val resultUri = CameraImageBean.instance.path
                    context?.let {
                        UCrop.of(resultUri, Uri.fromFile(CameraConstants.CROPPED_FILE))
                            //A4 X 8
                            //                                .withMaxResultSize(1680, 2376)
                            .start(it, this)
                    }
                }

                R.id.request_pick_photo -> if (data != null) {
                    val pickPath = data.data
                    //从相册选择后需要有个路径存放剪裁过的图片
                    if (pickPath != null) {
                        context?.let {
                            UCrop.of(pickPath, Uri.fromFile(CameraConstants.CROPPED_FILE))
                                //                                    .withMaxResultSize(1680, 2376)
                                .start(it, this)
                        }
                    }
                }
                UCrop.REQUEST_CROP -> {
                    val cropUri = data?.let { UCrop.getOutput(it) }
                    //拿到剪裁后的数据进行处理
                    @Suppress("UNCHECKED_CAST")
                    val callback = CallbackManager
                        .instance
                        .getCallback(CallbackType.ON_CROP) as IGlobalCallback<File>

                    Luban.with(context)
                        .load(cropUri)
                        //不存储压缩过的图片
                        .setTargetDir(null)
                        //设置50kb不需要压缩
                        .ignoreBy(50)
                        .filter { path ->
                            !(TextUtils.isEmpty(path) || path.toLowerCase(Locale.ENGLISH).endsWith(
                                ".gif"
                            ))
                        }
                        .setCompressListener(object : OnCompressListener {
                            override fun onStart() {
                                LogUtil.d("LatteFragment", "开始压缩")
                            }

                            override fun onSuccess(file: File) {
                                callback.executeCallback(file)
                                //清空存储剪裁的目录
                                FileUtils.deleteAllInDir(CameraConstants.CROPPED_ALBUM)
                            }

                            override fun onError(e: Throwable) {
                                LogUtil.e("LatteFragment", "压缩失败: $e")
                            }
                        })
                        .launch()
                }

                UCrop.RESULT_ERROR -> Toast.makeText(context, "剪裁出错", Toast.LENGTH_SHORT).show()
                else -> {
                }
            }
        }
    }
}