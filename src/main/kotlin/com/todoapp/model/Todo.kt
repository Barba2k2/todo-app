package com.todoapp.model

import java.time.LocalDateTime
import java.util.UUID

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class Status {
    PENDING, IN_PROGRESS, COMPLETED
}

data class Todo(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    var status: Status = Status.PENDING,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun markAsCompleted() {
        status = Status.COMPLETED
        updatedAt = LocalDateTime.now()
    }

    fun markAsInProgress() {
        status = Status.IN_PROGRESS
        updatedAt = LocalDateTime.now()
    }

    fun markAsPending() {
        status = Status.PENDING
        updatedAt = LocalDateTime.now()
    }
}
