package com.todoapp

import com.todoapp.repository.TodoRepository
import com.todoapp.ui.TodoCLI

fun main() {
    val repository = TodoRepository()
    val cli = TodoCLI(repository)
    cli.run()
}
