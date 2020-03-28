package com.flj.latte.ui.date

import android.content.Context
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author 傅令杰
 */
class DateDialogUtil {

    private var mDateListener: IDateListener? = null

    interface IDateListener {

        fun onDateChange(date: String)
    }

    fun setDateListener(listener: IDateListener) {
        this.mDateListener = listener
    }

    fun showDialog(context: Context) {
        val ll = LinearLayoutCompat(context)
        val picker = DatePicker(context)
        val lp = LinearLayoutCompat.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        picker.layoutParams = lp

        picker.init(1990, 1, 1) { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            val format = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
            val data = format.format(calendar.time)
            mDateListener?.onDateChange(data)
        }

        ll.addView(picker)

        AlertDialog
            .Builder(context)
            .setTitle("选择日期")
            .setView(ll)
            .setPositiveButton("确定") { _, _ -> }
            .setNegativeButton("取消") { _, _ -> }
            .show()
    }

}
