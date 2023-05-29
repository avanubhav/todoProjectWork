package com.test.todolistapp.todoapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.test.todolistapp.todoapplication.data.TodoRepository
import com.example.todoapplication.data.db.TodoData
import com.example.todoapplication.data.db.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TodoRepository
    val allTodo :  LiveData<List<TodoData>>

    init {
        val dao = TodoDatabase.getDatabase(application).getTodoDao()
        repository = TodoRepository(dao)
        allTodo = repository.allTodoData
    }

    fun insertTodo(todo: TodoData) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(todo)
    }

    fun updateTodo(todo: TodoData) = viewModelScope.launch(Dispatchers.IO){
        repository.update(todo)
    }

    fun deleteTodo(todo: TodoData) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(todo)
    }

}
