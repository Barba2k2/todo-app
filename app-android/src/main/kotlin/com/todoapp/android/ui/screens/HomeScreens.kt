package com.todoapp.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.todoapp.android.ui.theme.ThemeMode
import com.todoapp.android.ui.viewmodel.SettingsViewModel
import com.todoapp.android.ui.viewmodel.ThemeViewModel
import com.todoapp.android.ui.viewmodel.TodoEditorViewModel
import com.todoapp.android.ui.viewmodel.TodosViewModel
import com.todoapp.domain.model.SortBy
import com.todoapp.domain.model.Todo
import com.todoapp.domain.model.TodoPriority
import com.todoapp.domain.model.TodosFilter
import kotlinx.datetime.LocalDateTime

@Composable
fun HomeScreen(
  onEditTodo: (String) -> Unit,
  onCreateTodo: () -> Unit,
  onLogout: () -> Unit
) {
  var selectedTab by remember { mutableStateOf(0) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Todo App") },
        colors = TopAppBarDefaults.topAppBarColors()
      )
    },
    bottomBar = {
      BottomAppBar {
        BottomTabButton(
          selected = selectedTab == 0,
          icon = Icons.Rounded.List,
          label = "Todos",
          onClick = { selectedTab = 0 }
        )
        BottomTabButton(
          selected = selectedTab == 1,
          icon = Icons.Rounded.Person,
          label = "Perfil",
          onClick = { selectedTab = 1 }
        )
        BottomTabButton(
          selected = selectedTab == 2,
          icon = Icons.Rounded.Settings,
          label = "Config",
          onClick = { selectedTab = 2 }
        )
      }
    },
    floatingActionButton = {
      if (selectedTab == 0) {
        FloatingActionButton(onClick = onCreateTodo) {
          Icon(Icons.Rounded.Add, contentDescription = null)
        }
      }
    }
  ) { paddingValues ->
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      when (selectedTab) {
        0 -> TodosScreen(onEditTodo = onEditTodo)
        1 -> ProfileScreen(onLogout = onLogout)
        else -> SettingsScreen()
      }
    }
  }
}

@Composable
private fun BottomTabButton(
  selected: Boolean,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  onClick: () -> Unit
) {
  TextButton(onClick = onClick, modifier = Modifier.weight(1f)) {
    Icon(icon, contentDescription = null)
    Spacer(modifier = Modifier.size(6.dp))
    Text(label, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
  }
}

@Composable
fun TodosScreen(
  onEditTodo: (String) -> Unit,
  viewModel: TodosViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()

  LaunchedEffect(Unit) {
    viewModel.loadTodos()
  }

  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    FilterBar(filter = state.filter) { filter ->
      viewModel.updateFilter(
        isCompleted = filter.isCompleted,
        priority = filter.priority,
        sortBy = filter.sortBy,
        sortDescending = filter.sortDescending
      )
    }

    if (state.loading) {
      CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }

    if (state.error != null) {
      Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
    }

    if (!state.loading && state.todos.isEmpty()) {
      Text("Nenhuma tarefa encontrada.")
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      items(state.todos) { todo ->
        TodoCard(
          todo = todo,
          onToggle = { viewModel.toggleTodo(todo) },
          onEdit = { onEditTodo(todo.id) },
          onDelete = { viewModel.deleteTodo(todo.id) }
        )
      }
    }
  }
}

