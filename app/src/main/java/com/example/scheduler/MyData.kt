package com.example.scheduler

import java.io.Serializable


data class MyData(var title:String, var content:String, var starthour:Int, var startmin:Int, var endhour:Int, var endmin:Int):Serializable
