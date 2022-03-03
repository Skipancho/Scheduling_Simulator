package com.example.scheduling_simulator.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.scheduling_simulator.R
import com.example.scheduling_simulator.main.Task

class TaskListAdapter(
    private val context: Context,
    private val list : MutableList<Task>
) : BaseAdapter() {

    override fun getCount() = list.size

    override fun getItem(idx: Int) = list[idx]

    override fun getItemId(idx: Int) = idx.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val v = View.inflate(context, R.layout.task_item, null)

        val t = list[position]

        v.findViewById<TextView>(R.id.name_tv).text = t.name
        v.findViewById<TextView>(R.id.arriveTime_tv).text = t.arriveTime.toString()
        v.findViewById<TextView>(R.id.burstTime_tv).text = t.burstTime.toString()
        v.findViewById<TextView>(R.id.priority_tv).text = t.priority.toString()

        v.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialog = builder.setMessage("삭제하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    list.removeAt(position)
                    notifyDataSetChanged()
                }
                .setNegativeButton("취소",null)
                .create()
            dialog.show()
        }
        return v
    }


}