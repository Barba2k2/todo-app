#!/bin/bash

echo "🔧 Compilando TODO App..."
./gradlew installDist -q

if [ $? -eq 0 ]; then
    echo "✅ Compilação concluída!"
    echo "🚀 Iniciando TODO App..."
    echo ""
    ./build/install/todo-app/bin/todo-app
else
    echo "❌ Erro na compilação!"
    exit 1
fi
