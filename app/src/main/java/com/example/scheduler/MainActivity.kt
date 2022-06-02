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
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter:MyAdapter
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
        adapter.itemClickListener = object :MyAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("일정 수정 및 삭제")
                        .setPositiveButton("수정", DialogInterface.OnClickListener { dlg, _ ->
                            val intent1 = Intent(this@MainActivity, AddScheduler::class.java)
                            intent1.putExtra("fixsch", MyData(data[position].title, data[position].content, data[position].starthour, data[position].startmin,
                                data[position].endhour, data[position].endmin))
                            startActivity(intent1)
                        })
                        .setNegativeButton("삭제", DialogInterface.OnClickListener { dlg, _ ->
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
    fun readFileScan(scan:Scanner){
        while(scan.hasNextLine()){
            val title=scan.nextLine()
            val content = scan.nextLine()
            val starthour = scan.nextLine().toString().toInt()
            val startmin = scan.nextLine().toString().toInt()
            val endhour = scan.nextLine().toString().toInt()
            val endmin = scan.nextLine().toString().toInt()
            data.add(MyData(title,content, starthour, startmin,endhour,endmin))
        }
    }
    private fun initData(){

        val scan = Scanner(resources.openRawResource(R.raw.schedule))
        readFileScan(scan)
    }
}