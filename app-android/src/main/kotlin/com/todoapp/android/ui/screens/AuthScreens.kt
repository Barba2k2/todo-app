package com.todoapp.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.todoapp.android.ui.components.PrimaryButton
import com.todoapp.android.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
  viewModel: AuthViewModel,
  onRegister: () -> Unit,
  onForgot: () -> Unit,
  onSuccess: () -> Unit
) {
  val state by viewModel.state.collectAsState()
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  LaunchedEffect(state.message) {
    if (!state.message.isNullOrBlank()) {
      onSuccess()
      viewModel.clearMessage()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("Entrar", style = MaterialTheme.typography.titleLarge)

    OutlinedTextField(
      value = email,
      onValueChange = { email = it },
      label = { Text("Email") },
      modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("Senha") },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth()
    )

    if (state.error != null) {
      Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
    }

    PrimaryButton(
      text = if (state.loading) "Entrando" else "Entrar",
      enabled = !state.loading && email.isNotBlank() && password.isNotBlank(),
      modifier = Modifier.fillMaxWidth(),
      onClick = { viewModel.login(email.trim(), password) }
    )

    if (state.loading) {
      CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
    }

    TextButton(onClick = onRegister) { Text("Criar conta") }
    TextButton(onClick = onForgot) { Text("Esqueci minha senha") }
  }
}

@Composable
fun RegisterScreen(
  viewModel: AuthViewModel,
  onBack: () -> Unit,
  onSuccess: () -> Unit
) {
  val state by viewModel.state.collectAsState()
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var confirm by remember { mutableStateOf("") }

  LaunchedEffect(state.message) {
    if (!state.message.isNullOrBlank()) {
      onSuccess()
      viewModel.clearMessage()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("Criar conta", style = MaterialTheme.typography.titleLarge)

    OutlinedTextField(
      value = email,
      onValueChange = { email = it },
      label = { Text("Email") },
      modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("Senha") },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
      value = confirm,
      onValueChange = { confirm = it },
      label = { Text("Confirmar senha") },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth()
    )

    if (state.error != null) {
      Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
    }

    PrimaryButton(
      text = if (state.loading) "Criando" else "Criar conta",
      enabled = !state.loading && email.isNotBlank() && password.isNotBlank() && confirm.isNotBlank(),
      modifier = Modifier.fillMaxWidth(),
      onClick = { viewModel.register(email.trim(), password, confirm) }
    )

    Spacer(modifier = Modifier.height(4.dp))
    TextButton(onClick = onBack) { Text("Voltar") }
  }
}

@Composable
fun ForgotPasswordScreen(
  viewModel: AuthViewModel,
  onBack: () -> Unit,
  onReset: () -> Unit
) {
  val state by viewModel.state.collectAsState()
  var email by remember { mutableStateOf("") }

  LaunchedEffect(state.message) {
    if (!state.message.isNullOrBlank()) {
      onReset()
      viewModel.clearMessage()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("Recuperar senha", style = MaterialTheme.typography.titleLarge)

    OutlinedTextField(
      value = email,
      onValueChange = { email = it },
      label = { Text("Email") },
      modifier = Modifier.fillMaxWidth()
    )

    if (state.error != null) {
      Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
    }

    PrimaryButton(
      text = if (state.loading) "Enviando" else "Enviar token",
      enabled = !state.loading && email.isNotBlank(),
      modifier = Modifier.fillMaxWidth(),
      onClick = { viewModel.forgotPassword(email.trim()) }
    )

    TextButton(onClick = onBack) { Text("Voltar") }
  }
}

@Composable
fun ResetPasswordScreen(
  viewModel: AuthViewModel,
  onBack: () -> Unit
) {
  val state by viewModel.state.collectAsState()
  var email by remember { mutableStateOf("") }
  var token by remember { mutableStateOf("") }
  var newPassword by remember { mutableStateOf("") }
  var confirm by remember { mutableStateOf("") }

  LaunchedEffect(state.message) {
    if (!state.message.isNullOrBlank()) {
      viewModel.clearMessage()
      onBack()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("Resetar senha", style = MaterialTheme.typography.titleLarge)

    OutlinedTextField(
      value = email,
      onValueChange = { email = it },
      label = { Text("Email") },
      modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
      value = token,
      onValueChange = { token = it },
      label = { Text("Token") },
      modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
      value = newPassword,
      onValueChange = { newPassword = it },
      label = { Text("Nova senha") },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
      value = confirm,
      onValueChange = { confirm = it },
      label = { Text("Confirmar senha") },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth()
    )

    if (state.error != null) {
      Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
    }

    PrimaryButton(
      text = if (state.loading) "Salvando" else "Resetar",
      enabled = !state.loading && email.isNotBlank() && token.isNotBlank() && newPassword.isNotBlank() && confirm.isNotBlank(),
      modifier = Modifier.fillMaxWidth(),
      onClick = { viewModel.resetPassword(email.trim(), token, newPassword, confirm) }
    )

    TextButton(onClick = onBack) { Text("Voltar") }
  }
}
