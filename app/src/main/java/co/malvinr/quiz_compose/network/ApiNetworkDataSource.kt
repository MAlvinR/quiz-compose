package co.malvinr.quiz_compose.network

import co.malvinr.quiz_compose.network.model.ListDataResponse
import co.malvinr.quiz_compose.network.model.QuizResponse

interface ApiNetworkDataSource {
    suspend fun getQuizzes(): ListDataResponse<List<QuizResponse>>
}