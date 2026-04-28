package com.obpiub.runculter.data.database

import com.obpiub.runculter.data.model.Character
import com.obpiub.runculter.data.model.HistoricalFact
import com.obpiub.runculter.data.model.Landmark

/**
 * Файл с данными о достопримечательностях
 * 
 * ДОБАВЛЯЙТЕ СВОИ ДОСТОПРИМЕЧАТЕЛЬНОСТИ ЗДЕСЬ!
 * 
 * Как получить координаты:
 * 1. Откройте Яндекс.Карты (https://yandex.ru/maps)
 * 2. Найдите вашу достопримечательность
 * 3. Кликните правой кнопкой мыши на нужное место
 * 4. Скопируйте координаты (широта, долгота)
 * 
 * Формат: latitude (широта), longitude (долгота)
 * Пример: 55.7539, 37.6208 (Красная площадь, Москва)
 */
object LandmarksData {
    
    /**
     * Список всех достопримечательностей для добавления в базу данных
     * 
     * ДОБАВЛЯЙТЕ СВОИ ДОСТОПРИМЕЧАТЕЛЬНОСТИ В ЭТОТ СПИСОК!
     */
    fun getLandmarks(): List<LandmarkData> = listOf(
        // ПРИМЕР 1: Красная площадь, Москва
        LandmarkData(
            landmark = Landmark(
                name = "Красная площадь",
                description = "Главная площадь Москвы, объект Всемирного наследия ЮНЕСКО",
                latitude = 55.7539,  // ⬅️ ЗАМЕНИТЕ НА ВАШИ КООРДИНАТЫ
                longitude = 37.6208, // ⬅️ ЗАМЕНИТЕ НА ВАШИ КООРДИНАТЫ
                isVisited = false
            ),
            character = Character(
                name = "Иван Грозный",
                description = "Первый русский царь, правитель XVI века",
                landmarkId = null, // Будет установлено автоматически
                dialogues = listOf(
                    "Добро пожаловать на Красную площадь!",
                    "Здесь происходили важные исторические события.",
                    "Знаете ли вы, что площадь существует с XV века?",
                    "Это место видело множество важных моментов истории России."
                ),
                isUnlocked = false
            ),
            facts = listOf(
                HistoricalFact(
                    title = "История Красной площади",
                    content = "Красная площадь была заложена в конце XV века по указу Ивана III.",
                    routeId = 1,
                    isUnlocked = false
                )
            )
        ),
        
        // ПРИМЕР 2: Московский Кремль
        LandmarkData(
            landmark = Landmark(
                name = "Московский Кремль",
                description = "Исторический комплекс в центре Москвы",
                latitude = 55.7520,  // ⬅️ ЗАМЕНИТЕ НА ВАШИ КООРДИНАТЫ
                longitude = 37.6175, // ⬅️ ЗАМЕНИТЕ НА ВАШИ КООРДИНАТЫ
                isVisited = false
            ),
            character = Character(
                name = "Петр I",
                description = "Великий реформатор и основатель Санкт-Петербурга",
                landmarkId = null,
                dialogues = listOf(
                    "Кремль - сердце Москвы!",
                    "Здесь принимались важные государственные решения.",
                    "История этого места насчитывает много веков.",
                    "Каждая башня Кремля имеет свою уникальную историю."
                ),
                isUnlocked = false
            ),
            facts = listOf(
                HistoricalFact(
                    title = "Архитектура Кремля",
                    content = "Московский Кремль включает в себя 20 башен и множество исторических зданий.",
                    routeId = 1,
                    isUnlocked = false
                )
            )
        ),
        
        // Триггер-зона с Ралиной
        LandmarkData(
            landmark = Landmark(
                name = "Океанская зона",
                description = "Особое место, где можно встретить Ралину",
                latitude = 37.421982,  // Указанные координаты
                longitude = -122.084796, // Указанные координаты
                isVisited = false
            ),
            character = Character(
                name = "Ралина",
                description = "Загадочный персонаж, знающий многое об акулах",
                landmarkId = null,
                dialogues = listOf(
                    "Привет! Я Ралина, и я знаю все об акулах!",
                    "Акулы - удивительные существа, которые живут в океанах уже более 400 миллионов лет.",
                    "Знаете ли вы, что у акул нет костей? Их скелет состоит из хрящей!",
                    "Акулы могут чувствовать электрические поля других животных - это помогает им охотиться.",
                    "Некоторые виды акул могут жить до 100 лет!"
                ),
                isUnlocked = false
            ),
            facts = listOf(
                HistoricalFact(
                    title = "Акулы - древние существа",
                    content = "Акулы появились на Земле более 400 миллионов лет назад, задолго до динозавров. Они пережили несколько массовых вымираний и продолжают существовать до сих пор.",
                    routeId = 1,
                    isUnlocked = false
                ),
                HistoricalFact(
                    title = "Уникальный скелет акул",
                    content = "В отличие от большинства рыб, у акул нет костей. Их скелет полностью состоит из хрящевой ткани, что делает их более гибкими и легкими.",
                    routeId = 1,
                    isUnlocked = false
                ),
                HistoricalFact(
                    title = "Электрорецепция акул",
                    content = "Акулы обладают уникальной способностью чувствовать электрические поля, создаваемые другими животными. Это помогает им находить добычу даже в полной темноте или когда она спрятана в песке.",
                    routeId = 1,
                    isUnlocked = false
                )
            )
        )
        
        // ⬇️ ДОБАВЛЯЙТЕ СВОИ ДОСТОПРИМЕЧАТЕЛЬНОСТИ ЗДЕСЬ ⬇️
        // Скопируйте пример выше и замените данные на свои:
        /*
        LandmarkData(
            landmark = Landmark(
                name = "Название вашей достопримечательности",
                description = "Описание",
                latitude = ВАША_ШИРОТА,  // Например: 55.7539
                longitude = ВАША_ДОЛГОТА, // Например: 37.6208
                isVisited = false
            ),
            character = Character(
                name = "Имя персонажа",
                description = "Описание персонажа",
                landmarkId = null,
                dialogues = listOf(
                    "Реплика 1",
                    "Реплика 2",
                    "Реплика 3",
                    "Реплика 4"
                ),
                isUnlocked = false
            ),
            facts = listOf(
                HistoricalFact(
                    title = "Название факта",
                    content = "Содержание факта",
                    routeId = 1,
                    isUnlocked = false
                )
            )
        ),
        */
    )
}

/**
 * Класс-обертка для данных достопримечательности
 */
data class LandmarkData(
    val landmark: Landmark,
    val character: Character,
    val facts: List<HistoricalFact> = emptyList()
)
