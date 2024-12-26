package co.malvinr.quiz_compose.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class QuizEntity(
    val question: String,
    val answers: List<AnswerEntity>,
    initialAnswerSelected: Boolean = false
) {
    var isAnswerSelected by mutableStateOf(initialAnswerSelected)
}

class AnswerEntity(
    val answer: String,
    val isCorrect: Boolean,
    initialSelected: Boolean = false,
    initialGreenState: Boolean = false,
    initialRedState: Boolean = false,
) {
    var isSelected by mutableStateOf(initialSelected)
    var isGreenState by mutableStateOf(initialGreenState)
    var isRedState by mutableStateOf(initialRedState)
}