package com.todoapp.ui

import com.todoapp.model.Priority
import com.todoapp.model.Status
import com.todoapp.model.Todo
import com.todoapp.repository.TodoRepository
import java.time.format.DateTimeFormatter

class TodoCLI(private val repository: TodoRepository) {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    fun run() {
        println("=== TODO App ===")
        var running = true

        while (running) {
            printMenu()
            when (readLine()?.trim()) {
                "1" -> addTodo()
                "2" -> listTodos()
                "3" -> listByStatus()
                "4" -> updateStatus()
                "5" -> deleteTodo()
                "6" -> showStatistics()
                "0" -> running = false
                else -> println("❌ Opção inválida!")
            }
            if (running) {
                println("\nPressione ENTER para continuar...")
                readLine()
            }
        }
        println("Até logo!")
    }

    private fun printMenu() {
        println("\n" + "=".repeat(40))
        println("📝 Menu Principal")
        println("=".repeat(40))
        println("1. ➕ Adicionar TODO")
        println("2. 📋 Listar todos")
        println("3. 🔍 Filtrar por status")
        println("4. ✏️  Atualizar status")
        println("5. 🗑️  Deletar TODO")
        println("6. 📊 Estatísticas")
        println("0. 🚪 Sair")
        print("\nEscolha uma opção: ")
    }

    private fun addTodo() {
        println("\n➕ Adicionar novo TODO")
        print("Título: ")
        val title = readLine()?.trim() ?: ""
        if (title.isEmpty()) {
            println("❌ Título não pode ser vazio!")
            return
        }

        print("Descrição (opcional): ")
        val description = readLine()?.trim() ?: ""

        print("Prioridade (1-LOW, 2-MEDIUM, 3-HIGH) [2]: ")
        val priority = when (readLine()?.trim()) {
            "1" -> Priority.LOW
            "3" -> Priority.HIGH
            else -> Priority.MEDIUM
        }

        val todo = Todo(
            title = title,
            description = description,
            priority = priority
        )
        repository.add(todo)
        println("✅ TODO criado com sucesso! ID: ${todo.id}")
    }

    private fun listTodos() {
        val todos = repository.getAll()
        if (todos.isEmpty()) {
            println("\n📭 Nenhum TODO encontrado.")
            return
        }

        println("\n📋 Todos os TODOs (${todos.size}):")
        println("=".repeat(80))
        todos.forEachIndexed { index, todo ->
            printTodo(index + 1, todo)
        }
    }

    private fun listByStatus() {
        println("\n🔍 Filtrar por status:")
        println("1. ⏳ PENDING")
        println("2. 🔄 IN_PROGRESS")
        println("3. ✅ COMPLETED")
        print("Escolha: ")

        val status = when (readLine()?.trim()) {
            "1" -> Status.PENDING
            "2" -> Status.IN_PROGRESS
            "3" -> Status.COMPLETED
            else -> {
                println("❌ Opção inválida!")
                return
            }
        }

        val todos = repository.getByStatus(status)
        if (todos.isEmpty()) {
            println("\n📭 Nenhum TODO com status $status.")
            return
        }

        println("\n📋 TODOs com status $status (${todos.size}):")
        println("=".repeat(80))
        todos.forEachIndexed { index, todo ->
            printTodo(index + 1, todo)
        }
    }

    private fun updateStatus() {
        print("\n✏️  Digite o ID do TODO: ")
        val id = readLine()?.trim() ?: ""
        
        val todo = repository.getById(id)
        if (todo == null) {
            println("❌ TODO não encontrado!")
            return
        }

        println("\nTODO atual:")
        printTodo(1, todo)

        println("\n Novo status:")
        println("1. ⏳ PENDING")
        println("2. 🔄 IN_PROGRESS")
        println("3. ✅ COMPLETED")
        print("Escolha: ")

        when (readLine()?.trim()) {
            "1" -> {
                todo.markAsPending()
                println("✅ Status atualizado para PENDING!")
            }
            "2" -> {
                todo.markAsInProgress()
                println("✅ Status atualizado para IN_PROGRESS!")
            }
            "3" -> {
                todo.markAsCompleted()
                println("✅ Status atualizado para COMPLETED!")
            }
            else -> println("❌ Opção inválida!")
        }
    }

    private fun deleteTodo() {
        print("\n🗑️  Digite o ID do TODO para deletar: ")
        val id = readLine()?.trim() ?: ""
        
        val todo = repository.getById(id)
        if (todo == null) {
            println("❌ TODO não encontrado!")
            return
        }

        println("\nTODO a ser deletado:")
        printTodo(1, todo)
        print("\nConfirma exclusão? (S/N): ")
        
        if (readLine()?.trim()?.uppercase() == "S") {
            repository.delete(id)
            println("✅ TODO deletado com sucesso!")
        } else {
            println("❌ Exclusão cancelada.")
        }
    }

    private fun showStatistics() {
        val todos = repository.getAll()
        if (todos.isEmpty()) {
            println("\n📊 Sem dados para exibir.")
            return
        }

        val pending = repository.getByStatus(Status.PENDING).size
        val inProgress = repository.getByStatus(Status.IN_PROGRESS).size
        val completed = repository.getByStatus(Status.COMPLETED).size

        val highPriority = repository.getByPriority(Priority.HIGH).size
        val mediumPriority = repository.getByPriority(Priority.MEDIUM).size
        val lowPriority = repository.getByPriority(Priority.LOW).size

        println("\n📊 Estatísticas:")
        println("=".repeat(40))
        println("Total de TODOs: ${todos.size}")
        println("\n📈 Por Status:")
        println("  ⏳ Pendente:     $pending")
        println("  🔄 Em progresso: $inProgress")
        println("  ✅ Completos:    $completed")
        println("\n🎯 Por Prioridade:")
        println("  🔴 Alta:   $highPriority")
        println("  🟡 Média:  $mediumPriority")
        println("  🟢 Baixa:  $lowPriority")

        if (completed > 0 && todos.size > 0) {
            val completionRate = (completed.toDouble() / todos.size * 100).toInt()
            println("\n✨ Taxa de conclusão: $completionRate%")
        }
    }

    private fun printTodo(number: Int, todo: Todo) {
        val statusIcon = when (todo.status) {
            Status.PENDING -> "⏳"
            Status.IN_PROGRESS -> "🔄"
            Status.COMPLETED -> "✅"
        }

        val priorityIcon = when (todo.priority) {
            Priority.HIGH -> "🔴"
            Priority.MEDIUM -> "🟡"
            Priority.LOW -> "🟢"
        }

        println("\n$number. $statusIcon ${todo.title}")
        println("   ID: ${todo.id}")
        if (todo.description.isNotEmpty()) {
            println("   📄 ${todo.description}")
        }
        println("   $priorityIcon Prioridade: ${todo.priority}")
        println("   📅 Criado: ${todo.createdAt.format(dateFormatter)}")
        println("   🔄 Atualizado: ${todo.updatedAt.format(dateFormatter)}")
    }
}
