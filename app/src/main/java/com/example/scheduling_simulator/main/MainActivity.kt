package com.example.scheduling_simulator.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduling_simulator.R
import com.example.scheduling_simulator.adapter.GraphAdapter
import com.example.scheduling_simulator.adapter.TaskListAdapter
import com.example.scheduling_simulator.databinding.ActivityMainBinding
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), MainNavigator {
    private lateinit var adapter: TaskListAdapter
    private lateinit var graphAdapter: GraphAdapter

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
            .also { it.navigatorRef = WeakReference(this) }
    }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initListView()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        graphAdapter = GraphAdapter(this,viewModel.result_tasks)
        binding.resultRv.adapter = graphAdapter
        binding.resultRv.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
    }

    private fun initListView(){
        adapter = TaskListAdapter(this,viewModel.taskList)
        binding.taskLv.adapter = adapter
        val arrayAdapter = ArrayAdapter.createFromResource(this,R.array.scheduling_algorithm,android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = arrayAdapter
    }

    override fun updateGraph() {
        graphAdapter.notifyDataSetChanged()
    }

    override fun updateListView() {
        adapter.notifyDataSetChanged()
    }

    override fun algorithmCheck() {
        viewModel.algorithm.value = binding.spinner.selectedItem as String
    }

}