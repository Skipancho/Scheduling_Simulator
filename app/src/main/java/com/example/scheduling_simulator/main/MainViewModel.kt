package com.example.scheduling_simulator.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import splitties.toast.toast
import java.lang.ref.WeakReference
import java.util.*

class MainViewModel(app : Application) : AndroidViewModel(app) {

    var navigatorRef : WeakReference<MainNavigator>? = null
    private val navigator get() = navigatorRef?.get()

    val taskList = mutableListOf<Task>()
    val name = MutableLiveData("")
    val arriveTime = MutableLiveData("")
    val burstTime = MutableLiveData("")
    val priority = MutableLiveData("")
    val algorithm = MutableLiveData("")

    val result = MutableLiveData("")

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
        name.value=""
        arriveTime.value = ""
        burstTime.value = ""
        priority.value = ""
    }

    fun randomTask(){
        val nameT = "p${taskList.size+1}"
        val arriveT = (Math.random()*10).toInt()
        val burstT = (Math.random()*10).toInt()+1
        val priorityT = (Math.random()*5).toInt()
        taskList.add(Task(nameT,arriveT,burstT,priorityT))
        navigator?.updateListView()
    }

    fun start(){
        if (taskList.isNullOrEmpty()){
            toast("작업이 없습니다.")
            return
        }
        navigator?.algorithmCheck()
        toast("${algorithm.value}")
        result.value = ""
        when(algorithm.value){
            "SJF"-> sjf()
        }
    }

    private fun sjf(){
        var text = ""
        var runningTime = 0
        val taskQ = PriorityQueue<Task> { o1, o2 -> o1.arriveTime - o2.arriveTime }
        val readyQ = PriorityQueue<Task> { o1, o2 -> o1.burstTime - o2.burstTime }
        var curTask : Task? = null
        taskList.forEach { taskQ.add(it)}

        while (taskQ.isNotEmpty()||readyQ.isNotEmpty()||curTask != null){
            while (taskQ.isNotEmpty() && taskQ.peek()!!.arriveTime <= runningTime){
                readyQ.add(taskQ.poll())
            }
            if (curTask == null && readyQ.isNotEmpty()){
                curTask = readyQ.poll()
            }
            if (curTask != null){
                text = "$text${curTask.name}"
                curTask.burstTime--
                if (curTask.burstTime == 0){
                    curTask = null
                }
            }else{
                text = "$text(idle)"
            }
            runningTime++
        }

        result.value = text
        taskList.clear()
    }
}