@Composable
private fun FilterBar(
  filter: TodosFilter,
  onFilterChange: (TodosFilter) -> Unit
) {
  var isCompleted by remember { mutableStateOf(filter.isCompleted) }
  var priority by remember { mutableStateOf(filter.priority) }
  var sortBy by remember { mutableStateOf(filter.sortBy) }
  var descending by remember { mutableStateOf(filter.sortDescending) }

  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      TextButton(onClick = {
        isCompleted = null
        onFilterChange(filter.copy(isCompleted = null))
      }) { Text("Todos") }
      TextButton(onClick = {
        isCompleted = false
        onFilterChange(filter.copy(isCompleted = false))
      }) { Text("Pendentes") }
      TextButton(onClick = {
        isCompleted = true
        onFilterChange(filter.copy(isCompleted = true))
      }) { Text("Concluidos") }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      TextButton(onClick = {
        priority = null
        onFilterChange(filter.copy(priority = null))
      }) { Text("Prioridade: Todas") }
      TextButton(onClick = {
        priority = TodoPriority.High
        onFilterChange(filter.copy(priority = TodoPriority.High))
      }) { Text("Alta") }
      TextButton(onClick = {
        priority = TodoPriority.Urgent
        onFilterChange(filter.copy(priority = TodoPriority.Urgent))
      }) { Text("Urgente") }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      TextButton(onClick = {
        sortBy = SortBy.Priority
        onFilterChange(filter.copy(sortBy = SortBy.Priority, sortDescending = descending))
      }) { Text("Ordenar: Prioridade") }
      TextButton(onClick = {
        sortBy = SortBy.DueDate
        onFilterChange(filter.copy(sortBy = SortBy.DueDate, sortDescending = descending))
      }) { Text("Vencimento") }
      TextButton(onClick = {
        descending = !descending
        onFilterChange(filter.copy(sortBy = sortBy, sortDescending = descending))
      }) { Text(if (descending) "Desc" else "Asc") }
    }
  }
}

@Composable
private fun TodoCard(
  todo: Todo,
  onToggle: () -> Unit,
  onEdit: () -> Unit,
  onDelete: () -> Unit
) {
  val priorityColor = when (todo.priority) {
    TodoPriority.Low -> MaterialTheme.colorScheme.tertiary
    TodoPriority.Medium -> MaterialTheme.colorScheme.secondary
    TodoPriority.High -> MaterialTheme.colorScheme.primary
    TodoPriority.Urgent -> MaterialTheme.colorScheme.error
  }

  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(todo.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        IconButton(onClick = onToggle) {
          Icon(Icons.Rounded.Check, contentDescription = null)
        }
      }
      if (!todo.description.isNullOrBlank()) {
        Text(todo.description ?: "", style = MaterialTheme.typography.bodyMedium)
      }
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
          text = todo.priority.name,
          color = priorityColor,
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.SemiBold
        )
        todo.dueDate?.let {
          Text("Vence: $it", style = MaterialTheme.typography.bodySmall)
        }
      }
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onEdit) { Text("Editar") }
        TextButton(onClick = onDelete) { Text("Excluir") }
      }
    }
  }
}

