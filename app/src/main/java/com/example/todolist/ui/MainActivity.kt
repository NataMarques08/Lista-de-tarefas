package com.example.todolist.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View

import com.example.todolist.adapter.TaskListAdapter
import com.example.todolist.database.BancoTask
import com.example.todolist.databinding.ActivityMainBinding


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
            val intent = Intent(this,AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID,it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            val db = BancoTask(baseContext)
            db.deleteTask(it.id)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var db = BancoTask(baseContext)
        if (requestCode == CREATE_NEW_TASK){

            binding.recyclerView.adapter = adapter
            adapter.submitList(db.listaTarefas())
            updateList()
        }
    }
    private fun updateList(){
            var db = BancoTask(baseContext)
            val list = db.listaTarefas()



            if(list.isEmpty()) {
                binding.includeEmpty.emptyActivity.visibility =  View.VISIBLE
            }else{
                binding.includeEmpty.emptyActivity.visibility = View.GONE
            }
            adapter.submitList(list)
        }



    companion object{
        const val CREATE_NEW_TASK = 1000
    }

}