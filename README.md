# TODO App — Kotlin Multiplatform (Android + iOS)

App de gerenciamento pessoal de tarefas com Clean Architecture, KMP, UI separada por plataforma (Compose), autenticacao JWT e CRUD completo.

## ✅ Funcionalidades

- Auth: Login, Cadastro, Esqueci Senha, Reset Senha
- TODOs: listar, filtrar, criar, editar, toggle, deletar
- Config: base URL por ambiente + tema claro/escuro

## 🧱 Arquitetura

Multi‑módulo KMP:

```
:domain   -> entidades + use cases + contratos
:data     -> Ktor + DTOs + mappers + storage
:app-android -> UI Android (Compose + Hilt)
:app-ios     -> UI iOS (Compose Multiplatform)
```

## 🔧 Requisitos

- JDK 17+
- Android Studio (para Android)
- Xcode (para iOS)

## ▶️ Android

```
./gradlew :app-android:assembleDebug
```

No app, ajuste a Base URL em **Config** (ex.: `http://10.0.2.2:5001` no emulador).
Defaults:
- Android (emulador): `http://10.0.2.2:5001`
- iOS (simulador): `http://localhost:5001`

## ▶️ iOS

O módulo `:app-ios` gera um framework (`TodoIos`).
Crie um projeto iOS no Xcode e aponte para o framework gerado.

## 📄 Docs

- Design doc: `docs/plans/2025-12-27-todo-kmp-design.md`
