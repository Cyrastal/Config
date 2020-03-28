package com.flj.mall.fragments.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.flj.latte.fragments.bottom.DoubleClickExitFragment
import com.flj.mall.R

class PersonalFragment : DoubleClickExitFragment() {

    override fun setLayout(): Any {
        return R.layout.fragment_personal
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
    }
}