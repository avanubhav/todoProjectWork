package com.test.todolistapp.todoapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapplication.data.db.TodoData
import com.example.todoapplication.data.db.TodoDatabase
import com.test.todolistapp.databinding.ActivityMainBinding

class ToDoMainActivity : AppCompatActivity(), TodoListAdapter.TodoClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: TodoDatabase
    lateinit var todoViewModel: TodoViewModel
    lateinit var todoAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.toolbar.tvTitle.visibility = View.GONE

        setContentView(binding.root)

        initUI()

        todoViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(TodoViewModel::class.java)

        todoViewModel.allTodo.observe(this) { list ->
            list?.let {
                todoAdapter.updateList(list)
            }
        }

        database = TodoDatabase.getDatabase(this)
    }

    private fun initUI() {
        binding.rvTodo.setHasFixedSize(true)
        binding.rvTodo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todoAdapter = TodoListAdapter(this, this)
        binding.rvTodo.adapter = todoAdapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val todo = result.data?.getSerializableExtra("todo") as? TodoData
                    if (todo != null) {
                        todoViewModel.insertTodo(todo)
                    }
                }
            }

        binding.addTodoItem.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            getContent.launch(intent)
        }

    }

    private val updateOrDeleteTodo =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val todo = result.data?.getSerializableExtra("todo") as TodoData
                val isDelete = result.data?.getBooleanExtra("delete_todo", false) as Boolean
                if (!isDelete) {
                    todoViewModel.updateTodo(todo)
                } else {
                    todoViewModel.deleteTodo(todo)
                }
            }
        }

    override fun onItemClicked(todo: TodoData) {
        val intent = Intent(this, AddTodoActivity::class.java)
        intent.putExtra("current_todo", todo)
        updateOrDeleteTodo.launch(intent)
    }

    override fun onDelete(todo: TodoData) {
        todoViewModel.deleteTodo(todo)
    }
}