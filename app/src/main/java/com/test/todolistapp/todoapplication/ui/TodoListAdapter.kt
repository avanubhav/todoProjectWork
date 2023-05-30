package com.test.todolistapp.todoapplication.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.data.db.TodoData
import com.test.todolistapp.databinding.ItemTodoListBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodoListAdapter(private val context: Context, val listener: TodoClickListener) :
    RecyclerView.Adapter<ToDoItemViewHolder>() {

    private val todoList = ArrayList<TodoData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {

        val binding =
            ItemTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ToDoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        val item = todoList[position]

        with(holder.binding) {
            tvTitle.text = item.title
            tvTime.text = item.time
            itemLayout.setOnClickListener { listener.onItemClicked(todoList[holder.adapterPosition]) }

            val currentTime: String = SimpleDateFormat("h:mm a", Locale.ENGLISH).format(Date())
            val timeToCompare = item.time
            if (currentTime > timeToCompare) {
                tvTitle.setTextColor(Color.RED)
                tvStatus.visibility = VISIBLE
                tvStatus.text = "Pending"
            } else {
                tvTitle.setTextColor(Color.BLACK)
                tvStatus.visibility = GONE
            }

            ivClose.setOnClickListener {
                listener.onDelete(todoList[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun updateList(newList: List<TodoData>) {
        todoList.clear()
        todoList.addAll(newList)
        notifyDataSetChanged()
    }

    interface TodoClickListener {
        fun onItemClicked(todo: TodoData)
        fun onDelete(todo: TodoData)
    }
}
