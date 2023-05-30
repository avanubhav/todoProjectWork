package com.test.todolistapp.todoapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapplication.data.db.TodoData
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.test.todolistapp.databinding.ActivityAddTodoBinding

class AddTodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTodoBinding
    private lateinit var todo: TodoData
    private lateinit var oldTodo: TodoData
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.ivMore.visibility = View.GONE
        binding.toolbar.tvTitle.text = "Add a task"

        try {
            oldTodo = intent.getSerializableExtra("current_todo") as TodoData
            binding.etTaskTitle.editText?.setText(oldTodo.title)
            binding.etTaskTime.editText?.setText(oldTodo.time)
            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.etTaskTime.editText?.setOnClickListener {
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("SELECT TASK TIME")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.show(supportFragmentManager, "AddTodoActivity")

            materialTimePicker.addOnPositiveButtonClickListener {

                val pickedHour: Int = materialTimePicker.hour
                val pickedMinute: Int = materialTimePicker.minute

                val formattedTime: String = when {
                    pickedHour > 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour - 12}:0${materialTimePicker.minute} PM"
                        } else {
                            "${materialTimePicker.hour - 12}:${materialTimePicker.minute} PM"
                        }
                    }

                    pickedHour == 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} PM"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} PM"
                        }
                    }

                    pickedHour == 0 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour + 12}:0${materialTimePicker.minute} AM"
                        } else {
                            "${materialTimePicker.hour + 12}:${materialTimePicker.minute} AM"
                        }
                    }

                    else -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} AM"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} AM"
                        }
                    }
                }
                binding.etTaskTime.editText?.setText(formattedTime)
            }
        }

        binding.btnAdd.setOnClickListener {
            val title = binding.etTaskTitle.editText?.text.toString()
            val todoTime = binding.etTaskTime.editText?.text.toString()

            if (title.isNotEmpty() && todoTime.isNotEmpty()) {

                todo = if (isUpdate) {
                    TodoData(oldTodo.id, title, todoTime)
                } else {
                    TodoData(null, title, todoTime)
                }
                val intent = Intent()
                intent.putExtra("todo", todo)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(
                    this@AddTodoActivity,
                    "Please add todo or click cancel",
                    Toast.LENGTH_LONG
                )
                    .show()
                return@setOnClickListener
            }
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.toolbar.imgBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

    }
}