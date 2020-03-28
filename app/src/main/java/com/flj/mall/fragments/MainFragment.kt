package com.flj.mall.fragments

import android.graphics.Color
import com.flj.latte.fragments.bottom.BaseIconBottomFragment
import com.flj.latte.fragments.bottom.BottomIconTabBean
import com.flj.latte.fragments.bottom.DoubleClickExitFragment
import com.flj.mall.fragments.cart.ShopCartFragment
import com.flj.mall.fragments.index.IndexFragment
import com.flj.mall.fragments.personal.PersonalFragment
import com.flj.mall.fragments.sort.SortFragment
import java.util.*

class MainFragment : BaseIconBottomFragment() {

    override fun setItems(): LinkedHashMap<BottomIconTabBean, DoubleClickExitFragment> {
        val items = LinkedHashMap<BottomIconTabBean, DoubleClickExitFragment>()
        items[BottomIconTabBean("{faw-home}", "主页")] = IndexFragment()
        items[BottomIconTabBean("{faw-sort}", "分类")] = SortFragment()
        items[BottomIconTabBean("{faw-shopping-cart}", "购物车")] = ShopCartFragment()
        items[BottomIconTabBean("{faw-user}", "我的")] = PersonalFragment()
        return items
    }

    override fun setIndexFragment(): Int {
        return 0
    }

    override fun setClickedColor(): Int {
        return Color.RED
    }
}