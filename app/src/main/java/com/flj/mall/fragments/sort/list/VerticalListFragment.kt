package com.flj.mall.fragments.sort.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flj.latte.fragments.LatteFragment
import com.flj.latte.net.RestClient
import com.flj.latte.net.callback.ISuccess
import com.flj.mall.R
import com.flj.mall.fragments.sort.SortFragment

class VerticalListFragment : LatteFragment() {

    private lateinit var mRecyclerView: RecyclerView

    override fun setLayout(): Any {
        return R.layout.fragment_vertical_list
    }

    override fun onBindView(inflater: LayoutInflater, savedInstanceState: Bundle?, rootView: View) {
        mRecyclerView = rootView.findViewById(R.id.rv_vertical_menu_list)
        val manager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = manager
        //屏蔽动画
        mRecyclerView.itemAnimator = null
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        RestClient.builder()
            .url("sort_list.php")
            .loader(this)
            .success(object : ISuccess {

                override fun onSuccess(response: String) {
                    val data =
                        VerticalListDataConverter(response)
                            .convert()
                    val adapter =
                        SortRecyclerAdapter(
                            data, parentFragment as SortFragment
                        )
                    mRecyclerView.adapter = adapter
                }

            })
            .build()
            .get()
    }
}











