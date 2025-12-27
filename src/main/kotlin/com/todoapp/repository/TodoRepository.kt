package com.todoapp.repository

import com.todoapp.model.Priority
import com.todoapp.model.Status
import com.todoapp.model.Todo

class TodoRepository {
    private val todos = mutableListOf<Todo>()

    fun add(todo: Todo): Todo {
        todos.add(todo)
        return todo
    }

    fun getAll(): List<Todo> = todos.toList()

    fun getById(id: String): Todo? = todos.find { it.id == id }

    fun getByStatus(status: Status): List<Todo> = todos.filter { it.status == status }

    fun getByPriority(priority: Priority): List<Todo> = todos.filter { it.priority == priority }

    fun update(id: String, updater: (Todo) -> Unit): Todo? {
        val todo = getById(id)
        return todo?.also { updater(it) }
    }

    fun delete(id: String): Boolean {
        return todos.removeIf { it.id == id }
    }

    fun deleteAll(): Int {
        val count = todos.size
        todos.clear()
        return count
    }

    fun count(): Int = todos.size
}
