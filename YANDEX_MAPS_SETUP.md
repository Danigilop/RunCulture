# Инструкция по подключению Яндекс карт

## Проблема
Репозиторий `https://maven.yandex.ru` недоступен или требует авторизации.

## Решение: Использование локального AAR файла

### Шаг 1: Скачайте AAR файл
1. Перейдите на https://yandex.ru/dev/mapkit/
2. Зарегистрируйтесь в кабинете разработчика
3. Скачайте AAR файл библиотеки MapKit
4. Поместите файл в папку `app/libs/mapkit.aar`

### Шаг 2: Обновите build.gradle.kts
Раскомментируйте строку в `app/build.gradle.kts`:
```kotlin
implementation(files("libs/mapkit.aar"))
```
И закомментируйте:
```kotlin
// implementation("com.yandex.android:mapkit:4.2.0")
```

### Шаг 3: Синхронизируйте проект
- File → Sync Project with Gradle Files

## Альтернатива: Использование Google Maps

Если Яндекс карты недоступны, можно использовать Google Maps:
1. Получите API ключ Google Maps
2. Добавьте зависимость: `implementation("com.google.android.gms:play-services-maps:18.2.0")`
3. Обновите код в MapFragment.kt для работы с Google Maps
