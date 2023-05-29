package com.example.todoapplication.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoData)

    @Delete
    suspend fun delete(todo: TodoData)

    @Query("SELECT * from todo_table order by id ASC")
    fun getAllTodos(): LiveData<List<TodoData>>

    @Query("UPDATE todo_table set title = :title, time = :time where id = :id")
    suspend fun update(id: Long?, title: String?, time: String?)
}