package com.example.infoquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.compose.AppTheme
import com.example.infoquizapp.view.component.practicecontentscreencomponent.data.PracticeContentData
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.infoquizapp.view.screen.PracticeContentScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val questions = listOf(
            PracticeContentData(
                id = 1,
                questionText = "Какое из перечисленных значений соответствует слову «характер»?",
                answers = listOf(
                    "Умственная способность человека или потенциал рациональной мысли.",
                    "Способность понимать явления без рассуждений.",
                    "Характеристика индивида с точки зрения психики.",
                    "Индивидуальный склад человека."
                ),
                correctAnswerIndex = 2
            ),
            PracticeContentData(
                id = 2,
                questionText = "Что является столицей Франции?",
                answers = listOf(
                    "Берлин",
                    "Париж",
                    "Рим",
                    "Мадрид"
                ),
                correctAnswerIndex = 1
            )
        )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme() {
                val currentQuestionIndex = remember { mutableStateOf(0) }

                PracticeContentScreen(
                    questions = questions,
                    currentQuestionIndex = currentQuestionIndex.value,
                    onQuestionSelected = { index ->
                        currentQuestionIndex.value = index // переход к выбранному вопросу
                    },
                    onAnswerSelected = { selectedIndex ->
                        // обработка ответа ну чисто на отъебись для примера
                        val currentQuestion = questions[currentQuestionIndex.value]
                        if (selectedIndex == currentQuestion.correctAnswerIndex) {
                            println("Ответ верный")
                        } else {
                            println("Ответ неверный")
                        }
                        // автоматический переход к следующему вопросу
                        if (currentQuestionIndex.value < questions.size - 1) {
                            currentQuestionIndex.value++
                        }
                    },
                    onSkip = {
                        // пропустить вопрос
                        if (currentQuestionIndex.value < questions.size - 1) {
                            currentQuestionIndex.value++
                        }
                    },
                    onExit = {
                        // действие при выходе
                        println("Выход из практики")
                    }
                )
            }
        }
    }
}
