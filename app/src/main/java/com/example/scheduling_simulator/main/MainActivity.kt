package com.example.scheduling_simulator.main

import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.scheduling_simulator.R
import com.example.scheduling_simulator.adapter.TaskListAdapter
import com.example.scheduling_simulator.databinding.ActivityMainBinding
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), MainNavigator {
    private lateinit var adapter: TaskListAdapter

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
    }

    private fun initListView(){
        adapter = TaskListAdapter(this,viewModel.taskList)
        binding.taskLv.adapter = adapter
    }

    override fun updateListView() {
        adapter.notifyDataSetChanged()
    }

}