package com.example.lab8

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val data: MutableList<Contact>
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // 使用 inner class 讓 ViewHolder 可直接存取 data，並將點擊監聽器放在 init 中只建立一次
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvName: TextView = v.findViewById(R.id.tvName)
        val tvPhone: TextView = v.findViewById(R.id.tvPhone)
        private val imgDelete: ImageView = v.findViewById(R.id.imgDelete)

        init {
            imgDelete.setOnClickListener {
                // 使用 bindingAdapterPosition 取代已棄用的 adapterPosition，並防呆快速連點
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    data.removeAt(pos)
                    notifyItemRemoved(pos) // 僅針對被刪除的項目執行局部刷新與動畫
                }
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = data[position]
        holder.tvName.text = contact.name
        holder.tvPhone.text = contact.phone
    }
}
