package com.example.lab7

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val gridView = findViewById<GridView>(R.id.gridView)
        val listView = findViewById<ListView>(R.id.listView)

        val countList = mutableListOf<String>()
        val itemList = mutableListOf<Item>()
        val priceRange = 10..100 // 使用 Kotlin 區間運算子取代 IntRange(10, 100)

        // 使用 try-finally 確保 TypedArray 在任何情況下都會執行 recycle() 釋放記憶體
        val imageArray = resources.obtainTypedArray(R.array.image_list)
        try {
            for (index in 0 until imageArray.length()) {
                val photoId = imageArray.getResourceId(index, 0)
                val name = "水果${index + 1}"
                val price = priceRange.random()

                countList.add("${index + 1}個")
                itemList.add(Item(photoId, name, price))
            }
        } finally {
            imageArray.recycle()
        }

        // 透過 apply 作用域函式集中設定各元件屬性，提升可讀性
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            countList
        )

        gridView.apply {
            numColumns = 3
            adapter = MyAdapter(this@MainActivity, itemList, R.layout.adapter_vertical)
        }

        listView.adapter = MyAdapter(this, itemList, R.layout.adapter_horizontal)
    }
}
