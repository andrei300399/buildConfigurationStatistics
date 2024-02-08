# Build Configuration Statistics Plugin

## Introduction

Плагин для просмотра статистики работы сборок Jenkins

## Запуск плагина

1. Перейти в папку склонированного плагина
2. Запустить плагин и Jenkins mvn hpi:run -Dport=5000 -Dhudson.security.csrf.GlobalCrumbIssuerConfiguration.DISABLE_CSRF_PROTECTION=true
3. http://localhost:5000/jenkins/ запустить jenkins
4. Создать сборку
5. Запустить сборку
6. Перейти в меню Build Configuration Statistics слева




