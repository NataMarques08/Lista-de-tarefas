package com.example.todolist.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.ListTaskBinding
import com.example.todolist.model.Task

class TaskListAdapter :
    androidx.recyclerview.widget.ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallBack()) {

    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}

   inner class TaskViewHolder(
        private val binding: ListTaskBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item:Task){
            binding.tvTitle.text = item.title
            binding.tvDate.text = "${item.data} ${item.hora}"
            binding.ivOptions.setOnClickListener {
                showPopup(item)
            }
        }
        private fun showPopup(item:Task){
            val ivOptions = binding.ivOptions
            val popupMenu = PopupMenu(ivOptions.context,ivOptions)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_delete -> listenerDelete(item)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  ListTaskBinding.inflate(inflater,parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
class DiffCallBack : DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
}