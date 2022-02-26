package com.example.scheduling_simulator.main

data class Task(
    val name : String,
    val arriveTime : Int,
    val burstTime : Int,
    val priority : Int
)
