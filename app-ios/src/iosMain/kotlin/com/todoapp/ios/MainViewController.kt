package com.todoapp.ios

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.todoapp.domain.model.SortBy
import com.todoapp.domain.model.TodoPriority
import com.todoapp.domain.model.TodosFilter
import com.todoapp.domain.result.AppResult
import com.todoapp.ios.theme.ThemeMode
import com.todoapp.ios.theme.TodoTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { IosApp() }

private enum class Screen {
  Login,
  Register,
  Forgot,
  Reset,
  Home,
  Edit
}

@Composable
private fun IosApp() {
  val container = remember { AppContainer() }
  val scope = rememberCoroutineScope()

    var screen by remember { mutableStateOf(Screen.Login) }
    var editingId by remember { mutableStateOf<String?>(null) }
    var themeMode by remember { mutableStateOf(loadTheme()) }

    LaunchedEffect(Unit) {
        val hasSession = container.getSessionUseCase() != null
        screen = if (hasSession) Screen.Home else Screen.Login
    }

    TodoTheme(mode = themeMode) {
        when (screen) {
      Screen.Login -> LoginScreen(
        onRegister = { screen = Screen.Register },
        onForgot = { screen = Screen.Forgot },
        onSuccess = { screen = Screen.Home },
        container = container
      )

      Screen.Register -> RegisterScreen(
        onBack = { screen = Screen.Login },
        onSuccess = { screen = Screen.Login },
        container = container
      )

      Screen.Forgot -> ForgotScreen(
        onBack = { screen = Screen.Login },
        onReset = { screen = Screen.Reset },
        container = container
      )

      Screen.Reset -> ResetScreen(
        onBack = { screen = Screen.Login },
        container = container
      )

      Screen.Home -> HomeScreen(
        container = container,
        onCreate = { editingId = null; screen = Screen.Edit },
        onEdit = { id -> editingId = id; screen = Screen.Edit },
                onLogout = { scope.launch { container.logoutUseCase() }; screen = Screen.Login },
        onThemeChange = {
          themeMode = it
          saveTheme(it)
        }
      )

      Screen.Edit -> TodoEditorScreen(
        container = container,
        todoId = editingId,
        onDone = { screen = Screen.Home }
      )
    }
  }
}

@Composable
private fun LoginScreen(
  onRegister: () -> Unit,
  onForgot: () -> Unit,
  onSuccess: () -> Unit,
  container: AppContainer
) {
  val scope = rememberCoroutineScope()
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var error by remember { mutableStateOf<String?>(null) }

  BaseAuthLayout(title = "Entrar") {
    InputField("Email", email, onChange = { email = it })
    InputField("Senha", password, onChange = { password = it }, isPassword = true)

    PrimaryButton("Entrar", enabled = email.isNotBlank() && password.isNotBlank()) {
      scope.launch {
                when (val result = container.loginUseCase(email.trim(), password)) {
          is AppResult.Success -> {
            container.settingsRepository.setEmail(email)
            onSuccess()
          }

          is AppResult.Failure -> error = result.error.message ?: "Falha no login"
        }
      }
    }

    error?.let { ErrorText(it) }
    LinkButton("Criar conta", onRegister)
    LinkButton("Esqueci minha senha", onForgot)
  }
}

@Composable
private fun RegisterScreen(
  onBack: () -> Unit,
  onSuccess: () -> Unit,
  container: AppContainer
) {
  val scope = rememberCoroutineScope()
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var confirm by remember { mutableStateOf("") }
  var error by remember { mutableStateOf<String?>(null) }

  BaseAuthLayout(title = "Criar conta") {
    InputField("Email", email, onChange = { email = it })
    InputField("Senha", password, onChange = { password = it }, isPassword = true)
    InputField("Confirmar senha", confirm, onChange = { confirm = it }, isPassword = true)

    PrimaryButton("Cadastrar", enabled = email.isNotBlank() && password.isNotBlank() && confirm.isNotBlank()) {
      scope.launch {
                when (val result = container.registerUseCase(email.trim(), password, confirm)) {
          is AppResult.Success -> onSuccess()
          is AppResult.Failure -> error = result.error.message ?: "Falha no cadastro"
        }
      }
    }

    error?.let { ErrorText(it) }
    LinkButton("Voltar", onBack)
  }
}

