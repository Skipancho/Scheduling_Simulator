package com.example.scheduling_simulator.main

data class Task(
    val name : String,
    val arriveTime : Int,
    var burstTime : Int,
    val priority : Int
)
