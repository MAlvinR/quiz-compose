package co.malvinr.quiz_compose.data

import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getQuizResources(): Flow<List<QuizEntity>>
}