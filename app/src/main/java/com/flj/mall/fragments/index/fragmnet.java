package com.flj.mall.fragments.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.flj.latte.fragments.bottom.DoubleClickExitFragment;
import com.flj.mall.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class fragmnet extends DoubleClickExitFragment {
    @NotNull
    @Override
    public Object setLayout() {
        return R.layout.fdbv;
    }

    @Override
    public void onBindView(@NotNull LayoutInflater inflater, @Nullable Bundle savedInstanceState, @NotNull View rootView) {

    }
}
