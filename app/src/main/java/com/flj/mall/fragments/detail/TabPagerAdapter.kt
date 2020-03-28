package com.flj.mall.fragments.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class TabPagerAdapter(fm: FragmentManager, data: com.alibaba.fastjson.JSONObject) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mTabItems = ArrayList<String>()
    private val mPictures = ArrayList<ArrayList<String>>()

    init {
        val tabs = data.getJSONArray("tabs")
        val size = tabs.size
        for (i in 0 until size) {
            val eachTab = tabs.getJSONObject(i)
            val name = eachTab.getString("name")
            val pictureUrls = eachTab.getJSONArray("pictures")
            val eachTabPictureArray = ArrayList<String>()
            //存储每一个图片
            val pictureSize = pictureUrls.size
            for (j in 0 until pictureSize) {
                eachTabPictureArray.add(pictureUrls.getString(j))
            }
            mTabItems.add(name)
            mPictures.add(eachTabPictureArray)

        }
    }

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return ImageFragment.create(mPictures[0])
        } else if (position == 1) {
            return ImageFragment.create(mPictures[1])
        }
        return ImageFragment.create(mPictures[1])
    }

    override fun getCount(): Int {
        return mTabItems.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTabItems[position]
    }
}