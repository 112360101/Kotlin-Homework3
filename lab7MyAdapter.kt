package com.example.lab7

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes

class MyAdapter(
    context: Context,
    data: List<Item>,
    @LayoutRes private val layout: Int
) : ArrayAdapter<Item>(context, layout, data) {

    // 使用 ViewHolder 快取子元件參考，避免每次滑動都重複呼叫 findViewById()
    private class ViewHolder(view: View) {
        val imgPhoto: ImageView = view.findViewById(R.id.imgPhoto)
        val tvMsg: TextView = view.findViewById(R.id.tvMsg)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        // 1. 重複利用已滑出螢幕的 convertView，減少重複載入 XML 佈局的效能損耗
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(layout, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        // 2. 取得對應位置資料並綁定畫面
        val item = getItem(position) ?: return view
        holder.imgPhoto.setImageResource(item.photo)
        holder.tvMsg.text = if (layout == R.layout.adapter_vertical) {
            item.name
        } else {
            "${item.name}: ${item.price}元"
        }

        return view
    }
}