@Composable
private fun ForgotScreen(
  onBack: () -> Unit,
  onReset: () -> Unit,
  container: AppContainer
) {
  val scope = rememberCoroutineScope()
  var email by remember { mutableStateOf("") }
  var error by remember { mutableStateOf<String?>(null) }

  BaseAuthLayout(title = "Recuperar senha") {
    InputField("Email", email, onChange = { email = it })
    PrimaryButton("Enviar token", enabled = email.isNotBlank()) {
      scope.launch {
                when (val result = container.forgotPasswordUseCase(email.trim())) {
          is AppResult.Success -> onReset()
          is AppResult.Failure -> error = result.error.message ?: "Falha ao enviar"
        }
      }
    }
    error?.let { ErrorText(it) }
    LinkButton("Voltar", onBack)
  }
}

@Composable
private fun ResetScreen(
  onBack: () -> Unit,
  container: AppContainer
) {
  val scope = rememberCoroutineScope()
  var email by remember { mutableStateOf("") }
  var token by remember { mutableStateOf("") }
  var newPassword by remember { mutableStateOf("") }
  var confirm by remember { mutableStateOf("") }
  var error by remember { mutableStateOf<String?>(null) }

  BaseAuthLayout(title = "Resetar senha") {
    InputField("Email", email, onChange = { email = it })
    InputField("Token", token, onChange = { token = it })
    InputField("Nova senha", newPassword, onChange = { newPassword = it }, isPassword = true)
    InputField("Confirmar senha", confirm, onChange = { confirm = it }, isPassword = true)

    PrimaryButton(
      "Salvar",
      enabled = email.isNotBlank() && token.isNotBlank() && newPassword.isNotBlank() && confirm.isNotBlank()
    ) {
      scope.launch {
                when (
                    val result = container.resetPasswordUseCase(
                        email.trim(),
                        token,
                        newPassword,
                        confirm
                    )
                ) {
          is AppResult.Success -> onBack()
          is AppResult.Failure -> error = result.error.message ?: "Falha ao resetar"
        }
      }
    }

    error?.let { ErrorText(it) }
    LinkButton("Voltar", onBack)
  }
}

