package com.test.todolistapp.todoapplication.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapplication.data.db.TodoData
import com.example.todoapplication.data.db.TodoDatabase
import com.test.todolistapp.R
import com.test.todolistapp.databinding.ActivityMainBinding

class ToDoMainActivity : AppCompatActivity(), TodoListAdapter.TodoClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: TodoDatabase
    lateinit var todoViewModel: TodoViewModel
    lateinit var todoAdapter: TodoListAdapter
    private var isDelete = false

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
        )[TodoViewModel::class.java]

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
        openAlert(todo)
    }

    private fun openAlert(todo: TodoData) {

        val builder = AlertDialog.Builder(
            ContextThemeWrapper(this, R.style.AlertDialogCustom)
        )

        val positiveButtonClick = { dialog: DialogInterface, _: Int ->
            todoViewModel.deleteTodo(todo)
            isDelete = true
            dialog.dismiss()
        }
        val negativeButtonClick = { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }

        with(builder)
        {
            setTitle("Warning")
            setMessage(resources.getString(R.string.warning_msg, todo.title))
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener(function = negativeButtonClick)
            )
        }

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnOk = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val btnCancel = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)

        btnOk.setTextColor(resources.getColor(R.color.purple, null))
        btnCancel.setTextColor(resources.getColor(R.color.purple, null))
    }
}