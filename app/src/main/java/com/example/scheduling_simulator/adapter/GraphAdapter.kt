package com.example.scheduling_simulator.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduling_simulator.R
import com.example.scheduling_simulator.databinding.TaskImageBinding
import com.example.scheduling_simulator.main.TaskModel
import com.example.scheduling_simulator.util.Colors

class GraphAdapter(
    private val context: Context,
    private val tasks : List<TaskModel>
) : RecyclerView.Adapter<GraphAdapter.GraphViewHolder>(){

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): GraphViewHolder {
        val  binding : ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.task_image,
            parent,
            false
        )
        return GraphViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GraphViewHolder, position: Int) {
        val task = tasks[position]
        val binding = holder.binding as TaskImageBinding
        binding.taskModel = task
        if (task.idx == -1){
            binding.card.setBackgroundColor(Color.WHITE)
        }else {
            binding.card.setBackgroundColor(Colors.colors[task.idx % Colors.size])
        }
    }

    class GraphViewHolder(
        val binding : ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root)
}