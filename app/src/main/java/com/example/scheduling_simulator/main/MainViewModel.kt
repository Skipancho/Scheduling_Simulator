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
    val name = MutableLiveData("p0")
    val arriveTime = MutableLiveData("")
    val burstTime = MutableLiveData("")
    val priority = MutableLiveData("")
    val algorithm = MutableLiveData("")

    val timeSlice = MutableLiveData("3")

    val result_tasks = mutableListOf<TaskModel>()

    private var cnt = 0

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
            Task(nameText,arriveTimeText.toInt(),burstTimeText.toInt(),priorityText,taskList.size)
        )
        navigator?.updateListView()
        name.value="p${++cnt}"
        arriveTime.value = ""
        burstTime.value = ""
        priority.value = ""
    }

    fun randomTask(){
        val nameT = "p${cnt++}"
        val arriveT = (Math.random()*10).toInt()
        val burstT = (Math.random()*10).toInt()+1
        val priorityT = (Math.random()*5).toInt()
        taskList.add(Task(nameT,arriveT,burstT,priorityT,taskList.size))
        name.value = "p${cnt}"
        navigator?.updateListView()
    }

    fun clearTasks(){
        taskList.clear()
        result_tasks.clear()
        cnt = 0
        name.value="p0"
        navigator?.updateListView()
        navigator?.updateGraph()
    }

    fun start(){
        if (taskList.isNullOrEmpty()){
            toast("작업이 없습니다.")
            return
        }
        result_tasks.clear()
        navigator?.algorithmCheck()
        toast("${algorithm.value}")
        when(algorithm.value){
            "FCFS"->fcfs()
            "SJF"-> sjf()
            "HRN"-> hrn()
            "RR"-> {
                val timeS = timeSlice.value.toString()
                if (timeS == "" || timeS.toInt() == 0){
                    toast("timeslice를 확인해주세요.")
                    return
                }
                rr(timeS.toInt())
            }
        }
        navigator?.updateGraph()
    }

    private fun fcfs(){
        //first-come, first-service
        val taskQ = PriorityQueue<Task> { o1, o2 -> o1.arriveTime - o2.arriveTime }
        val readyQ = PriorityQueue<Task> { o1, o2 -> o1.arriveTime - o2.arriveTime }
        scheduling(taskQ, readyQ)
    }

    private fun sjf(){
        //shortest job first
        val taskQ = PriorityQueue<Task> { o1, o2 -> o1.arriveTime - o2.arriveTime }
        val readyQ = PriorityQueue<Task> { o1, o2 -> o1.burstTime - o2.burstTime }
        scheduling(taskQ,readyQ)
    }

    private fun hrn(){
        //highest response ration-next
        val taskQ = PriorityQueue<Task> { o1, o2 -> o1.arriveTime - o2.arriveTime }
        var curTask : Task? = null
        var runningTime = 0
        val readyQ = mutableListOf<Task>()

        taskList.forEach { taskQ.add(it.copy())}

        while (taskQ.isNotEmpty() || readyQ.isNotEmpty()||curTask != null){
            while (taskQ.isNotEmpty() && taskQ.peek()!!.arriveTime <= runningTime){
                readyQ.add(taskQ.poll()!!)
            }
            if (curTask == null && readyQ.isNotEmpty()){
                readyQ.sortWith{o1,o2 ->
                    val p1 : Double = (runningTime - o1.arriveTime + o1.burstTime)/o1.burstTime.toDouble()
                    val p2 : Double = (runningTime - o2.arriveTime + o2.burstTime)/o2.burstTime.toDouble()
                    when{
                        p1 > p2 -> -1
                        p1 == p2 -> o1.burstTime - o2.burstTime
                        else -> 1
                    }
                }
                curTask = readyQ[0]
                readyQ.removeAt(0)
            }
            if (curTask != null){
                result_tasks.add(TaskModel(curTask.name,runningTime,curTask.idx))
                curTask.burstTime--
                if (curTask.burstTime == 0){
                    curTask = null
                }
            }else{
                result_tasks.add(TaskModel("idle",runningTime,-1))
            }
            runningTime++
        }
        result_tasks.add(TaskModel("idle",runningTime,-1))
    }

    private fun scheduling(taskQ : PriorityQueue<Task>,readyQ : PriorityQueue<Task>){
        var curTask : Task? = null
        var runningTime = 0
        taskList.forEach { taskQ.add(it.copy())}
        while (taskQ.isNotEmpty() || readyQ.isNotEmpty()||curTask != null){
            while (taskQ.isNotEmpty() && taskQ.peek()!!.arriveTime <= runningTime){
                readyQ.add(taskQ.poll())
            }
            if (curTask == null && readyQ.isNotEmpty()){
                curTask = readyQ.poll()
            }
            if (curTask != null){
                result_tasks.add(TaskModel(curTask.name,runningTime,curTask.idx))
                curTask.burstTime--
                if (curTask.burstTime == 0){
                    curTask = null
                }
            }else{
                result_tasks.add(TaskModel("idle",runningTime,-1))
            }
            runningTime++
        }
        result_tasks.add(TaskModel("idle",runningTime,-1))
    }

    private fun rr(timeSlice : Int){
        var runningTime = 0
        var curTask : Task? = null
        val taskQ = PriorityQueue<Task> { o1, o2 -> o1.arriveTime - o2.arriveTime }
        val readyQ : Queue<Task> = LinkedList()
        var count = 0
        taskList.forEach { taskQ.add(it.copy())}
        while (taskQ.isNotEmpty() || readyQ.isNotEmpty()||curTask != null){
            while (taskQ.isNotEmpty() && taskQ.peek()!!.arriveTime <= runningTime){
                readyQ.add(taskQ.poll())
            }
            if (curTask == null && readyQ.isNotEmpty()){
                curTask = readyQ.poll()
            }
            if (curTask != null){
                result_tasks.add(TaskModel(curTask.name,runningTime,curTask.idx))
                curTask.burstTime--
                count++
                if (curTask.burstTime == 0){
                    curTask = null
                    count = 0
                }else if (count == timeSlice){
                    readyQ.add(curTask)
                    curTask = null
                    count = 0
                }
            }else{
                result_tasks.add(TaskModel("idle",runningTime,-1))
            }
            runningTime++
        }
        result_tasks.add(TaskModel("idle",runningTime,-1))
    }
}