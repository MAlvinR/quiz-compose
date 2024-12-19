package co.malvinr.quiz_compose.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListDataResponse<T>(val results: T)

@Serializable
data class QuizResponse(
    val question: String,
    @SerialName("correct_answer") val correctAnswer: String,
    @SerialName("incorrect_answers") val incorrectAnswers: List<String>,
)

