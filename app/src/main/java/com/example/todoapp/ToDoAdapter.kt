package com.example.todoapp.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.AddNewTask
import com.example.todoapp.MainActivity
import com.example.todoapp.Model.ToDoModel
import com.example.todoapp.R
import com.example.todoapp.Utils.DataBaseHelper

class ToDoAdapter(
    private val myDB: DataBaseHelper,
    private val activity: MainActivity,
    val context: Context
) : RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    private var mList: List<ToDoModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mList[position]
        holder.mCheckBox.text = item.task
        holder.mCheckBox.isChecked = toBoolean(item.status)
        holder.mCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) 1 else 0
            myDB.updateStatus(item.id, status)
        }
    }

    private fun toBoolean(num: Int): Boolean {
        return num != 0
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setTasks(list: List<ToDoModel>) {
        mList = list
        notifyDataSetChanged()
    }

    fun deletTask(position: Int) {
        val item = mList[position]
        myDB.deleteTask(item.id)
        val newList = mList.toMutableList()
        newList.removeAt(position)
        mList = newList
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item = mList[position]
        val bundle = Bundle().apply {
            putInt("id", item.id)
            putString("task", item.task)
        }
        val task = AddNewTask()
        task.arguments = bundle
        task.show(activity.supportFragmentManager, task.tag)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mCheckBox: CheckBox = itemView.findViewById(R.id.mcheckbox)
    }
}
