package com.example.todolist.ui


import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.database.BancoTask
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.example.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = BancoTask(baseContext)
        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            db.selecionarTask(taskId)?.let {
                binding.etTitulo.text = it.title
                binding.etHora.text = it.hora
                binding.etData.text = it.data
            }
            updateListener()
        }else{
            insertListener()
        }


        iconBack()

    }

    private fun insertListener() {
        binding.etData.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offSet = timeZone.getOffset(Date().time) * -1
                binding.etData.text =  Date(it + offSet).format()
            }
            datePicker.show(supportFragmentManager,"DATE_PICKER_TAG")
        }

        binding.etHora.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val minute =  if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.etHora.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager,null)
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }
         binding.buttonAddtask.setOnClickListener {
            val db = BancoTask(baseContext)
            val task = Task(
                id = intent.getIntExtra(TASK_ID, 0),
                title = binding.etTitulo.text,
                hora = binding.etHora.text,
                data = binding.etData.text
            )

            db.salvarTarefa(task)

            setResult(Activity.RESULT_OK)
            finish()

        }

    }

    fun updateListener() {
        binding.etData.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offSet = timeZone.getOffset(Date().time) * -1
                binding.etData.text = Date(it + offSet).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.etHora.editText?.setOnClickListener {
            val timePicker =
                MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val minute =  if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.etHora.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, "TAME_PICKER_TAG")
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }

        binding.buttonAddtask.setOnClickListener {
            val db = BancoTask(baseContext)
            val task = Task(id = intent.getIntExtra(TASK_ID, 0),
                title = binding.etTitulo.text,
                hora = binding.etHora.text,
                data = binding.etData.text
            )

            db.updateTask(task)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }

        fun iconBack() {
            binding.toolbarBack.setOnClickListener {
                finish()
            }
        }

        companion object {
            const val TASK_ID = "task_id"
        }

}






