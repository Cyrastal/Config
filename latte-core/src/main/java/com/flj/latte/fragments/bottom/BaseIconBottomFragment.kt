package com.flj.latte.fragments.bottom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.flj.latte.R
import com.flj.latte.fragments.LatteFragment
import com.mikepenz.iconics.view.IconicsTextView
import me.yokeyword.fragmentation.ISupportFragment

/**
 * @author 傅令杰
 */
abstract class BaseIconBottomFragment : LatteFragment(), View.OnClickListener {

    private val mTabBean = ArrayList<BottomIconTabBean>()
    private val mItemFragments = ArrayList<DoubleClickExitFragment>()
    private val mItems =
        LinkedHashMap<BottomIconTabBean, DoubleClickExitFragment>()
    private var mCurrentFragment = 0
    //设置首页一打开展示哪个平级的Fragment
    private var mIndexFragment = 0
    private var mClickedColor = Color.RED

    private lateinit var mBottomBar: LinearLayoutCompat

    abstract fun setItems()
            : LinkedHashMap<BottomIconTabBean, DoubleClickExitFragment>

    override fun setLayout(): Any {
        return R.layout.fragment_bottom
    }

    //设置首页一打开展示哪个平级的Fragment
    abstract fun setIndexFragment(): Int

    @ColorInt
    abstract fun setClickedColor(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIndexFragment = setIndexFragment()
        if (setClickedColor() != 0) {
            mClickedColor = setClickedColor()
        }
        val items = setItems()
        mItems.putAll(items)
        for ((key, value) in mItems) {
            mTabBean.add(key)
            mItemFragments.add(value)
        }
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
        mBottomBar = rootView.findViewById(R.id.bottom_bar)
        val size = mItems.size
        for (i in 0 until size) {
            LayoutInflater.from(context)
                .inflate(R.layout.bottom_item_icon_text_layout, mBottomBar)
            val item =
                mBottomBar.getChildAt(i) as RelativeLayout

            //为了正确的辨认点击事件
            item.tag = i
            item.setOnClickListener(this)

            val itemIcon =
                item.getChildAt(0) as IconicsTextView
            val itemTitle =
                item.getChildAt(1) as AppCompatTextView
            //初始化相应的数据
            val bean = mTabBean[i]
            itemIcon.text = bean.icon
            itemTitle.text = bean.title
            if (i == mIndexFragment) {
                itemIcon.setTextColor(mClickedColor)
                itemTitle.setTextColor(mClickedColor)
            }
        }

        val fragments = mItemFragments.toTypedArray<ISupportFragment>()
        supportDelegate.loadMultipleRootFragment(
            R.id.bottom_fragment_container
            , mIndexFragment, *fragments
        )
    }

    private fun resetColor() {
        val count = mBottomBar.childCount
        for (i in 0 until count) {
            val item =
                mBottomBar.getChildAt(i) as RelativeLayout
            val itemIcon =
                item.getChildAt(0) as IconicsTextView
            val itemTitle =
                item.getChildAt(1) as AppCompatTextView
            itemIcon.setTextColor(Color.GRAY)
            itemTitle.setTextColor(Color.GRAY)
        }
    }

    private fun changeColor(tabIndex: Int) {
        resetColor()
        val item =
            mBottomBar.getChildAt(tabIndex) as RelativeLayout
        val itemIcon =
            item.getChildAt(0) as IconicsTextView
        val itemTitle =
            item.getChildAt(1) as AppCompatTextView
        itemIcon.setTextColor(mClickedColor)
        itemTitle.setTextColor(mClickedColor)
    }

    override fun onClick(v: View) {
        val tabIndex = v.tag as Int
        changeColor(tabIndex)
        //展示和隐藏内容部分的
        supportDelegate.showHideFragment(
            mItemFragments[tabIndex],
            mItemFragments[mCurrentFragment]
        )
        //先后顺序不能错
        mCurrentFragment = tabIndex
    }
}
