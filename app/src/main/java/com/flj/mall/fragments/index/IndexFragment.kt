package com.flj.mall.fragments.index

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flj.latte.fragments.bottom.DoubleClickExitFragment
import com.flj.latte.net.RestClient
import com.flj.latte.net.callback.IError
import com.flj.latte.net.callback.IFailure
import com.flj.latte.net.callback.ISuccess
import com.flj.latte.ui.recycler.divider.BaseDecoration
import com.flj.latte.util.log.LogUtil
import com.flj.mall.R
import com.flj.mall.fragments.MainFragment

class IndexFragment : DoubleClickExitFragment() {

    private lateinit var mRecyclerView: RecyclerView

    override fun setLayout(): Any {
        return R.layout.fragment_index
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
        mRecyclerView = rootView.findViewById(R.id.rv_index)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //让Toolbar初始时是透明的
        val toolbar = view.findViewById<Toolbar>(R.id.tb_index)
        toolbar.background.alpha = 0
    }

    //就是惰性的加载数据和UI
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initRecyclerView()
        initData()
    }

    private fun initRecyclerView() {
        val manager = GridLayoutManager(context, 4)
        mRecyclerView.layoutManager = manager
        mRecyclerView.addItemDecoration(
            BaseDecoration.create(Color.LTGRAY, 5)
        )
    }

    private fun initData() {
        RestClient.builder()
            .url("index.php")
            .loader(this)
            .success(object : ISuccess {
                override fun onSuccess(response: String) {
                    LogUtil.d("INDEX", response)
                    val adapter = IndexRecyclerAdapter(IndexDataConverter(response).convert())
                    mRecyclerView.adapter = adapter
                    val mallBottom: MainFragment = parentFragment as MainFragment
                    mRecyclerView.addOnItemTouchListener(IndexItemClickListener.create(mallBottom))
                }
            })
            .failure(object : IFailure {
                override fun onFailure() {
                    LogUtil.d("INDEX", "onFailure")
                }

            })
            .error(object : IError {
                override fun onError(code: Int, message: String) {
                    LogUtil.d("INDEX", code)
                }

            })
            .build()
            .get()
    }
}











