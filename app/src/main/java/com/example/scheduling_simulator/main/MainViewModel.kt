package com.example.scheduling_simulator.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import splitties.toast.toast
import java.lang.ref.WeakReference

class MainViewModel(app : Application) : AndroidViewModel(app) {

    var navigatorRef : WeakReference<MainNavigator>? = null
    private val navigator get() = navigatorRef?.get()

    val taskList = mutableListOf<Task>()
    val name = MutableLiveData("")
    val arriveTime = MutableLiveData("")
    val burstTime = MutableLiveData("")
    val priority = MutableLiveData("")

    fun addTask(){
        val nameText = name.value!!
        val arriveTimeText = arriveTime.value!!
        val burstTimeText = burstTime.value!!

        if (nameText == "" || arriveTimeText == "" || burstTimeText == ""){
            toast("빈칸을 확인해 주세요.")
            return
        }

        val priorityText = if (priority.value == "") 0 else priority.value!!.toInt()
        taskList.add(
            Task(nameText,arriveTimeText.toInt(),burstTimeText.toInt(),priorityText)
        )
        navigator?.updateListView()
    }
}