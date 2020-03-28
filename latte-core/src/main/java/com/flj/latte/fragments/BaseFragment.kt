package com.flj.latte.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flj.latte.util.log.LogUtil
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author 傅令杰
 */
abstract class BaseFragment : SupportFragment() {

    abstract fun setLayout(): Any

    abstract fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d("FRAGMENT_NAME", this::class.java.name)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = when {
            setLayout() is Int -> inflater.inflate(setLayout() as Int, container, false)
            setLayout() is View -> setLayout() as View
            else -> throw ClassCastException("type of setLayout() must be int or View!")
        }
        onBindView(inflater, savedInstanceState, rootView)
        return rootView
    }
}