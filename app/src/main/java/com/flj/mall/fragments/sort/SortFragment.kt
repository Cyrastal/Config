package com.flj.mall.fragments.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.flj.latte.fragments.bottom.DoubleClickExitFragment
import com.flj.mall.R
import com.flj.mall.fragments.sort.content.ContentFragment
import com.flj.mall.fragments.sort.list.VerticalListFragment

class SortFragment : DoubleClickExitFragment() {

    override fun setLayout(): Any {
        return R.layout.fragment_sort
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        val listFragment = VerticalListFragment()
        //默认显示contentId是1的content
        val contentFragment = ContentFragment.newInstance(1)
        supportDelegate.loadRootFragment(
            R.id.vertical_list_container, listFragment
        )
        supportDelegate.loadRootFragment(
            R.id.sort_content_container, contentFragment
        )
    }
}