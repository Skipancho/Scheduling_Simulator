package com.example.scheduling_simulator.main

data class TaskModel(
    val name : String,
    val runningT : Int,
    val idx : Int
){
    val runningTime = runningT.toString()
}