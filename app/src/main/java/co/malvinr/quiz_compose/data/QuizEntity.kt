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
    initialCorrectlyMarked: Boolean = false,
    initialIncorrectlyMarked: Boolean = false,
) {
    var isSelected by mutableStateOf(initialSelected)
    var isCorrectlyMarked by mutableStateOf(initialCorrectlyMarked)
    var isIncorrectlyMarked by mutableStateOf(initialIncorrectlyMarked)
}