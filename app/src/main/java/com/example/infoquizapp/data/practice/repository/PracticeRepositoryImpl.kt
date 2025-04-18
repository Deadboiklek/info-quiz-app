package com.example.infoquizapp.data.practice.repository

import com.example.infoquizapp.data.PracticeDao
import com.example.infoquizapp.data.practice.PracticeEntity
import com.example.infoquizapp.domain.practice.repository.PracticeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PracticeRepositoryImpl(private val practiceDao: PracticeDao) : PracticeRepository {

    override suspend fun getPractice(id: Int): PracticeEntity? {
        return practiceDao.getPracticeById(id)
    }

    override suspend fun markPracticeAsDone(id: Int) {
        return practiceDao.updatePracticeStatus(id, true)
    }

    override suspend fun getAllPractice(): List<PracticeEntity> {
        return practiceDao.getAllPractice()
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (practiceDao.getPracticeCount() == 0) {
                val initialData = listOf(
                    PracticeEntity(
                        title = "Задание 1",
                        description = "В этом задании проверяется умение работать с объёмом информации, кодировкой символов и переводом между единицами измерения.",
                        isDone = false,
                        type = "type1"
                    ),
                    PracticeEntity(
                        title = "Задание 2",
                        description = "Анализ и декодирование информации (шифры, кодирование, однозначность расшифровки)",
                        isDone = false,
                        type = "type2"
                    ),
                    PracticeEntity(
                        title = "Задание 3",
                        description = "Работа с переменными, логическими выражениями и числовыми условиями",
                        isDone = false,
                        type = "type3"
                    ),
                    PracticeEntity(
                        title = "Задание 4",
                        description = "Формальные описания реальных объектов и процессов (анализ схем и таблиц)",
                        isDone = false,
                        type = "type4"
                    ),
                    PracticeEntity(
                        title = "Задание 5",
                        description = "Простой линейный алгоритм для формального исполнителя",
                        isDone = false,
                        type = "type5"
                    ),
                    PracticeEntity(
                        title = "Задание 6",
                        description = "Анализ программ с условными операторами",
                        isDone = false,
                        type = "type6"
                    ),
                    PracticeEntity(
                        title = "Задание 7",
                        description = "Работа с IP-адресами, электронной почтой и адресами файлов",
                        isDone = false,
                        type = "type7"
                    ),
                    PracticeEntity(
                        title = "Задание 8",
                        description = "Работа с логическими запросами (И, ИЛИ, НЕ) и расчёт количества найденных страниц",
                        isDone = false,
                        type = "type8"
                    ),
                    PracticeEntity(
                        title = "Задание 9",
                        description = "Поиск путей в графах между городами с учётом условий (прохождение через пункт или его исключение)",
                        isDone = false,
                        type = "type9"
                    ),
                    PracticeEntity(
                        title = "Задание 10",
                        description = "Сравнение чисел в различных системах счисления. Перевод чисел, поиск наибольшего/наименьшего, сумма и количество цифр",
                        isDone = false,
                        type = "type10"
                    ),
                    PracticeEntity(
                        title = "Задание 11",
                        description = "Использование поиска в операционной системе и текстовом редакторе",
                        isDone = false,
                        type = "type11"
                    ),
                )
                practiceDao.insertAllPractice(initialData)
            }
        }
    }
}