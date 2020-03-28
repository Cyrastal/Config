package com.flj.latte.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.flj.latte.R
import com.flj.latte.fragments.LatteFragment
import com.mikepenz.iconics.view.IconicsTextView
import java.util.*
import kotlin.math.max

/**
 * @author 傅令杰
 */

class AutoPhotoLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var mCurrentNum = 0
    private val mMaxNum: Int
    private val mMaxLineNum: Int
    private lateinit var mIconAdd: IconicsTextView
    private var mParams: LayoutParams? = null
    //要删除的图片ID
    private var mDeleteId = 0
    private lateinit var mTargetImageVew: AppCompatImageView
    private val mImageMargin: Int
    private var mDelegate: LatteFragment? = null
    private var mLineViews: LinkedList<View>? = null
    private var mTargetDialog: AlertDialog? = null
    private val mIconSize: Float

    private val mAllViews = LinkedList<LinkedList<View>>()
    private val mLineHeights = ArrayList<Int>()

    //防止多次的测量和布局过程
    private var mIsOnceInitOnMeasure = false
    private var mHasInitOnLayout = false

    init {
        @SuppressLint("CustomViewStyleable")
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.camera_flow_layout)
        mMaxNum = typedArray.getInt(R.styleable.camera_flow_layout_max_count, 1)
        mMaxLineNum = typedArray.getInt(R.styleable.camera_flow_layout_line_count, 3)
        mImageMargin = typedArray.getInt(R.styleable.camera_flow_layout_item_margin, 0)
        mIconSize = typedArray.getDimension(R.styleable.camera_flow_layout_icon_size, 20f)
        typedArray.recycle()
    }

    fun setDelegate(delegate: LatteFragment) {
        this.mDelegate = delegate
    }

    fun onCropTarget(uri: Uri) {
        createNewImageView()
        Glide.with(mDelegate!!)
            .load(uri)
            .apply(OPTIONS)
            .into(mTargetImageVew)
    }

    private fun createNewImageView() {
        mTargetImageVew = AppCompatImageView(context)
        mTargetImageVew.id = mCurrentNum
        mTargetImageVew.layoutParams = mParams
        mTargetImageVew.setOnClickListener { v ->
            //获取要删除的图片ID
            mDeleteId = v.id
            mTargetDialog!!.show()
            val window = mTargetDialog!!.window
            if (window != null) {
                window.setContentView(R.layout.dialog_image_click_panel)
                window.setGravity(Gravity.BOTTOM)
                window.setWindowAnimations(R.style.anim_panel_up_from_bottom)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val params = window.attributes
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
                params.dimAmount = 0.5f
                window.attributes = params
                window.findViewById<View>(R.id.dialog_image_clicked_btn_delete)
                    .setOnClickListener {
                        //得到要删除的图片
                        val deleteImageView = findViewById<View>(mDeleteId) as AppCompatImageView
                        //设置图片逐渐消失的动画
                        val animation = AlphaAnimation(1f, 0f)
                        animation.duration = 500
                        animation.repeatCount = 0
                        animation.fillAfter = true
                        animation.startOffset = 0
                        deleteImageView.animation = animation
                        animation.start()
                        this@AutoPhotoLayout.removeView(deleteImageView)
                        mCurrentNum -= 1
                        //当数目达到上限时隐藏添加按钮，不足时显示
                        if (mCurrentNum < mMaxNum) {
                            mIconAdd.visibility = View.VISIBLE
                        }
                        mTargetDialog!!.cancel()
                    }
                window.findViewById<View>(R.id.dialog_image_clicked_btn_undetermined)
                    .setOnClickListener { mTargetDialog!!.cancel() }
                window.findViewById<View>(R.id.dialog_image_clicked_btn_cancel)
                    .setOnClickListener { mTargetDialog!!.cancel() }
            }
        }
        //添加子View的时候传入位置
        this.addView(mTargetImageVew, mCurrentNum)
        mCurrentNum++
        //当天家数目大于mMaxNum时，自动隐藏添加按钮
        if (mCurrentNum >= mMaxNum) {
            mIconAdd.visibility = View.GONE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWith = MeasureSpec.getSize(widthMeasureSpec)
        val modeWith = MeasureSpec.getMode(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)
        //wrap_content
        var width = 0
        var height = 0
        //记录每一行的宽度与高度
        var lineWith = 0
        var lineHeight = 0
        //得到内部元素个数
        val cCount = childCount
        for (i in 0 until cCount) {
            val child = getChildAt(i)
            //测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            //的搭配LayoutParams
            val lp = child.layoutParams as MarginLayoutParams
            //子View占据的宽度
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            //子View占据的高度
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            //换行
            if (lineWith + childWidth > sizeWith - paddingLeft - paddingRight) {
                //对比得到最大宽度
                width = max(width, lineWith)
                //重置lineWidth
                lineWith = childWidth
                height += lineHeight
                lineHeight = childHeight
            } else {
                //未换行
                //叠加行宽
                lineWith += childWidth
                //得到当前最大的高度
                lineHeight = max(lineHeight, childHeight)
            }
            //最后一个子控件
            if (i == cCount - 1) {
                width = max(lineWith, width)
                //判断是否超过最大拍照限制
                height += lineHeight
            }
        }
        setMeasuredDimension(
            if (modeWith == MeasureSpec.EXACTLY) sizeWith else width + paddingLeft + paddingRight,
            if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else height + paddingTop + paddingBottom
        )
        //设置一行所有图片的宽高
        val imageSideLen = sizeWith / mMaxLineNum
        //只初始化一次
        if (!mIsOnceInitOnMeasure) {
            mParams = LayoutParams(imageSideLen, imageSideLen)
            mIsOnceInitOnMeasure = true
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mAllViews.clear()
        mLineHeights.clear()
        // 当前ViewGroup的宽度
        val width = width
        var lineWidth = 0
        var lineHeight = 0
        if (!mHasInitOnLayout) {
            mLineViews = LinkedList()
            mHasInitOnLayout = true
        }
        val cCount = childCount
        for (i in 0 until cCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as MarginLayoutParams
            val childWith = child.measuredWidth
            val childHeight = child.measuredHeight
            //如果需要换行
            if (childWith + lineWidth + lp.leftMargin + lp.rightMargin > width - paddingLeft - paddingRight) {
                //记录lineHeight
                mLineHeights.add(lineHeight)
                //记录当前一行的Views
                mLineViews?.let { mAllViews.add(it) }
                //重置宽和高
                lineWidth = 0
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin
                //重置View集合
                mLineViews!!.clear()
            }
            lineWidth += childWith + lp.leftMargin + lp.rightMargin
            lineHeight = max(lineHeight, lineHeight + lp.topMargin + lp.bottomMargin)
            mLineViews!!.add(child)
        }
        //处理最后一行
        mLineHeights.add(lineHeight)
        mLineViews?.let { mAllViews.add(it) }
        //设置子View位置
        var left = paddingLeft
        var top = paddingTop
        //行数
        val lineNum = mAllViews.size
        for (i in 0 until lineNum) {
            //当前行所有的View
            mLineViews = mAllViews[i]
            lineHeight = mLineHeights[i]
            val size = mLineViews!!.size
            for (j in 0 until size) {
                val child = mLineViews!![j]
                //判断child的状态
                if (child.visibility == View.GONE) {
                    continue
                }
                val lp = child.layoutParams as MarginLayoutParams
                //设置子View的边距
                val lc = left + lp.leftMargin
                val tc = top + lp.topMargin
                val rc = lc + child.measuredWidth - mImageMargin
                val bc = tc + child.measuredHeight
                //为子View进行布局
                child.layout(lc, tc, rc, bc)
                left += child.measuredWidth + lp.leftMargin + lp.rightMargin
            }
            left = paddingLeft
            top += lineHeight
        }
        mIconAdd.layoutParams = mParams
        mHasInitOnLayout = false
    }

    private fun initAddIcon() {
        mIconAdd = IconicsTextView(context)
        mIconAdd.text = mIconText
        mIconAdd.gravity = Gravity.CENTER
        mIconAdd.textSize = mIconSize
        mIconAdd.setBackgroundResource(R.drawable.border_text)
        mIconAdd.setOnClickListener { mDelegate!!.startCameraWithFileCheck() }
        this.addView(mIconAdd)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initAddIcon()
        mTargetDialog = AlertDialog.Builder(context).create()
    }

    companion object {
        private const val mIconText = "{fa-plus}"

        private val OPTIONS = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
    }
}
