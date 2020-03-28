package com.flj.mall

import android.os.Bundle
import com.flj.latte.activities.ProxyActivity
import com.flj.latte.fragments.LatteFragment
import com.flj.mall.fragments.launcher.LauncherFragment

class MainActivity : ProxyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun setRootFragment(): LatteFragment {
        return LauncherFragment()
    }

}
