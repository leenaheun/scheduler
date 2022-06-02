package com.example.scheduler

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduler.databinding.ActivityMainBinding
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter:MyAdapter
    lateinit var myDBHelper: MyDBHelper
    var data:ArrayList<MyData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        init()
    }

    private fun init(){
        binding.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL)) //줄 긋기
        adapter = MyAdapter(data)
        myDBHelper = MyDBHelper(this)
        adapter.itemClickListener = object :MyAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("일정 수정 및 삭제")
                        .setPositiveButton("수정", DialogInterface.OnClickListener { dlg, _ ->
                            val intent1 = Intent(this@MainActivity, AddScheduler::class.java)
                            intent1.putExtra("fixsch", MyData(0,0,0,data[position].title, data[position].content, data[position].starthour, data[position].startmin,
                                data[position].endhour, data[position].endmin))
                            startActivity(intent1)
                        })
                        .setNegativeButton("삭제", DialogInterface.OnClickListener { dlg, _ ->
                            myDBHelper.deleteSchedule(data[position])
                            adapter.removeItem(position)
                        })
                    val dlg = builder.create()
                    dlg.show()
            }
        }
        binding.recyclerView.adapter = adapter
        binding.plusbutton.setOnClickListener {
            val intent = Intent(this, AddScheduler::class.java)
            startActivity(intent)
        }
    }

    private fun initData(){
        val dbfile = getDatabasePath("mysch.db")
        if(!dbfile.parentFile.exists()){
            dbfile.parentFile.mkdir()
        }
        if(!dbfile.exists()){
            val file = resources.openRawResource(R.raw.mysch)
            val fillSize = file.available()
            val buffer = ByteArray(fillSize)
            file.read(buffer)
            file.close()
            dbfile.createNewFile()
            val ouput = FileOutputStream(dbfile)
            ouput.write(buffer)
            ouput.close()
        }
        myDBHelper = MyDBHelper(this)
        //날짜 받기
        data.addAll(myDBHelper.showschedule(0,0,0))
        if(intent.hasExtra("newsch")) {
            val newdata = intent.getSerializableExtra("newsch") as MyData
            myDBHelper.insertSchedule(newdata)
            //adapter.addItem(newdata)
        }

    }
}