@Composable
fun TodoEditorScreen(
  todoId: String?,
  onDone: () -> Unit,
  viewModel: TodoEditorViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var dueDate by remember { mutableStateOf("") }
  var priority by remember { mutableStateOf(TodoPriority.Medium) }
  var isCompleted by remember { mutableStateOf(false) }

  LaunchedEffect(todoId) {
    if (todoId != null) {
      viewModel.loadTodo(todoId)
    }
  }

  LaunchedEffect(state.todo) {
    state.todo?.let {
      title = it.title
      description = it.description ?: ""
      dueDate = it.dueDate?.toString() ?: ""
      priority = it.priority
      isCompleted = it.isCompleted
    }
  }

  LaunchedEffect(state.saved) {
    if (state.saved) {
      viewModel.clearMessage()
      onDone()
    }
  }

  Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    Text(if (todoId == null) "Nova tarefa" else "Editar tarefa", style = MaterialTheme.typography.titleLarge)

    OutlinedTextField(
      value = title,
      onValueChange = { title = it },
      label = { Text("Titulo") },
      modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
      value = description,
      onValueChange = { description = it },
      label = { Text("Descricao") },
      modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
      value = dueDate,
      onValueChange = { dueDate = it },
      label = { Text("Vencimento (YYYY-MM-DDTHH:MM:SS)") },
      modifier = Modifier.fillMaxWidth()
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      TextButton(onClick = { priority = TodoPriority.Low }) { Text("Low") }
      TextButton(onClick = { priority = TodoPriority.Medium }) { Text("Medium") }
      TextButton(onClick = { priority = TodoPriority.High }) { Text("High") }
      TextButton(onClick = { priority = TodoPriority.Urgent }) { Text("Urgent") }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      TextButton(onClick = { isCompleted = !isCompleted }) {
        Text(if (isCompleted) "Concluida" else "Pendente")
      }
    }

    if (state.error != null) {
      Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
    }

    val parsedDueDate = parseDateOrNull(dueDate)

    if (todoId == null) {
      PrimaryButton(
        text = "Salvar",
        enabled = title.isNotBlank(),
        modifier = Modifier.fillMaxWidth(),
        onClick = {
          viewModel.createTodo(
            title.trim(),
            description.takeIf { it.isNotBlank() },
            parsedDueDate,
            priority
          )
        }
      )
    } else {
      PrimaryButton(
        text = "Atualizar",
        enabled = title.isNotBlank(),
        modifier = Modifier.fillMaxWidth(),
        onClick = {
          viewModel.updateTodo(
            id = todoId,
            title = title.trim(),
            description = description.takeIf { it.isNotBlank() },
            dueDate = parsedDueDate,
            priority = priority,
            isCompleted = isCompleted
          )
        }
      )
      TextButton(onClick = { viewModel.deleteTodo(todoId) }) {
        Text("Excluir", color = MaterialTheme.colorScheme.error)
      }
    }
  }
}

private fun parseDateOrNull(value: String): LocalDateTime? {
  return try {
    if (value.isBlank()) null else LocalDateTime.parse(value)
  } catch (_: Throwable) {
    null
  }
}

@Composable
fun ProfileScreen(
  onLogout: () -> Unit,
  viewModel: SettingsViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()

  Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    Text("Perfil", style = MaterialTheme.typography.titleLarge)
    Text("Email: ${state.email ?: "-"}")
    PrimaryButton(text = "Sair", modifier = Modifier.fillMaxWidth(), onClick = onLogout)
  }
}

@Composable
fun SettingsScreen(
  settingsViewModel: SettingsViewModel = hiltViewModel(),
  themeViewModel: ThemeViewModel = hiltViewModel()
) {
  val state by settingsViewModel.state.collectAsState()
  val themeMode by themeViewModel.mode.collectAsState()
  var baseUrl by remember { mutableStateOf(state.baseUrl) }

  LaunchedEffect(state.baseUrl) {
    baseUrl = state.baseUrl
  }

  Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    Text("Configuracoes", style = MaterialTheme.typography.titleLarge)

    OutlinedTextField(
      value = baseUrl,
      onValueChange = { baseUrl = it },
      label = { Text("Base URL") },
      modifier = Modifier.fillMaxWidth()
    )
    PrimaryButton(
      text = "Salvar Base URL",
      modifier = Modifier.fillMaxWidth(),
      onClick = { settingsViewModel.setBaseUrl(baseUrl) }
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      TextButton(onClick = { settingsViewModel.setEnvironment(com.todoapp.domain.model.Environment.Dev) }) { Text("Dev") }
      TextButton(onClick = { settingsViewModel.setEnvironment(com.todoapp.domain.model.Environment.Stage) }) { Text("Stage") }
      TextButton(onClick = { settingsViewModel.setEnvironment(com.todoapp.domain.model.Environment.Prod) }) { Text("Prod") }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      TextButton(onClick = { themeViewModel.setMode(ThemeMode.System) }) { Text("Sistema") }
      TextButton(onClick = { themeViewModel.setMode(ThemeMode.Light) }) { Text("Claro") }
      TextButton(onClick = { themeViewModel.setMode(ThemeMode.Dark) }) { Text("Escuro") }
    }

    Text("Tema atual: ${themeMode.name}")
  }
}
