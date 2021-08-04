package com.example.todolist.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.todolist.adapter.TaskListAdapter
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.adapter = adapter
        updateList()
        insertListener()

    }

    private fun insertListener(){
        binding.fab.setOnClickListener {
            startActivityForResult(Intent(this,AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            Log.e("TAG","listener edit $it")
        }
        adapter.listenerDelete = {
            Log.e("TAG","listener delete $it")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK){
            binding.recyclerView.adapter = adapter
            updateList()
        }
    }
    private fun updateList(){
        val list = TaskDataSource.getList()

        binding.includeEmpty.emptyActivity.visibility = if (list.isEmpty()) View.VISIBLE
        else View.GONE
            adapter.submitList(list)
    }

    companion object{
        const val CREATE_NEW_TASK = 1000
    }

}