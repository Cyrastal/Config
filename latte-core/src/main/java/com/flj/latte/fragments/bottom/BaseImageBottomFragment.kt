package com.flj.latte.fragments.bottom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.flj.latte.R
import com.flj.latte.fragments.LatteFragment
import me.yokeyword.fragmentation.ISupportFragment
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author 傅令杰
 */
abstract class BaseImageBottomFragment : LatteFragment(), View.OnClickListener {

    private val mTabBeans = ArrayList<BottomImageTabBean>()
    private val mItemFragments = ArrayList<DoubleClickExitFragment>()
    private val mItems = LinkedHashMap<BottomImageTabBean, DoubleClickExitFragment>()
    private var mCurrentDelegate = 0
    private var mIndexDelegate = 0

    private lateinit var mBottomBar: LinearLayoutCompat

    abstract fun setItems(): LinkedHashMap<BottomImageTabBean, DoubleClickExitFragment>

    override fun setLayout(): Any {
        return R.layout.fragment_bottom
    }

    abstract fun setIndexFragment(): Int

    abstract fun setTextClickedColor(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIndexDelegate = setIndexFragment()

        val items = setItems()
        mItems.putAll(items)
        for ((key, value) in mItems) {
            mTabBeans.add(key)
            mItemFragments.add(value)
        }
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
        mBottomBar = rootView.findViewById(R.id.bottom_bar)
        val size = mItems.size
        for (i in 0 until size) {
            inflater.inflate(R.layout.bottom_item_image_text_layout, mBottomBar)
            val item = mBottomBar.getChildAt(i) as RelativeLayout
            //设置每个item的点击事件
            item.tag = i
            item.setOnClickListener(this)
            item.post {
                val itemImage = item.getChildAt(0) as AppCompatImageView
                val itemTitle = item.getChildAt(1) as AppCompatTextView
                //初始化数据
                val bean = mTabBeans[i]
                itemTitle.text = bean.title
                itemImage.setImageResource(bean.imageNormal)

                if (i == mIndexDelegate) {
                    itemImage.setImageResource(bean.imageFocus)
                    itemTitle.setTextColor(setTextClickedColor())
                }
            }
        }
        val fragments = mItemFragments.toTypedArray<ISupportFragment>()
        supportDelegate.loadMultipleRootFragment(
            R.id.bottom_fragment_container,
            mIndexDelegate,
            *fragments
        )
    }

    private fun resetImage() {
        val count = mBottomBar.childCount
        for (i in 0 until count) {
            val item = mBottomBar.getChildAt(i) as RelativeLayout
            val itemImage = item.getChildAt(0) as AppCompatImageView
            val itemTitle = item.getChildAt(1) as AppCompatTextView
            itemTitle.setTextColor(Color.GRAY)

            val bean = mTabBeans[i]
            itemImage.setImageResource(bean.imageNormal)
        }
    }

    private fun changeImage(tabIndex: Int) {
        resetImage()
        val item = mBottomBar.getChildAt(tabIndex) as RelativeLayout
        val itemImage = item.getChildAt(0) as AppCompatImageView
        val itemTitle = item.getChildAt(1) as AppCompatTextView
        itemTitle.setTextColor(setTextClickedColor())

        val bean = mTabBeans[tabIndex]
        itemImage.setImageResource(bean.imageFocus)
    }

    override fun onClick(v: View) {
        val tabIndex = v.tag as Int
        changeImage(tabIndex)
        supportDelegate.showHideFragment(mItemFragments[tabIndex], mItemFragments[mCurrentDelegate])
        //注意先后顺序
        mCurrentDelegate = tabIndex
    }
}
