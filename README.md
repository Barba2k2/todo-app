# 📝 TODO App - Kotlin

Um aplicativo de lista de tarefas (TODO) simples e interativo em Kotlin com interface de linha de comando (CLI).

## 🚀 Funcionalidades

- ➕ Adicionar novos TODOs com título, descrição e prioridade
- 📋 Listar todos os TODOs
- 🔍 Filtrar TODOs por status (Pendente, Em Progresso, Completo)
- ✏️ Atualizar status dos TODOs
- 🗑️ Deletar TODOs
- 📊 Ver estatísticas (total, por status, por prioridade, taxa de conclusão)

## 📦 Estrutura do Projeto

```
todo-app/
├── src/main/kotlin/com/todoapp/
│   ├── Main.kt                      # Ponto de entrada
│   ├── model/
│   │   └── Todo.kt                  # Modelo de dados
│   ├── repository/
│   │   └── TodoRepository.kt        # Gerenciamento em memória
│   └── ui/
│       └── TodoCLI.kt               # Interface CLI
├── build.gradle.kts                 # Configuração do Gradle
└── settings.gradle.kts
```

## 🛠️ Requisitos

- Java JDK 17 ou superior
- Gradle (ou use o wrapper incluído)

## ▶️ Como Executar

### Opção 1: Criar e executar a distribuição (recomendado)

```bash
cd todo-app
./gradlew installDist
./build/install/todo-app/bin/todo-app
```

### Opção 2: Usando Gradle run (pode ter problemas com input)

```bash
./gradlew run --console=plain
```

### Opção 3: Compilar e executar JAR

```bash
./gradlew build
java -cp build/libs/todo-app-1.0.0.jar com.todoapp.MainKt
```

## 📖 Como Usar

Ao executar o app, você verá um menu interativo:

```
📝 Menu Principal
========================================
1. ➕ Adicionar TODO
2. 📋 Listar todos
3. 🔍 Filtrar por status
4. ✏️ Atualizar status
5. 🗑️ Deletar TODO
6. 📊 Estatísticas
0. 🚪 Sair
```

### Adicionar TODO
- Informe o título (obrigatório)
- Informe a descrição (opcional)
- Escolha a prioridade: LOW (1), MEDIUM (2), HIGH (3)

### Gerenciar TODOs
- Use o ID exibido para atualizar ou deletar TODOs
- TODOs podem ter 3 status: PENDING, IN_PROGRESS, COMPLETED

## 🎯 Modelo de Dados

### Todo
- `id`: UUID único
- `title`: Título da tarefa
- `description`: Descrição opcional
- `priority`: LOW, MEDIUM ou HIGH
- `status`: PENDING, IN_PROGRESS ou COMPLETED
- `createdAt`: Data/hora de criação
- `updatedAt`: Data/hora da última atualização

## 🧪 Executar Testes

```bash
./gradlew test
```

## 📝 Licença

Este é um projeto de exemplo para aprendizado.
