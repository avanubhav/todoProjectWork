package com.test.todolistapp.todoapplication.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.data.db.TodoData
import com.test.todolistapp.databinding.ItemTodoListBinding

class TodoListAdapter(private val context: Context, val listener: TodoClickListener):
    RecyclerView.Adapter<ToDoItemViewHolder>(){

    private val todoList = ArrayList<TodoData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {

        val binding = ItemTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ToDoItemViewHolder(binding)
    }

    fun deleteList(position: Int){
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        val item = todoList[position]

        with(holder.binding) {
            tvTitle.text = item.title
            tvTime.text = item.time
            itemLayout.setOnClickListener { listener.onItemClicked(todoList[holder.adapterPosition]) }

            ivClose.setOnClickListener {
                listener.onDelete(todoList[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun updateList(newList: List<TodoData>){
        todoList.clear()
        todoList.addAll(newList)
        notifyDataSetChanged()
    }

    interface TodoClickListener {
        fun onItemClicked(todo: TodoData)
        fun onDelete(todo: TodoData)
    }
}
