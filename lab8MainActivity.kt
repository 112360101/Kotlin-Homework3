package com.example.lab8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: MyAdapter
    private val contacts = mutableListOf<Contact>()

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data ?: return@registerForActivityResult
            val name = intent.getStringExtra("name").orEmpty()
            val phone = intent.getStringExtra("phone").orEmpty()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                contacts.add(Contact(name, phone))
                // 僅通知最後一筆新增，避免整份清單重繪
                myAdapter.notifyItemInserted(contacts.size - 1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        myAdapter = MyAdapter(contacts)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity) // 預設即為 VERTICAL
            adapter = myAdapter
            setHasFixedSize(true) // 項目高度固定時啟用，可提升 RecyclerView 排版效能
        }

        btnAdd.setOnClickListener {
            startForResult.launch(Intent(this, SecActivity::class.java))
        }
    }
}
