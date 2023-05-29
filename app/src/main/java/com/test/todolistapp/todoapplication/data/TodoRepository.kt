package com.test.todolistapp.todoapplication.data

import androidx.lifecycle.LiveData
import com.example.todoapplication.data.db.TodoDao
import com.example.todoapplication.data.db.TodoData

class TodoRepository(private val todoDao: TodoDao) {

    val allTodoData: LiveData<List<TodoData>> = todoDao.getAllTodos()

    suspend fun insert(todo: TodoData){
        todoDao.insert(todo)
    }

    suspend fun delete(todo: TodoData){
        todoDao.delete(todo)
    }

    suspend fun update(todo: TodoData){
        todoDao.update(todo.id, todo.title, todo.time)
    }
}