@Composable
private fun HomeScreen(
  container: AppContainer,
  onCreate: () -> Unit,
  onEdit: (String) -> Unit,
  onLogout: () -> Unit,
  onThemeChange: (ThemeMode) -> Unit
) {
  val scope = rememberCoroutineScope()
  var selectedTab by remember { mutableStateOf(0) }
  var todos by remember { mutableStateOf(listOf<com.todoapp.domain.model.Todo>()) }
  var error by remember { mutableStateOf<String?>(null) }
  var filter by remember { mutableStateOf(TodosFilter()) }
  var email by remember { mutableStateOf<String?>(null) }
  var baseUrl by remember { mutableStateOf("") }
  var environment by remember { mutableStateOf<com.todoapp.domain.model.Environment?>(null) }

  LaunchedEffect(Unit) {
    email = container.settingsRepository.getEmail()
    baseUrl = container.settingsRepository.getBaseUrl()
    environment = container.settingsRepository.getEnvironment()
    refreshTodos(container, filter) { result, err ->
      todos = result
      error = err
    }
  }

  Column(modifier = androidx.compose.ui.Modifier.fillMaxSize().padding(24.dp)) {
    when (selectedTab) {
      0 -> {
        Text("Todos", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        FilterRow(filter = filter, onChange = {
          filter = it
          scope.launch {
            refreshTodos(container, it) { result, err ->
              todos = result
              error = err
            }
          }
        })
        error?.let { ErrorText(it) }
        todos.forEach { todo ->
          TodoCard(todo = todo, onToggle = {
            scope.launch {
              container.toggleTodoUseCase(todo.id)
              refreshTodos(container, filter) { result, err ->
                todos = result
                error = err
              }
            }
          }, onEdit = { onEdit(todo.id) }, onDelete = {
            scope.launch {
              container.deleteTodoUseCase(todo.id)
              refreshTodos(container, filter) { result, err ->
                todos = result
                error = err
              }
            }
          })
        }
        PrimaryButton("Nova tarefa", onClick = onCreate)
      }

      1 -> {
        Text("Perfil", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        Text("Email: ${email ?: "-"}")
        PrimaryButton("Sair", onClick = onLogout)
      }

      else -> {
        Text("Configuracoes", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        InputField("Base URL", baseUrl, onChange = { baseUrl = it })
        PrimaryButton("Salvar Base URL") {
          scope.launch {
            container.settingsRepository.setBaseUrl(baseUrl)
          }
        }
        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
          LinkButton("Dev") {
            scope.launch { container.settingsRepository.setEnvironment(com.todoapp.domain.model.Environment.Dev) }
          }
          LinkButton("Stage") {
            scope.launch { container.settingsRepository.setEnvironment(com.todoapp.domain.model.Environment.Stage) }
          }
          LinkButton("Prod") {
            scope.launch { container.settingsRepository.setEnvironment(com.todoapp.domain.model.Environment.Prod) }
          }
        }
        ThemePicker(onThemeChange)
        Text("Ambiente: ${environment?.name ?: ""}")
      }
    }

    Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
      LinkButton("Todos") { selectedTab = 0 }
      LinkButton("Perfil") { selectedTab = 1 }
      LinkButton("Config") { selectedTab = 2 }
    }
  }
}

@Composable
private fun TodoEditorScreen(
  container: AppContainer,
  todoId: String?,
  onDone: () -> Unit
) {
  val scope = rememberCoroutineScope()
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var dueDate by remember { mutableStateOf("") }
  var priority by remember { mutableStateOf(TodoPriority.Medium) }
  var isCompleted by remember { mutableStateOf(false) }
  var error by remember { mutableStateOf<String?>(null) }

  LaunchedEffect(todoId) {
    if (todoId != null) {
      when (val result = container.getTodoUseCase(todoId)) {
        is AppResult.Success -> {
          val todo = result.data
          title = todo.title
          description = todo.description ?: ""
          dueDate = todo.dueDate?.toString() ?: ""
          priority = todo.priority
          isCompleted = todo.isCompleted
        }

        is AppResult.Failure -> error = result.error.message
      }
    }
  }

  BaseAuthLayout(title = if (todoId == null) "Nova tarefa" else "Editar tarefa") {
    InputField("Titulo", title, onChange = { title = it })
    InputField("Descricao", description, onChange = { description = it })
    InputField("Vencimento (YYYY-MM-DDTHH:MM:SS)", dueDate, onChange = { dueDate = it })

    PriorityPicker(priority = priority, onSelect = { priority = it })

    LinkButton(if (isCompleted) "Concluida" else "Pendente") { isCompleted = !isCompleted }

    error?.let { ErrorText(it) }

    PrimaryButton(if (todoId == null) "Salvar" else "Atualizar", enabled = title.isNotBlank()) {
      val parsed = parseDateOrNull(dueDate)
      scope.launch {
        if (todoId == null) {
          container.createTodoUseCase(title.trim(), description.takeIf { it.isNotBlank() }, parsed, priority)
        } else {
          container.updateTodoUseCase(
            todoId,
            title.trim(),
            description.takeIf { it.isNotBlank() },
            parsed,
            priority,
            isCompleted
          )
        }
        onDone()
      }
    }

    if (todoId != null) {
      LinkButton("Excluir") {
        scope.launch {
          container.deleteTodoUseCase(todoId)
          onDone()
        }
      }
    }

    LinkButton("Voltar", onDone)
  }
}

private suspend fun refreshTodos(
  container: AppContainer,
  filter: TodosFilter,
  update: (List<com.todoapp.domain.model.Todo>, String?) -> Unit
) {
  when (val result = container.getTodosUseCase(filter)) {
    is AppResult.Success -> update(result.data, null)
    is AppResult.Failure -> update(emptyList(), result.error.message ?: "Erro")
  }
}

@Composable
private fun FilterRow(filter: TodosFilter, onChange: (TodosFilter) -> Unit) {
  Column {
    LinkButton("Todos") { onChange(filter.copy(isCompleted = null)) }
    LinkButton("Pendentes") { onChange(filter.copy(isCompleted = false)) }
    LinkButton("Concluidos") { onChange(filter.copy(isCompleted = true)) }
    LinkButton("Ordenar por prioridade") { onChange(filter.copy(sortBy = SortBy.Priority)) }
  }
}

@Composable
private fun PriorityPicker(priority: TodoPriority, onSelect: (TodoPriority) -> Unit) {
  Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
    LinkButton("Low") { onSelect(TodoPriority.Low) }
    LinkButton("Medium") { onSelect(TodoPriority.Medium) }
    LinkButton("High") { onSelect(TodoPriority.High) }
    LinkButton("Urgent") { onSelect(TodoPriority.Urgent) }
  }
}

