# Базовый образ с Java 21 (только JRE)
FROM eclipse-temurin:21-jre

# Рабочая директория внутри контейнера
WORKDIR /app

# Копируем наш jar в контейнер
COPY build/libs/BankApplicationSimulation-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт 8080
EXPOSE 8080

# Команда запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