@Composable
private fun ThemePicker(onThemeChange: (ThemeMode) -> Unit) {
  Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
    LinkButton("Sistema") { onThemeChange(ThemeMode.System) }
    LinkButton("Claro") { onThemeChange(ThemeMode.Light) }
    LinkButton("Escuro") { onThemeChange(ThemeMode.Dark) }
  }
}

@Composable
private fun BaseAuthLayout(title: String, content: @Composable () -> Unit) {
  Column(
    modifier = androidx.compose.ui.Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
  ) {
    androidx.compose.material3.Text(title, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
    content()
  }
}

@Composable
private fun InputField(label: String, value: String, onChange: (String) -> Unit, isPassword: Boolean = false) {
  androidx.compose.material3.OutlinedTextField(
    value = value,
    onValueChange = onChange,
    label = { androidx.compose.material3.Text(label) },
    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
    visualTransformation = if (isPassword) androidx.compose.ui.text.input.PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None
  )
}

@Composable
private fun PrimaryButton(text: String, enabled: Boolean = true, onClick: () -> Unit) {
  androidx.compose.material3.Button(
    onClick = onClick,
    enabled = enabled,
    modifier = androidx.compose.ui.Modifier.fillMaxWidth()
  ) {
    androidx.compose.material3.Text(text)
  }
}

@Composable
private fun LinkButton(text: String, onClick: () -> Unit) {
  androidx.compose.material3.TextButton(onClick = onClick) { androidx.compose.material3.Text(text) }
}

@Composable
private fun ErrorText(text: String) {
  androidx.compose.material3.Text(text, color = androidx.compose.material3.MaterialTheme.colorScheme.error)
}

@Composable
private fun TodoCard(
  todo: com.todoapp.domain.model.Todo,
  onToggle: () -> Unit,
  onEdit: () -> Unit,
  onDelete: () -> Unit
) {
  androidx.compose.material3.Card(
    modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(vertical = 6.dp),
    elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = androidx.compose.ui.Modifier.padding(16.dp),
      verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(6.dp)
    ) {
      androidx.compose.material3.Text(
        todo.title,
        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
      )
      todo.description?.let { androidx.compose.material3.Text(it) }
      androidx.compose.material3.Text("Prioridade: ${todo.priority}")
      todo.dueDate?.let { androidx.compose.material3.Text("Vence: $it") }
      Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
        LinkButton("Toggle", onToggle)
        LinkButton("Editar", onEdit)
        LinkButton("Excluir", onDelete)
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

private fun loadTheme(): ThemeMode {
  val defaults = NSUserDefaults.standardUserDefaults
  return when (defaults.stringForKey("theme_mode")) {
    ThemeMode.Light.name -> ThemeMode.Light
    ThemeMode.Dark.name -> ThemeMode.Dark
    else -> ThemeMode.System
  }
}

private fun saveTheme(mode: ThemeMode) {
  val defaults = NSUserDefaults.standardUserDefaults
  defaults.setObject(mode.name, "theme_mode")
}
